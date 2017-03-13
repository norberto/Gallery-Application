package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Date;
import java.util.List;

public class BrowseVM {
    private String title;

    private Integer imagePage;
    private Integer imagePageCount;
    private Integer pageImageMax;
    private List<ImageEntity> imageList;
    private Boolean removeConfirmation;
    private Boolean changed;

    private Integer cataloguePage;
    private Integer cataloguePageCount;
    private Integer pageCatalogueMax;
    private List<CatalogueEntity> catalogueList;

    private CatalogueEntity defaultCatalogue;
    private CatalogueEntity selectedCatalogue;
    private CatalogueEntity editCatalogue;
    private ValidationMessages vmsgs;

    private Boolean backButton;

    @WireVariable
    private CatalogueService catalogueService;

    @WireVariable
    private ImageService imageService;
    private Boolean saved;

    // Initializers
    @Init
    public void init(@BindingParam("pageImageMax") Integer imageMax,
                     @BindingParam("pageCatalogueMax") Integer folderMax,
                     @ContextParam(ContextType.BINDER) Binder binder) {

        String defaultCatalogueName = "Non-categorized";
        setChanged(false);
        setRemoveConfirmation(false);
        setImagePage(1);
        setCataloguePage(1);

        setPageImageMax(imageMax);
        setPageCatalogueMax(folderMax);
        setSaved(false);

        setDefaultCatalogue(catalogueService.loadNoFetch(defaultCatalogueName));
        setSelectedCatalogue(defaultCatalogue);

        updateCatalogues();
        updateImages();
        setBackButton(false);
        vmsgs = ((BinderCtrl) binder).getValidationMessages();
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    // Commands
    @Command
    @NotifyChange({"catalogueList", "cataloguePageCount", "title", "changed", "saved"})
    public void createCatalogue() {
        CatalogueEntity ce = new CatalogueEntity();
        ce.setTitle(title);
        ce.setCreatedDate(new Date());
        clearMessages();
        if(!catalogueService.create(ce)) {
            vmsgs.addMessages(null, null, "new_title", new String[] {"Catalogue already exists."});
        } else {
            vmsgs.clearKeyMessages("new_title");
        }
        updateCatalogues();
        setChanged(false);
        setSaved(true);
    }

    @Command
    @NotifyChange({"imageList", "selectedCatalogue", "backButton", "imagePageCount"})
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        setImagePage(1);                                                                                                // Reset imagePage back to first
        setSelectedCatalogue(catalogueService.load(ce.getId()));                                            // Set current catalogue to the selected one
        updateImages();                                                                                                 // Load images from the selected folder
        setBackButton(true);                                                                                            // Enable "Go back" button
    }

    @Command
    @NotifyChange({"imagePage", "imageList", "imagePageCount"})
    public void previousPage() {
        if (imagePage != 1) {
            imagePage--;
            updateImages();
        }
    }

    @Command
    @NotifyChange({"cataloguePage", "catalogueList", "cataloguePageCount"})
    public void previousCataloguePage() {
        if (cataloguePage > 1) {
            cataloguePage--;
            updateCatalogues();
        }
    }

    @Command
    @NotifyChange({"cataloguePage", "catalogueList", "cataloguePageCount"})
    public void nextCataloguePage() {
        setCataloguePageCount(catalogueService.count(pageCatalogueMax, null, false));
        if (cataloguePage < cataloguePageCount) {
            cataloguePage++;
            setCatalogueList(catalogueService.loadByPage(getCataloguePage(), pageCatalogueMax, null, false));
        }
    }


    @Command
    @NotifyChange({"imagePage", "imageList", "imagePageCount"})
    public void nextPage() {
        if (!imagePage.equals(imagePageCount)) {
            imagePage++;
            updateImages();
        }
    }

    @Command
    @NotifyChange({"selectedCatalogue", "backButton", "imageList", "imagePageCount", "catalogueList", "cataloguePageCount"})
    public void goBack() {
        setSelectedCatalogue(defaultCatalogue);
        setImagePage(1);
        updateCatalogues();
        updateImages();
        setBackButton(false);
    }

    @Command
    @NotifyChange({"changed", "saved"})
    public void changed(@BindingParam("state") Boolean value) {
        setChanged((value != null) ? value : true);
        setSaved(false);
    }

    @Command
    @NotifyChange({"editCatalogue", "saved", "changed", "removeConfirmation"})
    public void selectEditCatalogue(@BindingParam("editCatalogue") CatalogueEntity edit) {
        setSaved(false);
        setChanged(false);
        setRemoveConfirmation(false);
        setEditCatalogue(edit);
    }

    @Command
    @NotifyChange({"editCatalogue", "catalogueList", "changed", "saved"})
    public void editCatalogue() {
        editCatalogue.setTitle(editCatalogue.getTitle());
        catalogueService.update(editCatalogue);
        setCatalogueList(catalogueService.loadByPage(cataloguePage, pageCatalogueMax, null, false));
        setChanged(false);
        setSaved(true);
    }

    private void updateImages() {
        if (selectedCatalogue != null) {
            setImagePageCount(catalogueService.imageCount(selectedCatalogue, pageImageMax));
            setImageList(imageService.loadCatalogueImages(getImagePage(), pageImageMax, selectedCatalogue));
        }
    }

    public List<ImageEntity> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageEntity> imageList) {
        this.imageList = imageList;
    }

    @Command
    @NotifyChange({"selectedCatalogue", "catalogueList", "cataloguePage", "cataloguePageCount", "backButton", "imageList", "imagePageCount", "imagePage", "removeConfirmation"})
    public void deleteCatalogue() {
        catalogueService.remove(editCatalogue);
        setRemoveConfirmation(false);
        updateCatalogues();

        if (catalogueList.isEmpty()) {
            previousCataloguePage();
        }

        if (selectedCatalogue.getId().equals(editCatalogue.getId())) {
            setSelectedCatalogue(defaultCatalogue);
            updateImages();
            setBackButton(false);
        }
    }

    @GlobalCommand
    @NotifyChange({"imageList", "imagePageCount"})
    public void reload() {
        updateImages();
    }

    @Command
    @NotifyChange({"removeConfirmation"})
    public void changeConfirmationState(@BindingParam("confirm") Boolean state) {
        setRemoveConfirmation(state);
    }

    @Command
    public void clearMessages() {
        vmsgs.clearAllMessages();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    // Getters
    public CatalogueEntity getEditCatalogue() {
        return editCatalogue;
    }

    // Setters
    public void setEditCatalogue(CatalogueEntity editCatalogue) {
        this.editCatalogue = editCatalogue;
    }

    public Boolean getBackButton() {
        return backButton;
    }

    public void setBackButton(Boolean backButton) {
        this.backButton = backButton;
    }

    public List<CatalogueEntity> getCatalogueList() {
        return catalogueList;
    }

    public void setCatalogueList(List<CatalogueEntity> catalogueList) {
        this.catalogueList = catalogueList;
    }

    public CatalogueEntity getDefaultCatalogue() {
        return defaultCatalogue;
    }

    public void setDefaultCatalogue(CatalogueEntity defaultCatalogue) {
        this.defaultCatalogue = defaultCatalogue;
    }

    public CatalogueEntity getSelectedCatalogue() {
        return selectedCatalogue;
    }

    public void setSelectedCatalogue(CatalogueEntity selectedCatalogue) {
        this.selectedCatalogue = selectedCatalogue;
    }

    public ImageService getImageService() {
        return imageService;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public CatalogueService getCatalogueService() {
        return catalogueService;
    }

    public void setCatalogueService(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    public Integer getImagePage() {
        return imagePage;
    }

    public void setImagePage(Integer imagePage) {
        this.imagePage = imagePage;
    }

    public Integer getImagePageCount() {
        return imagePageCount;
    }

    public void setImagePageCount(Integer imagePageCount) {
        this.imagePageCount = imagePageCount;
    }

    public void setPageImageMax(Integer pageMax) {
        this.pageImageMax = pageMax;
    }

    public void setPageCatalogueMax(Integer pageCatalogueMax) {
        this.pageCatalogueMax = pageCatalogueMax;
    }

    public Integer getCataloguePageCount() {
        return cataloguePageCount;
    }

    public Integer getCataloguePage() {
        return cataloguePage;
    }

    public void setCataloguePageCount(Integer cataloguePageCount) {
        this.cataloguePageCount = cataloguePageCount;
    }

    public void setCataloguePage(Integer cataloguePage) {
        this.cataloguePage = cataloguePage;
    }

    private void updateCatalogues() {
        setCataloguePageCount(catalogueService.count(pageCatalogueMax, null, false));
        setCatalogueList(catalogueService.loadByPage(getCataloguePage(), pageCatalogueMax, null, false));
    }

    public Boolean getRemoveConfirmation() {
        return removeConfirmation;
    }

    public void setRemoveConfirmation(Boolean removeConfirmation) {
        this.removeConfirmation = removeConfirmation;
    }

    public boolean getChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public Boolean getSaved() {
        return saved;
    }
}
