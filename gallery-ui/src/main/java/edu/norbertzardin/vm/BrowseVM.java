package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import java.util.Date;
import java.util.List;

public class BrowseVM {
    // Properties
    private String name;

    private List<CatalogueEntity> catalogueList;
    private List<ImageEntity> imageList;
    private CatalogueEntity selectedCatalogue;
    private CatalogueEntity editCatalogue;
    private Boolean backButton;
    private CatalogueEntity defaultCatalogue;

    @WireVariable
    private CatalogueService catalogueService;

    @WireVariable
    private ImageService imageService;

    @WireVariable
    private TagService tagService;

    // Initializers
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        String defaultCatalogueName = "Non-categorized";
        setDefaultCatalogue(catalogueService.getCatalogueByName(defaultCatalogueName));
        setSelectedCatalogue(defaultCatalogue);
        setCatalogueList(catalogueService.getCatalogueList());
        loadImages();
        setBackButton(false);
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        loadImages();
    }

    // Commands
    @Command
    @NotifyChange({"catalogueList", "name"})
    public void createCatalogue() {
        CatalogueEntity ce = new CatalogueEntity();
        ce.setTitle(name);
        ce.setCreatedDate(new Date());
        catalogueService.createCatalogue(ce);
        catalogueList = catalogueService.getCatalogueList();
        setName(""); // clear name property after catalogue is created
    }

    @NotifyChange({"imageList", "selectedCatalogue", "backButton"})
    @Command
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        setSelectedCatalogue(catalogueService.getCatalogueById(ce.getId()));
        loadImages();
        setBackButton(true);
    }



    @Command
    @NotifyChange({"selectedCatalogue", "backButton", "imageList"})
    public void goBack() {
        setSelectedCatalogue(defaultCatalogue);
        loadImages();
        setBackButton(false);
    }

    @Command
    @NotifyChange("editCatalogue")
    public void selectEditCatalogue(@BindingParam("editCatalogue") CatalogueEntity edit) {
        setEditCatalogue(edit);
    }

    @Command
    public void editCatalogue() {
        catalogueService.editCatalogue(editCatalogue);
    }

    public void loadImages() {
        setSelectedCatalogue(catalogueService.getCatalogueById(selectedCatalogue.getId()));
        if (selectedCatalogue != null) {
            setImageList(selectedCatalogue.getImages());
        }
    }

    public List<ImageEntity> getImageList() {
        return imageList;
    }

    @Command
    @NotifyChange({"selectedCatalogue", "catalogueList", "backButton", "imageList"})
    public void deleteCatalogue() {
        if (selectedCatalogue.getId().equals(editCatalogue.getId())) {
            setSelectedCatalogue(defaultCatalogue);
            loadImages();
            setImageList(selectedCatalogue.getImages());
            setBackButton(false);
        }
        catalogueService.deleteCatalogue(editCatalogue);
        setCatalogueList(catalogueService.getCatalogueList());
    }

    @GlobalCommand
    @NotifyChange("imageList")
    public void reload () {

        loadImages();
    }

    // Setters
    public void setEditCatalogue(CatalogueEntity editCatalogue) {
        this.editCatalogue = editCatalogue;
    }

    public void setDefaultCatalogue(CatalogueEntity defaultCatalogue) {
        this.defaultCatalogue = defaultCatalogue;
    }

    public void setBackButton(Boolean backButton) {
        this.backButton = backButton;
    }

    public void setImageList(List<ImageEntity> imageList) {
        this.imageList = imageList;
    }

    public void setCatalogueList(List<CatalogueEntity> catalogueList) {
        this.catalogueList = catalogueList;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setSelectedCatalogue(CatalogueEntity selectedCatalogue) {
        this.selectedCatalogue = selectedCatalogue;
    }

    public void setCatalogueService(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters
    public CatalogueEntity getEditCatalogue() {
        return editCatalogue;
    }

    public Boolean getBackButton() {
        return backButton;
    }

    public List<CatalogueEntity> getCatalogueList() {
        return catalogueList;
    }

    public CatalogueEntity getDefaultCatalogue() {
        return defaultCatalogue;
    }

    public CatalogueEntity getSelectedCatalogue() {
        return selectedCatalogue;
    }

    public ImageService getImageService() {
        return imageService;
    }

    public CatalogueService getCatalogueService() {
        return catalogueService;
    }

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }
}