package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.form.UploadForm;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

public class ImageVM {
    private String name;
    private String description;

    private ImageEntity selectedImage;
    private List<CatalogueEntity> catalogueList;
    private List<TagEntity> tagList;
    private Boolean editMode;
    private String tags;
    private Boolean removeConfirmation;
    private Integer tagsLeft;
    private Integer tagLimit;
    private boolean changed;


    private UploadForm editForm;

    @WireVariable
    private TagService tagService;

    @WireVariable
    private ImageService imageService;
    private ValidationMessages validationMessages;

    @Init
    public void init(@BindingParam("tagLimit") Integer limit) {
        setTagLimit(limit);
        setRemoveConfirmation(false);
        setEditMode(false);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ContextParam(ContextType.BINDER) Binder binder) {
        Selectors.wireComponents(view, this, false);
        validationMessages = ((BinderCtrl) binder).getValidationMessages();

    }

    @GlobalCommand
    @NotifyChange({"selectedImage", "tagList", "editMode", "editForm", "removeConfirmation", "tagsLeft"})
    public void viewImage(@BindingParam("selectedImage") ImageEntity image) {
        clearMessages();
        // If selected image is not null - fetch image with all contents
        if (image != null) {
            setSelectedImage(imageService.loadMedium(image.getId()));
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
            }
            // Update local content
            setSelectedImage(imageService.loadMedium(selectedImage.getId()));
            setTagList(selectedImage.getTags());
        }

    }

    @Command
    @NotifyChange({"removeConfirmation"})
    public void deleteImage() {
        setRemoveConfirmation(false);
        imageService.remove(selectedImage);
    }

    @Command
    @NotifyChange({"selectedImage", "editMode", "tagList", "tags", "tagsLeft"})
    public void editImage() {
        selectedImage.setName(editForm.getName());
        selectedImage.setDescription(editForm.getDescription());
        // If selected image is not null process data
        if (selectedImage != null) {
            // Parse tags
            String[] parsed_tags = ImageUtil.parseTags(getTags());
            // Lookup and updatePageContent OR create new tags and add them to image
            for (String tag : parsed_tags) {
                if(tagService.createTag(tag, selectedImage)) {
                    setTagsLeft(getTagsLeft() - 1);
                }
            }
            // Update image
            imageService.update(selectedImage);
            // Update local content
            setSelectedImage(imageService.loadMedium(selectedImage.getId()));
            loadTags();
        }
        // Disable edit mode
        setEditMode(false);
    }

    @Command
    @NotifyChange("editMode")
    public void editMode() {
        clearMessages();
        setEditMode(!editMode);
    }

    private void clearMessages() {
        validationMessages.clearAllMessages();
    }

    @Command
    public void onDownload() {
        if (selectedImage != null) {
            ByteData data = imageService.download(selectedImage.getId());
            // Check if fetched image is not null, if not, start downloading
            if (data != null) {
                ImageUtil.download(data, selectedImage);
            }
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

    public void setEditMode(Boolean editMode) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
