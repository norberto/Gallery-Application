package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ByteData;
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
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import java.util.Date;
import java.util.List;

public class UploadVM {

    private final String defaultCatalogueName = "Non-categorized";

    private CatalogueEntity defaultCatalogue;
    private String filter;
    private String name;
    private String description;
    private String tags;
    private String datatype;
    private byte[] thumbnail;
    private byte[] mediumSize;
    private byte[] download;
    private Boolean uploaded;
    private CatalogueEntity selectedCatalogue;

    private List<CatalogueEntity> catalogueList;

    @WireVariable
    private ImageService imageService;

    @WireVariable
    private TagService tagService;

    @WireVariable
    private CatalogueService catalogueService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
    }

    public UploadVM() {}

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        catalogueList = catalogueService.getCatalogueList();
        defaultCatalogue = catalogueService.getCatalogueByNameNoFetch(defaultCatalogueName);
        setSelectedCatalogue(defaultCatalogue);
        setUploaded(false);
    }

    @Command
    public void tags(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
        System.out.println(event.getData());
    }

    @Command
    public void submit(){
        ImageEntity ie = new ImageEntity();
        ie.setName(name);
        ie.setDescription(description);
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
        ie.setCreatedDate(new Date());

        imageService.createImage(ie);

        String[] tagList = ImageUtil.parseTags(tags);
        for(String tag : tagList){
            tagService.createTag(tag, ie);
        }

        Executions.getCurrent().sendRedirect("/view.zul");
    }
    @Command
    @NotifyChange({"thumbnail", "uploaded"})
    public void onUpload(@BindingParam("upEvent") UploadEvent event){

        Media media = event.getMedia();
        if (media instanceof Image) {
            Image img = (Image) media;
            setDownload(img.getByteData());
            setMediumSize(ImageUtil.scaleImageToSize(img, 500));
            setThumbnail(ImageUtil.scaleImageToSize(img, 200));
            setDatatype(img.getFormat());
            setUploaded(true);
        } else {
            Messagebox.show("Not an image: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @NotifyChange("selectedCatalogue")
    @Command
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        this.selectedCatalogue = ce;
    }

    @Command
    @NotifyChange("catalogueList")
    public void filter() {
        System.out.println(getFilter());
        setCatalogueList(catalogueService.getCatalogueListByKey(getFilter()));
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

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public Boolean getUploaded() {
        return uploaded;
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getFilter() {
        return filter;
    }


    @Command
    public void setFilter(String filter) {
        this.filter = filter;
    }
}
