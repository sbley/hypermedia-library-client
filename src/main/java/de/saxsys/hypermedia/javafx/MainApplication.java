package de.saxsys.hypermedia.javafx;

import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import javax.inject.Inject;

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

    @FXML
    private ProgressIndicator listProgress;

    @FXML
    private ProgressIndicator detailProgress;

    @FXML
    private Label errorLabel;

    @Inject
    private ErrorLog errorlog;

    private final ObjectProperty<Book> detailBook = new SimpleObjectProperty<>();

    public void initialize() {
        bookList.setCellFactory(p -> new BookCell());
        listProgress.setVisible(false);
        detailProgress.setVisible(false);
        bookList.getSelectionModel().selectedItemProperty()
                .addListener((ChangeListener<Book>) (arg0, oldBook, newBook) -> {
                    if (newBook != null) {
                        requestBookDisplay(newBook);
                    }

                });

        detailBook.addListener((ChangeListener<Book>) (arg0, arg1, newBook) -> {
            if (newBook != null) {
                titleLabel.setText(newBook.getTitle() == null ? "" : newBook.getTitle());
                authorLabel.setText(newBook.getAuthor() == null ? "" : newBook.getAuthor());
                descriptionLabel.setText(newBook.getDesc() == null ? "No Description" : newBook.getDesc());
                returnName.setText(newBook.getBorrower() == null ? "Fehler" : newBook.getBorrower().toString());
                lendTextField.setVisible(!newBook.isLent());
                rentButton.setVisible(!newBook.isLent());
                returnButton.setVisible(newBook.isLent());
                returnName.setVisible(newBook.isLent());
            }
        });

        errorLabel.setOnMouseClicked(arg0 -> {
            errorlog.errorProperty().set("");
        });

        errorLabel.textProperty().bind(errorlog.errorProperty());

        errorLabel.textProperty().addListener((ChangeListener<String>) (arg0, arg1, arg2) -> {
            System.out.println(arg2);
        });
    }

    @FXML
    void lendButtonPressed(ActionEvent event) {
        detailProgress.setVisible(true);
        new Async(() -> {
            Book lend = bean.lend(lendTextField.getText(), detailBook.get());
            Platform.runLater(() -> {
                displayBookDetails(lend);
                detailProgress.setVisible(false);
            });
        }).start();
    }

    @FXML
    void returnButtonPressed(ActionEvent event) {
        detailProgress.setVisible(true);
        new Async(() -> {
            Book takeBack = bean.takeBack(detailBook.get());
            Platform.runLater(() -> {
                displayBookDetails(takeBack);
                detailProgress.setVisible(false);
            });
        }).start();
    }

    @FXML
    void searchButtonPressed(ActionEvent event) {
        listProgress.setVisible(true);
        new Async(() -> {
            List<Book> search = bean.search(searchTextField.getText());
            Platform.runLater(() -> {
                bookList.setItems(FXCollections.observableArrayList(search));
                listProgress.setVisible(false);
            });
        }).start();

    }

    private void requestBookDisplay(Book book) {
        listProgress.setVisible(true);
        new Async(() -> {
            Book detail = bean.showDetails(book);
            Platform.runLater(() -> {
                Platform.runLater(() -> {
                    displayBookDetails(detail);
                    listProgress.setVisible(false);
                });
            });
        }).start();
    }

    private void displayBookDetails(Book bookToLend) {
        detailBook.set(null);
        detailBook.set(bookToLend);
    }

}