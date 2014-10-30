package de.saxsys.hypermedia.javafx;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ErrorLog {

    private final StringProperty error = new SimpleStringProperty("");

    public StringProperty errorProperty() {
        return this.error;
    }

    public void error(String message) {
        Platform.runLater(() -> error.set(message));

    }
}
