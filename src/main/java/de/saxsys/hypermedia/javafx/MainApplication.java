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

import de.saxsys.hypermedia.javafx.service.Book;
import de.saxsys.hypermedia.javafx.service.LibraryService;
import de.saxsys.mvvmfx.FxmlView;

public class MainApplication implements FxmlView {
    @Inject
    private LibraryService bean;

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

        titleLabel.setText("");
        authorLabel.setText("");
        descriptionLabel.setText("");
        lendTextField.setVisible(false);
        rentButton.setVisible(false);
        returnButton.setVisible(false);
        returnName.setVisible(false);

        bookList.setCellFactory(p -> new BookCell());
        listProgress.setVisible(false);
        detailProgress.setVisible(false);
        bookList.getSelectionModel()
                .selectedItemProperty()
                .addListener((ChangeListener<Book>) (arg0, oldBook, newBook) -> {
                    if (newBook != null) {
                        requestBookDisplay(newBook);
                    }

                });

        detailBook.addListener((ChangeListener<Book>) (arg0, arg1, newBook) -> {
            if (newBook != null) {
                titleLabel.setText(newBook.getTitle() == null ? "" : newBook.getTitle());
                authorLabel.setText(newBook.getAuthor() == null ? "" : newBook.getAuthor());
                descriptionLabel.setText(newBook.getDesc() == null ? "No Description"
                        : newBook.getDesc());
                returnName.setText(newBook.getBorrower() == null ? "Error" : "Lent to member: "
                        + newBook.getBorrower().toString());
                lendTextField.setVisible(newBook.isAvailable());
                rentButton.setVisible(newBook.isAvailable());
                returnButton.setVisible(newBook.isReturnable());
                returnName.setVisible(newBook.isReturnable());
            }
        });

        errorLabel.setOnMouseClicked(arg0 -> {
            errorlog.errorProperty().set("");
        });

        errorLabel.textProperty().bind(errorlog.errorProperty());
    }

    @FXML
    void lendButtonPressed(ActionEvent event) {
        detailProgress.setVisible(true);
        new Async(() -> {
            try {
                Book lend = bean.lend(lendTextField.getText(), detailBook.get());
                Platform.runLater(() -> displayBookDetails(lend));
            } finally {
                Platform.runLater(() -> detailProgress.setVisible(false));
            }
        }).start();
    }

    @FXML
    void returnButtonPressed(ActionEvent event) {
        detailProgress.setVisible(true);
        new Async(() -> {
            try {
                Book takeBack = bean.takeBack(detailBook.get());
                Platform.runLater(() -> displayBookDetails(takeBack));
            } finally {
                Platform.runLater(() -> detailProgress.setVisible(false));
            }
        }).start();
    }

    @FXML
    void searchButtonPressed(ActionEvent event) {
        listProgress.setVisible(true);
        new Async(
                () -> {
                    try {
                        List<Book> search = bean.search(searchTextField.getText());
                        Platform.runLater(() -> bookList.setItems(FXCollections.observableArrayList(search)));
                    } finally {
                        Platform.runLater(() -> listProgress.setVisible(false));
                    }
                }).start();
    }

    private void requestBookDisplay(Book book) {
        listProgress.setVisible(true);
        new Async(() -> {
            try {
                Book detail = bean.showDetails(book);
                Platform.runLater(() -> displayBookDetails(detail));
            } finally {
                Platform.runLater(() -> listProgress.setVisible(false));
            }
        }).start();
    }

    private void displayBookDetails(Book bookToLend) {
        if (null != bookToLend) {
            detailBook.set(null);
            detailBook.set(bookToLend);
        }
    }

}
