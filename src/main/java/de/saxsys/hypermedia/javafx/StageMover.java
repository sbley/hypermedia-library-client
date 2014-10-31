package de.saxsys.hypermedia.javafx;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StageMover {

    public static void makeStageMovable(final Stage stage) {
        Scene scene = stage.getScene();
        final Delta dragDelta = new Delta();
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            // record a delta distance for the drag and drop operation.
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();
            });
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + dragDelta.x);
            stage.setY(mouseEvent.getScreenY() + dragDelta.y);
        });
    }
}

class Delta {
    double x, y;
}
