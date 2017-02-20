package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;

import java.util.List;

public class ImageListVM {

    private String searchString;

    @Wire("#selectedImage")
    private ImageEntity selectedImage;

    @WireVariable
    private ImageService imageService;

    private List<ImageEntity> imageList;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        imageList = getImages();
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        imageList = getImages();
    }

    @NotifyChange("imageList")
    @Command
    public void doSearch() {
        if((getSearchString() != null) && !getSearchString().equals("")) {
            imageList = imageService.findImagesByName(getSearchString());

        }
    }

    public ImageListVM() {}

    public void setImageList(List<ImageEntity> list){ this.imageList = list; }

    public List<ImageEntity> getImageList(){ return imageList; }

    @Command
    @NotifyChange("selectedImage")
    public void viewImage(@BindingParam("selectedImage") ImageEntity  image){
        setSelectedImage(image);
        String openModal = "$('#myModal').modal('show')";
        Clients.evalJavaScript(openModal);
    }

    public List<ImageEntity> getImages(){
        return imageService.getImageList();
    }

    public void setImageService(ImageService imageService){
        this.imageService = imageService;
    }

    public ImageService getImageService(){ return imageService; }

    public ImageEntity getSelectedImage(){
        return this.selectedImage;
    }

    public void setSelectedImage(ImageEntity image) {
        this.selectedImage = image;
    }

    @Command
    @NotifyChange("selectedImage")
    public void deleteImage(@BindingParam("deleteID") int id){
        imageService.deleteImage(id);
        selectedImage = null;
        Executions.getCurrent().sendRedirect("/view.zul");
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
