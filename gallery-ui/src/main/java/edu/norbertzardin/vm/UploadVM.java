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
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

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
    private Integer page;
    private Integer pageCount;
    private Integer pageMax;


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
    public void init(@BindingParam("pageMax") Integer max) {
        setAllowSubmit(false);
        setPage(1);
        setPageMax(max);
        updatePageCount();
        loadImagePlaceholder();
        setThumbnail(imagePlaceholder);
        updateCataloguesList();
        setDefaultCatalogue(catalogueService.loadNoFetch(defaultCatalogueName));
        setUploadForm(new UploadForm());
        setSelectedCatalogue(defaultCatalogue);
    }

    private void loadImagePlaceholder() {
        try {
            File f = new File("META-INF/resources/images/noimage.gif");
            imagePlaceholder = Files.readAllBytes(f.toPath());
        } catch (IOException e) {
            System.err.println("No image placeholder found.");
        }
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

        imageService.create(ie);

        String[] tagList = ImageUtil.parseTags(uploadForm.getTags());
        for (String tag : tagList) {
            tagService.create(tag, ie);
        }

        Executions.getCurrent().sendRedirect("/view.zul");
    }

    @Command
    @NotifyChange({"thumbnail", "uploaded", "allowSubmit", "errorMessage"})
    public void onUpload(@BindingParam("upEvent") UploadEvent event) throws IOException{
//    public void onUpload$fileUpload(UploadEvent event) throws IOException {
        int fileSizeBytes = event.getMedia().getStreamData().available();

        if (fileSizeBytes <= 5120) {
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
        } else {
            Messagebox.show("WTF" + fileSizeBytes);
        }
    }

    @NotifyChange("selectedCatalogue")
    @Command
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        this.selectedCatalogue = ce;
    }

    @Command
    @NotifyChange({"catalogueList", "pageCount", "page"})
    public void filter() {
        setCatalogueList(catalogueService.loadByPage(getPage(), getPageMax(), getFilter(), true));
        setPageCount(catalogueService.count(getPageMax(), getFilter(), false));
        setPage(1);
    }

    @Command
    @NotifyChange({"page", "catalogueList"})
    public void previousPage() {
        if (page != 1) {
            page--;
            updatePageCount();
            updateCataloguesList();
        }
    }

    @Command
    @NotifyChange({"page", "catalogueList", "pageCount"})
    public void nextPage() {
        updatePageCount();
        if (!page.equals(pageCount)) {
            page++;
        } else if (page >= pageCount) {
            setPage(getPageCount());
        }
        updateCataloguesList();
    }


    private void updatePageCount() {
        setPageCount(catalogueService.count(getPageMax(), getFilter(), true));
    }

    public boolean isSearch() {
        return filter != null;
    }

    private void updateCataloguesList() {
        setCatalogueList(catalogueService.loadByPage(getPage(), getPageMax(), getFilter(), true));
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

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageMax() {
        return pageMax;
    }

    public void setPageMax(Integer pageMax) {
        this.pageMax = pageMax;
    }
}
