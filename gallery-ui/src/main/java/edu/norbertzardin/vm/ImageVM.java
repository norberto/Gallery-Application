package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.form.UploadForm;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import edu.norbertzardin.util.ImageUtil;
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

import java.util.List;

public class ImageVM {
    private ImageEntity selectedImage;
    private List<CatalogueEntity> catalogueList;
    private List<TagEntity> tagList;
    private boolean editMode;
    private String tags;
    private Boolean removeConfirmation;
    private Integer tagsLeft;
    private Integer tagLimit;


    private UploadForm editForm;

    @WireVariable
    private TagService tagService;

    @WireVariable
    private ImageService imageService;

    @Init
    public void init(@BindingParam("tagLimit") Integer limit) {
        setTagLimit(limit);
        setRemoveConfirmation(false);
        setEditMode(false);
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @GlobalCommand
    @NotifyChange({"selectedImage", "tagList", "editMode", "editForm", "removeConfirmation", "tagsLeft"})
    public void viewImage(@BindingParam("selectedImage") ImageEntity image) {
        // If selected image is not null - fetch image with all contents
        if (image != null) {
            setSelectedImage(imageService.getImageByIdFullFetch(image.getId()));
            editForm = new UploadForm(selectedImage.getName(), selectedImage.getDescription());
            setTagsLeft(getTagLimit() - selectedImage.getTags().size());
        }
        // If fetched image is not null, load its tags to local contents
        if (getSelectedImage() != null) {
            loadTags();
        }
        // Disable edit mode on modal open
        setEditMode(false);
        setRemoveConfirmation(false);
    }

    @Command
    public void loadTags() {
        // If selected image is not null - fetch tags
        if (selectedImage != null) {
            setTagList(selectedImage.getTags());
        }
    }

    @Command
    @NotifyChange({"tagList", "selectedImage"})
    public void deleteTag(@BindingParam("selectedTag") TagEntity tag) {
        // If selected tag is not null delete it
        if (tag != null) {
            // Look up the tag
            TagEntity tag_ = tagService.getTagById(tag.getId());
            // If tag found start deleting process
            if (tag_ != null) {
                tagService.removeTag(tag_);
                // Update local content
                setSelectedImage(imageService.getImageByIdWithFetch(selectedImage.getId()));
                setTagList(selectedImage.getTags());
            }
        }

    }

    @Command
    @NotifyChange({"removeConfirmation"})
    public void deleteImage() {
        setRemoveConfirmation(false);
        imageService.deleteImage(selectedImage);
    }

    @Command
    @NotifyChange({"selectedImage", "editMode", "tagList", "tags", "tagsLeft"})
    public void editImage() {
        selectedImage.setName(editForm.getName());
        selectedImage.setDescription(editForm.getDescription());
        // If selected image is not null process data
        if (selectedImage != null) {
            // Parse tags
            String[] parsed_tags = ImageUtil.parseTags(editForm.getTags());
            // Lookup and updatePageContent OR create new tags and add them to image
            for (String tag : parsed_tags) {
                tagService.createTag(tag, selectedImage);
            }
            setTagsLeft(getTagsLeft() - parsed_tags.length);
            // Update image
            imageService.editImage(selectedImage);
            // Update local content
            setSelectedImage(imageService.getImageByIdWithFetch(selectedImage.getId()));
            loadTags();
            setTags(null);
        }
        // Disable edit mode
        setEditMode(false);
    }

    @Command
    @NotifyChange("editMode")
    public void editMode() {
        setEditMode(!editMode);
    }

    @Command
    public void onDownload() {
        // Check if currently selected image is not null, if not - fetch image again with its contents
        if (selectedImage != null) {
            setSelectedImage(imageService.getImageByIdFullFetch(selectedImage.getId()));
        }
        // Check if fetched image is not null, if not, start downloading
        if (selectedImage != null) {
            ImageUtil.download(selectedImage);
        }
    }

    @Command
    @NotifyChange({"removeConfirmation"})
    public void changeConfirmationState(@BindingParam("confirm") Boolean state) {
        setRemoveConfirmation(state);
    }

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public ImageEntity getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(ImageEntity selectedImage) {
        this.selectedImage = selectedImage;
    }

    public List<CatalogueEntity> getCatalogueList() {
        return catalogueList;
    }

    public void setCatalogueList(List<CatalogueEntity> catalogueList) {
        this.catalogueList = catalogueList;
    }

    public Boolean getEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public List<TagEntity> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagEntity> tagList) {
        this.tagList = tagList;
    }

    public UploadForm getEditForm() {
        return editForm;
    }

    public void setEditForm(UploadForm editForm) {
        this.editForm = editForm;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getRemoveConfirmation() {
        return removeConfirmation;
    }

    public void setRemoveConfirmation(Boolean removeConfirmation) {
        this.removeConfirmation = removeConfirmation;
    }

    public Integer getTagsLeft() {
        return tagsLeft;
    }

    public void setTagsLeft(Integer tagsLeft) {
        this.tagsLeft = tagsLeft;
    }

    public Integer getTagLimit() {
        return tagLimit;
    }

    public void setTagLimit(Integer tagLimit) {
        this.tagLimit = tagLimit;
    }
}
