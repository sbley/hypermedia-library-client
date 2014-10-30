package de.saxsys.hypermedia.javafx;

import javafx.scene.control.ListCell;
import de.saxsys.hypermedia.javafx.service.Book;

public class BookCell extends ListCell<Book> {
    @Override
    protected void updateItem(Book arg0, boolean arg1) {
        super.updateItem(arg0, arg1);
        if (arg0 != null) {
            setText(arg0.getTitle() + " by " + arg0.getAuthor());
        }
    }
}
