package de.saxsys.hypermedia.jsf;

import static de.saxsys.hypermedia.jsf.HalUtil.replaceParam;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.theoryinpractise.halbuilder.api.ContentRepresentation;
import com.theoryinpractise.halbuilder.api.Link;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.jaxrs.JaxRsHalBuilderReaderSupport;

@Named("library")
@ViewScoped
public class LibraryBean implements Serializable {

    private static final String HAL_JSON = "application/hal+json";
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryBean.class);

    private String query;
    private List<Book> books = new ArrayList<Book>();

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void search() {
        try {
            Client apiClient = ClientBuilder.newClient().register(JaxRsHalBuilderReaderSupport.class);
            WebTarget apiHome = apiClient.target("http://localhost:8080/rest");
            // home
            Response responseHome = apiHome.request(HAL_JSON).get();
            ContentRepresentation repHome = responseHome.readEntity(ContentRepresentation.class);
            // search
            Link searchLink = repHome.getLinkByRel("lib:search");
            String href = replaceParam(searchLink.getHref(), query);
            Response responseSearch = apiClient.target(href).request(HAL_JSON).get();
            ContentRepresentation repSearch = responseSearch.readEntity(ContentRepresentation.class);

            LOGGER.info("Value: {}", repSearch.getContent());

            Collection<ReadableRepresentation> resultSet = repSearch.getResourceMap().get("lib:book");
            books.clear();
            for (ReadableRepresentation rep : resultSet) {
                books.add(new Book((String) rep.getValue("title"), (String) rep.getValue("author"), (String) rep
                        .getValue("description", null)));
            }
        } catch (Exception e) {
            LOGGER.error("Error during search", e);
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Error during search", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
