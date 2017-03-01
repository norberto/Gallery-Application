package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

public class ImageListVM {

    private String searchString;

    private Integer page;
    private Integer pageCount;
    private List<Integer> pageLabels;

    @Wire("#selectedImage")
    private ImageEntity selectedImage;

    @WireVariable
    private ImageService imageService;

    @WireVariable
    private TagService tagService;

    private List<ImageEntity> imageList;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        loadImages();
    }

    @Init
    public void init(@QueryParam("page") Integer page) {
        if (page != null) {
            this.page = page;
        } else {
            this.page = 1;
        }
        pageCount = imageService.getPageCount(15);
        pageLabels = new ArrayList<Integer>();
        for (int i = 1; i <= pageCount; i++) {
            pageLabels.add(i);
        }
    }

    @NotifyChange("imageList")
    @Command
    public void doSearch(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
        if ((getSearchString() != null) && !getSearchString().equals("")) {
            TagEntity tag = tagService.getTagByName(searchString);
            setImageList(imageService.findImagesByKeys(getSearchString(), tag));
        } else {
            setImageList(imageService.getImageList(1, 15));
        }
    }

    public void setImageList(List<ImageEntity> list) {
        this.imageList = list;
    }

    @GlobalCommand
    @NotifyChange("imageList")
    public void reload () {
        loadImages();
    }

    public List<ImageEntity> getImageList() {
        return imageList;
    }

    @Command
    @NotifyChange({"selectedImage", "tagList"})
    public void viewImage(@BindingParam("selectedImage") ImageEntity image) {
        setSelectedImage(imageService.getImageByIdWithFetch(image.getId()));
    }

    private void loadImages() {
        imageList = imageService.getImageList(this.page, 15);
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public ImageService getImageService() {
        return imageService;
    }

    public ImageEntity getSelectedImage() {
        return this.selectedImage;
    }

    private void setSelectedImage(ImageEntity image) {
        this.selectedImage = image;
    }


    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPage() {
        return this.page;
    }

    public List<Integer> getPageLabels() {
        return pageLabels;
    }

    public void setPageLabels(List<Integer> pageLabels) {
        this.pageLabels = pageLabels;
    }

    @Command
    public void goToPage(@BindingParam("page") Integer p) {
        this.page = p;
        changePage();
    }

    @Command
    public void previousPage() {
        if (page != 1) {
            page--;
        }
    }

    @Command
    public void nextPage() {
        if (!page.equals(pageCount)) {
            page++;
            changePage();
        }
    }


    public void changePage() {
        Executions.getCurrent().sendRedirect("/view.zul?page=" + this.page);
    }
}