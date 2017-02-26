package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.DataService;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
import edu.norbertzardin.util.ImageUtil;
import org.hibernate.type.ByteType;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.*;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadVM {

    private final String defaultCatalogueName = "Non-categorized";

    private CatalogueEntity defaultCatalogue;

    private String name;
    private String description;
    private String tags;
    private String datatype;
    private byte[] thumbnail;
    private byte[] mediumSize;
    private byte[] download;
    private CatalogueEntity selectedCatalogue;

    private List<CatalogueEntity> catalogueList;

    @WireVariable
    private ImageService imageService;

    @WireVariable
    private TagService tagService;

    @WireVariable
    private CatalogueService catalogueService;

    @WireVariable
    private DataService dataService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
    }

    public UploadVM() {}


    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        catalogueList = catalogueService.getCatalogueList();
        defaultCatalogue = catalogueService.getCatalogueByName(defaultCatalogueName);
        setSelectedCatalogue(defaultCatalogue);
    }

    @Command
    public void createImage(){
        ImageEntity ie = new ImageEntity();
        ie.setName(name);
        ie.setDescription(description);
        ie.setCreatedDate(new Date());
        ie.setCatalogue(selectedCatalogue);

        ByteData thumbnail_ = new ByteData();
        ByteData mediumImage_ = new ByteData();
        ByteData download_ = new ByteData();

        thumbnail_.setData(thumbnail);
        mediumImage_.setData(mediumSize);
        download_.setData(download);

        ie.setThumbnail(thumbnail_);
        ie.setMediumImage(mediumImage_);
        ie.setDownload(download_);
        ie.setDatatype(datatype);

        imageService.createImage(ie);

        String[] tagList = parseTags(tags);
        for(String tag : tagList){
            TagEntity tag_ = tagService.getTagByName(tag);
            if(tag_ == null){
                TagEntity te = new TagEntity();
                te.setName(tag);
                te.addImage(ie);
                te.setCreatedDate(new Date());
                imageService.createTag(te);
            } else {
                tag_.addImage(ie);
                tagService.updateTag(tag_);
            }
        }

        Executions.getCurrent().sendRedirect("/view.zul");
    }
    @Command("onUpload")
    @NotifyChange("thumbnail")
    public void onUpload(@BindingParam("upEvent") UploadEvent event){
        Media media = event.getMedia();
        if (media instanceof Image) {
            Image img = (Image) media;
            setDownload(img.getByteData());
            setMediumSize(ImageUtil.scaleImageToSize(img, 650));
            setThumbnail(ImageUtil.scaleImageToSize(img, 200));
            setDatatype(img.getFormat());
        } else {
            Messagebox.show("Not an image: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @NotifyChange("selectedCatalogue")
    @Command
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        this.selectedCatalogue = ce;
    }


    private String[] parseTags(String tags) {
        if(tags == null) return new String[0];
        String[] tagList = tags.split(",");
        for(int i = 0; i < tagList.length; i++) {
            if(tagList[i].charAt(0) == ' ') {
                tagList[i] = tagList[i].substring(1, tagList[i].length());
            }
        }
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

    public TagService getTagService() {
        return tagService;
    }

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    public void setThumbnail(byte[] thumbnail) { this.thumbnail = thumbnail; }

    public byte[] getThumbnail () {
        return thumbnail;
    }

    public void setMediumSize(byte[] mediumSize) {
        this.mediumSize = mediumSize;
    }

    public void setDownload(byte[] download) {
        this.download = download;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
}
