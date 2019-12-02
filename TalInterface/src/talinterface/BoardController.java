/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.beans.binding.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author maximo
 */
public class BoardController implements Initializable {

    @FXML
    private ListView<ImageItem> poolList;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;

    private Connecter connecter;
    @FXML
    private ImageView FieldView;
    @FXML
    private Pane imagePane;
    @FXML
    private MenuItem openRegex;
    @FXML
    private MenuItem openAutomata;
    @FXML
    private MenuItem closeItem;

    public static Config config;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button makeDeterministicButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FieldView.fitWidthProperty().bind(imagePane.widthProperty());
        FieldView.fitHeightProperty().bind(imagePane.heightProperty());
        File f = new File("tal.config");
        System.out.println(f.exists());
        config = new Config(f, !f.exists());
        poolList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> {
                    if (newV != null) {
                        FieldView.setImage(newV.getImage());
                    }
                });
        closeButton.disableProperty().bind(Bindings.equal(poolList.getSelectionModel().selectedIndexProperty(), -1));
    }

    @FXML
    private void addButtonHandler(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("Automata.fxml"));
            Parent root = cargador.load();
            AutomataController ac = cargador.<AutomataController>getController();
            ac.setConnecter(connecter);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Couldn't find Automata formulaire");
        }
    }

    @FXML
    private void removeButtonHandler(ActionEvent event) {
    }

    public void setConnecter(Connecter c) {
        this.connecter = c;
    }

    public void addImageItem(ImageItem i) {
        poolList.getItems().add(i);
        poolList.getSelectionModel().select(i);
    }

    @FXML
    private void openAutomata(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open automata...");
        fc.setInitialDirectory(config.getPath());
        File file = fc.showOpenDialog(this.FieldView.getScene().getWindow());
        if (file != null) {
            connecter.openAutomataFile(file.getAbsolutePath());
            config.setPath(file.getParentFile());
        }
    }
    
    private void openFile(int type) {
        FileChooser fc = new FileChooser();
        String title;
        switch (type) {
            case AUTOMATA: title = "Open Automata..."; break;
            case REGEX: title = "Open Regular Expression..."; break;
            default: title = "Open File...";
        }
        fc.setTitle(title);
        fc.setInitialDirectory(config.getPath());
        File file = fc.showOpenDialog(this.FieldView.getScene().getWindow());
        if (file != null) {
            int i = file.getAbsolutePath().lastIndexOf('.');
            if (i >= 0) {
                String ext = file.getAbsolutePath().substring(i + 1);
                switch (ext) {
                    case "a":
                        System.out.println("Opening automata...");
                        connecter.openAutomataFile(file.getAbsolutePath());
                        break;
                    case "e":
                        System.out.println("Opening ER...");
                        break;
                    default:
                        openUnknown();
                        break;
                }
            } else {
                openUnknown();
            }
            config.setPath(file.getParentFile());
        }
    }

    private void openUnknown() {
        System.err.println("Format unknown");
    }
    
    public static final int AUTOMATA = 0;
    public static final int REGEX = 1;
    public static final int UNKNOWN = 2;

    @FXML
    private void closeItem(ActionEvent event) {
        
    }

    @FXML
    private void minimizeAutomata(ActionEvent event) {
        connecter.minimize(poolList.getSelectionModel().getSelectedItem().getID());
    }

    @FXML
    private void makeDeterministic(ActionEvent event) {
    }
}
