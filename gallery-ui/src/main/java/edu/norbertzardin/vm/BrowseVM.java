package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.impl.ValidationMessagesImpl;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import java.util.Date;
import java.util.List;

public class BrowseVM {
    private String title;

    private Integer imagePage;
    private Long imagePageCount;
    private Integer pageImageMax;
    private List<ImageEntity> imageList;

    private Integer cataloguePage;
    private Long cataloguePageCount;
    private Integer pageCatalogueMax;
    private List<CatalogueEntity> catalogueList;

    private CatalogueEntity defaultCatalogue;
    private CatalogueEntity selectedCatalogue;
    private CatalogueEntity editCatalogue;

    private Boolean backButton;

    @WireVariable
    private CatalogueService catalogueService;

    @WireVariable
    private ImageService imageService;

    // Initializers
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view,
                     @BindingParam("pageImageMax") Integer imageMax,
                     @BindingParam("pageCatalogueMax") Integer folderMax) {

        Selectors.wireComponents(view, this, false);
        String defaultCatalogueName = "Non-categorized";

        setImagePage(1);
        setCataloguePage(1);

        setPageImageMax(imageMax);
        setPageCatalogueMax(folderMax);

        setDefaultCatalogue(catalogueService.getCatalogueByNameNoFetch(defaultCatalogueName));
        setSelectedCatalogue(defaultCatalogue);

        updateCatalogues();
        updateImages();
        setBackButton(false);
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    // Commands
    @Command
    @NotifyChange({"catalogueList", "cataloguePageCount", "title"})
    public void createCatalogue() {
        CatalogueEntity ce = new CatalogueEntity();
        ce.setTitle(title);
        ce.setCreatedDate(new Date());
        catalogueService.createCatalogue(ce);
        updateCatalogues();
//        setCataloguePage(cataloguePageCount.intValue());
    }

    @Command
    @NotifyChange({"imageList", "selectedCatalogue", "backButton", "imagePageCount"})
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        setImagePage(1);                                                                                                     // Reset imagePage back to first
        setSelectedCatalogue(catalogueService.getCatalogueById(ce.getId()));                                            // Set current catalogue to the selected one
        updateImages();                                                                                                   // Load images from the selected folder
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
        setCataloguePageCount(catalogueService.getCataloguePageCount(pageCatalogueMax));
        if (cataloguePage < cataloguePageCount.intValue()) {
            cataloguePage++;
            setCatalogueList(catalogueService.getCatalogueListByPage(getCataloguePage(), pageCatalogueMax));
        }
    }



    @Command
    @NotifyChange({"imagePage", "imageList", "imagePageCount"})
    public void nextPage() {
        if (!imagePage.equals(imagePageCount.intValue())) {
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
    @NotifyChange("editCatalogue")
    public void selectEditCatalogue(@BindingParam("editCatalogue") CatalogueEntity edit) {
        setEditCatalogue(edit);
    }

    @Command
    @NotifyChange({"editCatalogue", "catalogueList"})
    public void editCatalogue() {
        editCatalogue.setTitle(editCatalogue.getTitle());
        catalogueService.editCatalogue(editCatalogue);
        setCatalogueList(catalogueService.getCatalogueListByPage(cataloguePage, pageCatalogueMax));
    }

    private void updateImages() {
        if (selectedCatalogue != null) {
            setImagePageCount(catalogueService.getPageCount(selectedCatalogue, pageImageMax));
            setImageList(imageService.getImagesFromFolderForPage(getImagePage(), pageImageMax, selectedCatalogue));
        }
    }

    public List<ImageEntity> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageEntity> imageList) {
        this.imageList = imageList;
    }

    @Command
    @NotifyChange({"selectedCatalogue", "catalogueList", "cataloguePage", "cataloguePageCount", "backButton", "imageList", "imagePageCount", "imagePage"})
    public void deleteCatalogue() {
        catalogueService.deleteCatalogue(editCatalogue);
        updateCatalogues();

        if(catalogueList.isEmpty()) {
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
    public void clearMessages(@BindingParam("errors") ValidationMessagesImpl messages){
        messages.clearAllMessages();
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

    public Long getImagePageCount() {
        return imagePageCount;
    }

    public void setImagePageCount(Long imagePageCount) {
        this.imagePageCount = imagePageCount;
    }

    public void setPageImageMax(Integer pageMax) {
        this.pageImageMax = pageMax;
    }

    public void setPageCatalogueMax(Integer pageCatalogueMax) {
        this.pageCatalogueMax = pageCatalogueMax;
    }

    public Long getCataloguePageCount() {
        return cataloguePageCount;
    }

    public Integer getCataloguePage() {
        return cataloguePage;
    }

    public void setCataloguePageCount(Long cataloguePageCount) {
        this.cataloguePageCount = cataloguePageCount;
    }

    public void setCataloguePage(Integer cataloguePage) {
        this.cataloguePage = cataloguePage;
    }

    private void updateCatalogues() {
        setCataloguePageCount(catalogueService.getCataloguePageCount(pageCatalogueMax));
        setCatalogueList(catalogueService.getCatalogueListByPage(getCataloguePage(), pageCatalogueMax));
    }
}
