package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.form.UploadForm;
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
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

public class UploadVM {
    private final String defaultCatalogueName = "Non-categorized";

    private UploadForm uploadForm;
    private CatalogueEntity defaultCatalogue;
    private String filter;
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
    private boolean allowSubmit;
    private String errorMessage;
    private byte[] imagePlaceholder;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        setAllowSubmit(false);
        try {
            File f = new File("META-INF/resources/images/noimage.gif");
            imagePlaceholder = Files.readAllBytes(f.toPath());
        } catch (IOException e) {
            System.err.println("No image placeholder found.");
        }
        setThumbnail(imagePlaceholder);
        Selectors.wireComponents(view, this, false);
        setCatalogueList(catalogueService.getCatalogueList());
        setDefaultCatalogue(catalogueService.getCatalogueByNameNoFetch(defaultCatalogueName));
        setUploadForm(new UploadForm());
        setSelectedCatalogue(defaultCatalogue);
    }

    @Command
    public void tags(@ContextParam(ContextType.TRIGGER_EVENT) Event event) {
        System.out.println(event.getData());
    }

    @Command
    public void submit() {
        ImageEntity ie = new ImageEntity();
        ie.setName(uploadForm.getName());
        ie.setDescription(uploadForm.getDescription());
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

        String[] tagList = ImageUtil.parseTags(uploadForm.getTags());
        for (String tag : tagList) {
            tagService.createTag(tag, ie);
        }

        Executions.getCurrent().sendRedirect("/view.zul");
    }

    @Command
    @NotifyChange({"thumbnail", "uploaded", "allowSubmit", "errorMessage"})
    public void onUpload(@BindingParam("upEvent") UploadEvent event) {
        Media media = event.getMedia();
        InputStream is = new BufferedInputStream(new ByteArrayInputStream(media.getByteData()));
        try {
            String mimeType = URLConnection.guessContentTypeFromStream(is);
            if (mimeType != null && media instanceof Image) {
                Image img = (Image) media;
                setDownload(img.getByteData());
                setMediumSize(ImageUtil.scaleImageToSize(img, 500));
                setThumbnail(ImageUtil.scaleImageToSize(img, 200));
                setDatatype(img.getFormat());
                setAllowSubmit(true);
                errorMessage = null;
            } else {
                errorMessage = "Selected item is not an image.";
                setAllowSubmit(false);
                setThumbnail(imagePlaceholder);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        setCatalogueList(catalogueService.getCatalogueListByKey(getFilter()));
    }

    public ImageService getImageService() {
        return imageService;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
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

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getFilter() {
        return filter;
    }


    @Command
    public void setFilter(String filter) {
        this.filter = filter;
    }

    public UploadForm getUploadForm() {
        return uploadForm;
    }

    public void setUploadForm(UploadForm uploadForm) {
        this.uploadForm = uploadForm;
    }

    public boolean getAllowSubmit() {
        return this.allowSubmit;
    }

    public void setAllowSubmit(boolean allowSubmit) {
        this.allowSubmit = allowSubmit;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setDefaultCatalogue(CatalogueEntity catalogue) {
        this.defaultCatalogue = catalogue;
    }
}
