package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.ImageService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class ViewImageVM {

    @WireVariable
    private ImageService imageService;

    private ImageEntity selectedImage;
    private boolean editMode;


    private int id;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @QueryParam("id") Integer id) {
        if(id == null) System.out.println("ID IS NULL");

//        Selectors.wireComponents(view, this, false);
//        setSelectedImage(imageService.getImageById(Integer.getInteger(id)));
//        setEditMode(false);
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view, @QueryParam("id") Long id) {
        Selectors.wireComponents(view, this, false);

        setSelectedImage(imageService.getImageById(id));
        setEditMode(false);
    }


    public ImageEntity getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(ImageEntity selectedImage) {
        this.selectedImage = selectedImage;
    }

    @Command
    @NotifyChange("editMode")
    public void editMode() {
        if(!editMode) setEditMode(true);
        else setEditMode(false);
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public ImageService getImageService() {
        return imageService;
    }
}
