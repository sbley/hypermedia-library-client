package de.saxsys.hypermedia.javafx;

import insidefx.undecorator.Undecorator;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.guigarage.flatterfx.FlatterFX;
import com.guigarage.flatterfx.FlatterInputType;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.cdi.MvvmfxCdiApplication;

public class Starter extends MvvmfxCdiApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void startMvvmfx(Stage primaryStage) throws Exception {
        // Parent view =
        // FXMLLoader.load(getClass().getResource("MainApplication.fxml"));

        ViewTuple tuple = FluentViewLoader.fxmlView(MainApplication.class).load();
        primaryStage.setMinWidth(1200);
        primaryStage.setMaxWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Undecorator undecorator = new Undecorator(primaryStage, (Region) tuple.getView());
        undecorator.getStylesheets().add("skin/undecorator.css");
        Scene scene = new Scene(undecorator, 1200, 700);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        FlatterFX.style(FlatterInputType.DEFAULT);
    }

}
