/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import java.util.Arrays;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author max
 */
public class TalInterface extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Connecter connecter = new StreamConnecter();
        connecter.start();
        connecter.send("Echo");
        connecter.send("Bruuuuh");
        
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("Board.fxml"));
        Parent root = cargador.load();
        BoardController ac = cargador.<BoardController>getController();

        ac.setConnecter(connecter);
        connecter.setBoardController(ac);

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                connecter.finish();
            }
        });

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
