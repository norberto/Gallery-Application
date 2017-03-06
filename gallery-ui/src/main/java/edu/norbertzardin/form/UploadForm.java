package edu.norbertzardin.form;

public class UploadForm {

    public UploadForm(){}

    public UploadForm(String name, String description) {
        setName(name);
        setDescription(description);
    }

    private String name;
    private String description;
    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
