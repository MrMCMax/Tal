/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author max
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextField IDField;
    @FXML
    private Button createButton;
    @FXML
    private Button getButton;
    
    private Socket connection;
    private PrintWriter pw;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = new Socket("localhost", 9000);
            pw = new PrintWriter(connection.getOutputStream(), false);
        } catch (IOException e) {
            System.err.println("Couldn't connect");
        }
    }    

    @FXML
    private void buttonHandler(ActionEvent event) {
        if (event.getSource() == createButton) {
            pw.println("Create ER");
            pw.println(IDField.getText());
            pw.flush();
        } else if (event.getSource() == getButton){
            pw.println("Get ER");
            pw.println(IDField.getText());
            pw.flush();
        }
    }
    
}
