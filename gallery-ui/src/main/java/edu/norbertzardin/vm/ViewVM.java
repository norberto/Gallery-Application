package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

public class ViewVM {
    private Integer pageMax;
    private String searchString;
    private TagEntity searchTag;

    private Integer page;
    private Integer pageCount;

    @Wire("#selectedImage")
    private ImageEntity selectedImage;

    @WireVariable
    private ImageService imageService;

    @WireVariable
    private TagService tagService;

    private List<ImageEntity> imageList;

    @Init
    public void init(@BindingParam("pageMax") Integer max) {
        setPage(1);
        setPageMax(max);
        updatePageCount();
        loadImages();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange({"imageList", "page", "pageCount", "searchString"})
    public void doSearch() {
        setPage(1);
        setSearchString(searchString.replaceAll("%", ""));
        if (isSearch()) {
            searchTag = tagService.load(searchString, true);
            setImageList(imageService.find(getSearchString(), searchTag, getPage(), pageMax));
        } else {
            setImageList(imageService.loadImages(getPage(), pageMax));
        }
        updatePageCount();
    }

    @GlobalCommand
    @NotifyChange({"imageList", "page", "pageCount"})
    public void reload () {
        if(imageList.isEmpty() || page < 0 || page > pageCount) {
            updatePageCount();
            setPage(getPageCount());
        }
        loadImages();
    }

    @Command
    @NotifyChange({"selectedImage", "tagList"})
    public void viewImage(@BindingParam("selectedImage") ImageEntity image) {
        setSelectedImage(imageService.loadMedium(image.getId()));
    }

    @Command
    @NotifyChange({"page", "imageList"})
    public void previousPage() {
        if (page != 1) {
            page--;
            updatePageContent();
        }
    }

    @Command
    @NotifyChange({"page", "imageList", "pageCount"})
    public void nextPage() {
        if (!page.equals(pageCount)) {
            page++;
        } else if(page >= pageCount) {
            setPage(getPageCount());
        }
        updatePageContent();
    }

    public boolean isSearch() { return searchString != null && !searchString.equals(""); }

    private void updatePageCount() {
        Integer imageCount;
        if(isSearch()) {
            imageCount = imageService.count(getSearchString(), searchTag).intValue();
        } else {
            imageCount = imageService.count(null, null).intValue();
        }

        Integer count = imageCount / pageMax;
        if(count * pageMax < imageCount) {
            setPageCount(count + 1);
        } else {
            setPageCount(count);
        }
    }

    private void updatePageContent() {
        if(!isSearch()){
            loadImages();
        } else {
            setImageList(imageService.find(searchString, searchTag, page, pageMax));
        }
        updatePageCount();
    }

    private void loadImages() {
        setImageList(imageService.loadImages(getPage(), pageMax));
    }

    public void setImageList(List<ImageEntity> list) {
        this.imageList = list;
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

    public List<ImageEntity> getImageList() {
        return imageList;
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

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageMax(Integer pageMax) {
        this.pageMax = pageMax;
    }
}