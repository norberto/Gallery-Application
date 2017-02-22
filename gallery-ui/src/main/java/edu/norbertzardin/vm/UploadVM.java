package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.Date;
import java.util.List;

public class UploadVM {

    private final String defaultCatalogueName = "Non-categorized";

    private CatalogueEntity defaultCatalogue;

    private String name;
    private String description;
    private String tags;
    private byte[] imageData;
    private CatalogueEntity selectedCatalogue;

    private List<CatalogueEntity> catalogueList;

    @Wire("#image")
    org.zkoss.zul.Image image;

    @WireVariable
    private ImageService imageService;

    @WireVariable
    private CatalogueService catalogueService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
    }

    public UploadVM() {}


    @Init
    public void init(){
        catalogueList = catalogueService.getCatalogueList();
        defaultCatalogue = catalogueService.getCatalogueByName(defaultCatalogueName);
        setSelectedCatalogue(defaultCatalogue);
    }

    @Command
    public void createImage(){
        ImageEntity ie = new ImageEntity();
        ie.setName(name);
        ie.setDescription(description);
        ie.setImageData(imageData);
        ie.setCreatedDate(new Date());
        ie.setCatalogue(selectedCatalogue);
        imageService.createImage(ie);

        String[] tagList = parseTags(tags);
        for(String tag : tagList){
            TagEntity te = new TagEntity();
            te.setName(tag);
            te.setImage(ie);
            imageService.createTag(te);
        }

        Executions.getCurrent().sendRedirect("/view.zul");
    }
    @Command("onUpload")
    public void onUpload(@BindingParam("upEvent") UploadEvent event){
        Media media = event.getMedia();
        if (media instanceof Image) {
            Image img = (Image) media;
            image.setContent(img);
            double scale = ImageUtil.getScalingRatio((AImage) img, 200.0);
            image.setHeight((img.getHeight() * scale) + "px");
            image.setWidth((img.getWidth() * scale) + "px");
            setImageData(media.getByteData());

        } else {
            Messagebox.show("Not an image: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @NotifyChange("selectedCatalogue")
    @Command
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        this.selectedCatalogue = ce;
    }


    public String[] parseTags(String tags){
        String[] tagList = tags.split(",");
        return tagList;
    }

    public void setImageService(ImageService imageService){
        this.imageService = imageService;
    }

    public ImageService getImageService(){ return imageService; }

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public CatalogueEntity getSelectedCatalogue() {
        return selectedCatalogue;
    }

    public void setSelectedCatalogue(CatalogueEntity selectedCatalogue) {
        this.selectedCatalogue = selectedCatalogue;
    }

    public List<CatalogueEntity> getCatalogueList() {
        return catalogueList;
    }

    public void setCatalogueList(List<CatalogueEntity> catalogueList) {
        this.catalogueList = catalogueList;
    }

    public void setCatalogueService(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }
}
