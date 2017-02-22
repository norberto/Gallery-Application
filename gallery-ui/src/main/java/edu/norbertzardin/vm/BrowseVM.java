package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BrowseVM {
    // Properties
    private String name;

    private List<CatalogueEntity> catalogueList;
    private List<ImageEntity> imageList;

    private CatalogueEntity selectedCatalogue;
    private ImageEntity selectedImage;
    private CatalogueEntity editCatalogue;
    private boolean backButton;


    private CatalogueEntity defaultCatalogue;

    @WireVariable
    private CatalogueService catalogueService;

    @WireVariable
    private ImageService imageService;


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
    @NotifyChange("catalogueList")
    public void createCatalogue() {
        CatalogueEntity ce = new CatalogueEntity();
        ce.setTitle(name);
        ce.setCreatedDate(new Date());
        catalogueService.createCatalogue(ce);
        catalogueList = catalogueService.getCatalogueList();
    }

    @Command
    @NotifyChange("selectedImage")
    public void viewImage(@BindingParam("selectedImage") ImageEntity image) {
        setSelectedImage(image);
        String openModal = "$('#viewImageModal').modal('show')";
        Clients.evalJavaScript(openModal);
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
        if (selectedCatalogue != null) {
            setImageList(selectedCatalogue.getImages());
        }
    }

    public List<ImageEntity> getImageList() {
        return imageList;
    }


    @Command
    @NotifyChange({"selectedCatalogue", "catalogueList", "backButton"})
    public void deleteCatalogue() {
        if (selectedCatalogue == editCatalogue) {
            setSelectedCatalogue(defaultCatalogue);
            loadImages();
            setImageList(selectedCatalogue.getImages());
        }
        catalogueService.deleteCatalogue(editCatalogue);
        setCatalogueList(catalogueService.getCatalogueList());
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

    public void setBackButton(boolean backButton) {
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

    public ImageEntity getSelectedImage() {
        return selectedImage;
    }

    public CatalogueEntity getEditCatalogue() {
        return editCatalogue;
    }

    public boolean getBackButton() {
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

    public String getName() {
        return this.name;
    }


}
