/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 *
 * @author maximo
 */
public class talinterface extends Application {



    @Override
    public void start(Stage stage) {
        String version = System.getProperty("java.version");
        Label l = new Label ("Hello, JavaFX 11, running on " + version);
        Scene scene = new Scene(new StackPane(l), 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
