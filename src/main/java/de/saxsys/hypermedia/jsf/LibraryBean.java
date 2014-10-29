package de.saxsys.hypermedia.jsf;

import static de.saxsys.hypermedia.jsf.HalUtil.replaceParam;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.cache.BrowserCacheFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.theoryinpractise.halbuilder.api.ContentRepresentation;
import com.theoryinpractise.halbuilder.api.Link;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.jaxrs.JaxRsHalBuilderReaderSupport;

import de.saxsys.hypermedia.jsf.interceptor.MeasureTime;

@Named("library")
@ViewScoped
public class LibraryBean implements Serializable {

    private static final String BASE_URL = "http://localhost:8080/rest";
    private static final String HAL_JSON = "application/hal+json";
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryBean.class);

    private String query;
    private List<Book> books = new ArrayList<Book>();
    private Book detailBook;

    private Integer lendTo;

    private Client apiClient;

    @PostConstruct
    public void init() {
        apiClient =
                ClientBuilder.newClient()
                        .register(JaxRsHalBuilderReaderSupport.class)
                        .register(BrowserCacheFeature.class);
    }

    @MeasureTime
    public void search() {
        try {
            WebTarget apiHome = apiClient.target(BASE_URL);
            // home
            Response responseHome = apiHome.request(HAL_JSON).get();
            ContentRepresentation repHome = responseHome.readEntity(ContentRepresentation.class);
            // search
            Link searchLink = repHome.getLinkByRel("lib:search");
            String href = replaceParam(searchLink.getHref(), query);
            Response responseSearch = apiClient.target(href).request(HAL_JSON).get();
            ContentRepresentation repSearch =
                    responseSearch.readEntity(ContentRepresentation.class);

            Collection<ReadableRepresentation> resultSet =
                    repSearch.getResourceMap().get("lib:book");
            books.clear();
            if (null != resultSet) {
                for (ReadableRepresentation rep : resultSet) {
                    Book book = toBook(rep);
                    new Book(
                            rep.getResourceLink().getHref(), (String) rep.getValue("title", null),
                            (String) rep.getValue("author", null), (String) rep.getValue(
                                    "description", null), rep.getLinkByRel("lib:lend"),
                            rep.getLinkByRel("lib:return"));
                    if (null == book.getTitle() || null == book.getAuthor()) {
                        // get book
                        Response response =
                                apiClient.target(book.getHref()).request(HAL_JSON).get();
                        ContentRepresentation repBook =
                                response.readEntity(ContentRepresentation.class);
                        book = toBook(repBook);
                    }
                    books.add(book);
                }
            }
            detailBook = null;
        } catch (Exception e) {
            LOGGER.error("Error during search", e);
            displayError("Error during search", e.getMessage());
        }
    }

    @MeasureTime
    public void showDetails(Book item) {
        LOGGER.debug("Show details for book at {}", item.getHref());
        try {
            Response response = apiClient.target(item.getHref()).request(HAL_JSON).get();
            ContentRepresentation rep = response.readEntity(ContentRepresentation.class);
            detailBook = toBook(rep);
        } catch (Exception e) {
            LOGGER.error("Error retrieving book", e);
            displayError("Error retrieving book", e.getMessage());
        }
    }

    @MeasureTime
    public void lend() {
        LOGGER.debug("Lend book {} to member {}", detailBook.getTitle(), lendTo);
        Response response =
                apiClient.target(
                        replaceParam(detailBook.getRelLend().getHref(), String.valueOf(lendTo)))
                        .request(HAL_JSON)
                        .put(Entity.json("{\"memberId\":" + lendTo + "}"));
        ContentRepresentation rep = response.readEntity(ContentRepresentation.class);
        if (response.getStatus() >= 400) {
            String message = (String) rep.getValue("title");
            String detail = (String) rep.getValue("detail", null);
            LOGGER.error("{} {} {}", response.getStatus(), message, detail);
            displayError(message, detail);
        } else {
            detailBook = toBook(rep);
            lendTo = null;
            LOGGER.debug("Book {} lent to member {}", detailBook.getTitle(),
                    detailBook.getBorrower());
            displayInfo("Book lent to member " + detailBook.getBorrower());
        }
    }

    @MeasureTime
    public void takeBack() {
        LOGGER.debug("Return book {} from member {}", detailBook.getTitle(),
                detailBook.getBorrower());
        Response response =
                apiClient.target(
                        replaceParam(detailBook.getRelReturn().getHref(), detailBook.getBorrower()
                                .toString()))
                        .request(HAL_JSON)
                        .delete();
        ContentRepresentation rep = response.readEntity(ContentRepresentation.class);
        if (response.getStatus() >= 400) {
            String message = (String) rep.getValue("title");
            String detail = (String) rep.getValue("detail", null);
            LOGGER.error("{} {}", message, detail);
            displayError(message, detail);
        } else {
            detailBook = toBook(rep);
            LOGGER.debug("Book {} returned", detailBook.getTitle());
            displayInfo("Book returned");
        }
    }

    private Book toBook(ReadableRepresentation rep) {
        Book book =
                new Book(
                        rep.getResourceLink().getHref(), (String) rep.getValue("title", null),
                        (String) rep.getValue("author", null), (String) rep.getValue("description",
                                null), rep.getLinkByRel("lib:lend"), rep.getLinkByRel("lib:return"));
        List<? extends ReadableRepresentation> borrowerResource = rep.getResourcesByRel("borrower");
        if (1 == borrowerResource.size())
            book.setBorrower(Integer.valueOf((String) borrowerResource.get(0).getValue("id")));
        return book;
    }

    private void displayError(String message, String detail) {
        FacesMessage msg = new FacesMessage(SEVERITY_ERROR, message, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void displayInfo(String message) {
        FacesMessage msg = new FacesMessage(SEVERITY_INFO, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getDetailBook() {
        return detailBook;
    }

    public Integer getLendTo() {
        return lendTo;
    }

    public void setLendTo(Integer lendTo) {
        this.lendTo = lendTo;
    }
}
