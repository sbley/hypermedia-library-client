package de.saxsys.hypermedia.javafx;

import javafx.scene.control.ListCell;
import de.saxsys.hypermedia.javafx.service.Book;

public class BookCell extends ListCell<Book> {

    @Override
    protected void updateItem(Book item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getTitle() + " by " + item.getAuthor());
        } else {
            setText("");
        }
    }
}
