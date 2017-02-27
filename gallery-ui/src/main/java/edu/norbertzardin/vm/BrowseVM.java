package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;

import java.util.Date;
import java.util.List;

public class BrowseVM {
    // Properties
    private String name;

    private List<CatalogueEntity> catalogueList;
    private List<ImageEntity> imageList;
    private List<TagEntity> tagList;

    private CatalogueEntity selectedCatalogue;
    private ImageEntity selectedImage;
    private CatalogueEntity editCatalogue;
    private Boolean backButton;
    private boolean editMode;


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

    @Command
    @NotifyChange({"selectedImage", "tagList"})
    public void viewImage(@BindingParam("selectedImage") ImageEntity image) {
        setSelectedImage(imageService.getImageByIdFullFetch(image.getId()));
        loadTags();
    }

    @NotifyChange({"imageList", "selectedCatalogue", "backButton"})
    @Command
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        setSelectedCatalogue(catalogueService.getCatalogueById(ce.getId()));
        loadImages();
        setBackButton(true);
    }

    @Command
    public void onDownload() {
        setSelectedImage(imageService.getImageByIdFullFetch(selectedImage.getId()));
        Filedownload.save(selectedImage.getDownload().getData(),
                "image/" + selectedImage.getDatatype(),
                "download_" + selectedImage.toString());

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

    @Command
    @NotifyChange("tagList")
    public void loadTags() {
        setTagList(selectedImage.getTags());
    }

    @Command
    @NotifyChange({"selectedImage", "imageList", "selectedCatalogue"})
    public void deleteImage(){
        imageService.deleteImage(selectedImage);
        setSelectedCatalogue(catalogueService.getCatalogueById(selectedCatalogue.getId()));
        selectedImage = null;
        loadImages();
    }

    @Command
    @NotifyChange({"selectedImage", "editMode"})
    public void editImage() {
        imageService.editImage(selectedImage);
        setEditMode(false);
    }

    @Command
    @NotifyChange("editMode")
    public void editMode() {
        if (!editMode) setEditMode(true);
        else setEditMode(false);
    }
    // Setters

    public void setSelectedImage(ImageEntity selectedImage) {
        this.selectedImage = selectedImage;
    }

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

    private void setTagList(List<TagEntity> tagList) {
        this.tagList = tagList;
    }

    public void setEditMode(boolean editMode) { this.editMode = editMode; }


    // Getters

    public ImageEntity getSelectedImage() {
        return selectedImage;
    }

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

    public List<TagEntity> getTagList() {
        return tagList;
    }

    public Boolean getEditMode() {
        return editMode;
    }
}
