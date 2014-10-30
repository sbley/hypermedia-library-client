package de.saxsys.hypermedia.javafx;

import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.google.common.collect.ImmutableSet;

import de.saxsys.hypermedia.jsf.Book;
import de.saxsys.hypermedia.jsf.LibraryBean;
import de.saxsys.mvvmfx.FxmlView;

public class MainApplication implements FxmlView {

    @Inject
    private LibraryBean bean;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Button rentButton;

    @FXML
    private TextField lendTextField;

    @FXML
    private Label returnName;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button returnButton;

    @FXML
    private Button searchButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ListView<Book> bookList;

    public void initialize() {
        bookList.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> p) {
                ListCell<Book> cell = new ListCell<Book>() {
                    @Override
                    protected void updateItem(Book book, boolean bln) {

                        super.updateItem(book, bln);
                        if (book != null) {
                            setText(book.getTitle() + " by " + book.getAuthor());
                        }
                    }
                };
                return cell;
            }
        });
    }

    @FXML
    void lendButtonPressed(ActionEvent event) {

    }

    @FXML
    void returnButtonPressed(ActionEvent event) {

    }

    @FXML
    void searchButtonPressed(ActionEvent event) {
        List<Book> search = bean.search(searchTextField.getText());
        bookList.setItems(FXCollections.observableArrayList(search));
    }

    /*
     * Fix of CDI + Guava 1.15 issue //
     * https://code.google.com/p/guava-libraries/issues/detail?id=1433#c20
     */
    @Produces
    Set<com.google.common.util.concurrent.Service> dummyServices() {
        return ImmutableSet.of();
    }
}
