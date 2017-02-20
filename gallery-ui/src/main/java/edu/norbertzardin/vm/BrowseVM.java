package edu.norbertzardin.vm;

import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.service.CatalogueService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

public class BrowseVM {

    private String name;
    private List<CatalogueEntity> catalogueList;


//    @Wire("#selectedCatalogue")
    private CatalogueEntity selectedCatalogue;

    @WireVariable
    private CatalogueService catalogueService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        catalogueList = catalogueService.getCatalogueList();
    }

    @Init
    public void inie(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    public BrowseVM() {}

    @Command
    public void createCatalogue() {
        CatalogueEntity ce = new CatalogueEntity();
        ce.setTitle(name);
        catalogueService.createCatalogue(ce);
    }

    @NotifyChange("selectedCatalogue")
    @Command
    public void selectCatalogue(@BindingParam("selectedCatalogue") CatalogueEntity ce) {
        this.selectedCatalogue = ce;
    }


    public void setImageService(CatalogueService catalogueService){
        this.catalogueService = catalogueService;
    }
    public CatalogueService getImageService(){ return catalogueService; }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() { return this.name; }

    public CatalogueEntity getSelectedCatalogue() {
        return selectedCatalogue;
    }

    public void setSelectedCatalogue(CatalogueEntity selectedCatalogue) {
        this.selectedCatalogue = selectedCatalogue;
    }

    public List<CatalogueEntity> getCatalogueList() {
        System.out.println("Getting");
        return catalogueList;
    }

    public void setCatalogueList(List<CatalogueEntity> catalogueList) {
        this.catalogueList = catalogueList;
    }

    @Command
    public void editCatalogue() {
        System.out.println("editing");
        catalogueService.editCatalogue(selectedCatalogue);
    }

}
