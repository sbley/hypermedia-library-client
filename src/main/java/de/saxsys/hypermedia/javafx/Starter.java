package de.saxsys.hypermedia.javafx;

import javafx.scene.Scene;
import javafx.stage.Stage;
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
        Scene scene = new Scene(tuple.getView());
        primaryStage.setScene(scene);

        primaryStage.show();
        // FlatterFX.style(FlatterInputType.DEFAULT);
    }

}
