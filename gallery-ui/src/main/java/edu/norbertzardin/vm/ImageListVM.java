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
    private final Integer pageMax = 5;
    private String searchString;

    private Integer page;
    private Integer pageCount;

    @Wire("#selectedImage")
    private ImageEntity selectedImage;

    @WireVariable
    private ImageService imageService;

    @WireVariable
    private TagService tagService;

    private List<ImageEntity> imageList;

    @AfterCompose
    @NotifyChange({"page", "pageCount"})
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        setPage(1);
        setPageCount(imageService.getPageCount(pageMax));
        loadImages();
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        setPage(1);
        setPageCount(imageService.getPageCount(pageMax));
        loadImages();
    }

    @NotifyChange("imageList")
    @Command
    public void doSearch(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
        if ((getSearchString() != null) && !getSearchString().equals("")) {
            TagEntity tag = tagService.getTagByName(searchString);
            setImageList(imageService.findImagesByKeys(getSearchString(), tag));
        } else {
            setImageList(imageService.getImageList(1, pageMax));
        }
    }

    @GlobalCommand
    @NotifyChange({"imageList", "page"})
    public void reload () {
        if(page < 0 || page > pageCount) {
            page = 1;
        }
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

    @Command
    @NotifyChange({"page", "imageList"})
    public void previousPage() {
        if (page != 1) {
            page--;
            loadImages();
        }
    }

    @Command
    @NotifyChange({"page", "imageList"})
    public void nextPage() {
        if (!page.equals(pageCount)) {
            page++;
            loadImages();
        }
    }

    private void loadImages() {
        setImageList(imageService.getImageList(getPage(), pageMax));
    }

    public void setImageList(List<ImageEntity> list) {
        this.imageList = list;
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

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount.intValue();
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}