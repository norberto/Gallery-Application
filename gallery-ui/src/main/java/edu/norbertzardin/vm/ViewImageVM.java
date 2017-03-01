package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import java.util.List;

public class ViewImageVM {
    private ImageEntity selectedImage;
    private List<CatalogueEntity> catalogueList;
    private List<TagEntity> tagList;
    private boolean editMode;
    private String tags = "fwafwafwafawfawfwa";

    @WireVariable
    private TagService tagService;

    @WireVariable
    private CatalogueService catalogueService;

    @WireVariable
    private ImageService imageService;

    @Init
    public void init (@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        setEditMode(false);
    }


    @AfterCompose
    public void afterCompose (@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }

    @Command
    @NotifyChange({"selectedImage","catalogueList","tagList","editMode"})
    public void viewImage(@BindingParam("selectedImage") ImageEntity image) {
        if(image != null) {
            setSelectedImage(imageService.getImageByIdFullFetch(image.getId()));
        }
        if(getSelectedImage() != null) {
            loadTags();
        }
        setEditMode(false);
    }

    @Command
    public void loadTags() {
        if(selectedImage != null) {
            setTagList(selectedImage.getTags());
        }
    }

    @Command
    @NotifyChange({"tagList", "selectedImage"})
    public void deleteTag(@BindingParam("selectedTag") TagEntity tag) {
        if(tag != null) {
            TagEntity tag_ = tagService.getTagById(tag.getId());
            if(tag_ != null) {
                tagService.removeTag(tag_);
                setSelectedImage(imageService.getImageByIdWithFetch(selectedImage.getId()));
                setTagList(selectedImage.getTags());
            }
        }

    }

    @Command
    public void deleteImage() {
        imageService.deleteImage(selectedImage);
    }

    @Command
    @NotifyChange({"selectedImage", "editMode", "tagList", "tags"})
    public void editImage() {
        if(selectedImage != null) {
            String[] parsed_tags = ImageUtil.parseTags(tags);
            for(String tag : parsed_tags) {
                tagService.createTag(tag, selectedImage);
            }
            imageService.editImage(selectedImage);
            setSelectedImage(imageService.getImageByIdWithFetch(selectedImage.getId()));
            loadTags();
//            setTags(null);
        }
        setEditMode(false);
    }

    @Command
    @NotifyChange("editMode")
    public void editMode() {
        if (!editMode) {
            setEditMode(true);
        } else {
            setEditMode(false);
        }
    }

    @Command
    public void onDownload() {
        if(selectedImage != null) {
            setSelectedImage(imageService.getImageByIdFullFetch(selectedImage.getId()));
        }
        if(selectedImage != null) {
            ImageUtil.download(selectedImage);
        }

    }

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    public void setCatalogueService(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public ImageEntity getSelectedImage() {
        return selectedImage;
    }


    public List<CatalogueEntity> getCatalogueList() {
        return catalogueList;
    }

    public void setEditMode(boolean editMode) { this.editMode = editMode; }

    public Boolean getEditMode() {
        return editMode;
    }

    public void setSelectedImage(ImageEntity selectedImage) {
        this.selectedImage = selectedImage;
    }

    public void setCatalogueList(List<CatalogueEntity> catalogueList) {
        this.catalogueList = catalogueList;
    }

    public void setTagList(List<TagEntity> tagList) {
        this.tagList = tagList;
    }

    public List<TagEntity> getTagList() {
        return tagList;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return this.tags;
    }
}
