/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author max
 */
public class AutomataController implements Initializable {

    @FXML
    private TextField stateTextField;
    @FXML
    private Button addState;
    @FXML
    private ListView<String> stateList;
    @FXML
    private Button removeStateButton;
    @FXML
    private TextField symbolTextField;
    @FXML
    private Button addSymbol;
    @FXML
    private ListView<String> symbolList;
    @FXML
    private Button removeSymbol;
    @FXML
    private ComboBox<String> initialStateBox;
    @FXML
    private ComboBox<String> symbolBox;
    @FXML
    private ComboBox<String> endStateBox;
    @FXML
    private Button addTransitionButton;
    @FXML
    private ListView<String> transitionList;
    @FXML
    private Button removeTransitionButton;
    @FXML
    private ComboBox<String> initialStateChoice;
    @FXML
    private ComboBox<String> finalStateCombo;
    @FXML
    private Button addFinalState;
    @FXML
    private ListView<String> finalStateList;
    @FXML
    private Button removeFinalStateButton;
    @FXML
    private Button createAutomataButton;

    private Connecter connecter;
    @FXML
    private TextField nameField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        removeStateButton.disableProperty().bind(Bindings.equal(-1, stateList.getSelectionModel().selectedIndexProperty()));
        removeSymbol.disableProperty().bind(Bindings.equal(-1, symbolList.getSelectionModel().selectedIndexProperty()));
        removeTransitionButton.disableProperty().bind(Bindings.equal(-1, transitionList.getSelectionModel().selectedIndexProperty()));
        removeFinalStateButton.disableProperty().bind(Bindings.equal(-1, finalStateList.getSelectionModel().selectedIndexProperty()));

        addState.disableProperty().bind(Bindings.equal("", stateTextField.textProperty()));
        addSymbol.disableProperty().bind(Bindings.equal("", symbolTextField.textProperty()));
        addTransitionButton.disableProperty().bind(orEncadenado(
                Bindings.equal(initialStateBox.getSelectionModel().selectedIndexProperty(), -1),
                Bindings.equal(symbolBox.getSelectionModel().selectedIndexProperty(), -1),
                Bindings.equal(endStateBox.getSelectionModel().selectedIndexProperty(), -1)));
        addFinalState.disableProperty().bind(Bindings.equal(-1, finalStateCombo.getSelectionModel().selectedIndexProperty()));

        initialStateBox.itemsProperty().bind(stateList.itemsProperty());
        symbolBox.itemsProperty().bind(symbolList.itemsProperty());
        endStateBox.itemsProperty().bind(stateList.itemsProperty());
        initialStateChoice.itemsProperty().bind(stateList.itemsProperty());
        finalStateCombo.itemsProperty().bind(stateList.itemsProperty());
    }

    @FXML
    private void addState(ActionEvent event) {
        stateList.getItems().add(stateTextField.getText());
    }

    @FXML
    private void removeState(ActionEvent event) {
        stateList.getItems().remove(stateList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void addSymbol(ActionEvent event) {
        if (!symbolTextField.getText().equals("")) {
            symbolList.getItems().add(symbolTextField.getText());
        }
    }

    @FXML
    private void removeSymbol(ActionEvent event) {
        symbolList.getItems().remove(symbolList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void addTransition(ActionEvent event) {
        transitionList.getItems().add(String.format("(%s %s %s)",
                initialStateBox.getSelectionModel().getSelectedItem(),
                symbolBox.getSelectionModel().getSelectedItem(),
                endStateBox.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void removeTransition(ActionEvent event) {
        transitionList.getItems().remove(transitionList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void addFinalState(ActionEvent event) {
        finalStateList.getItems().add(finalStateCombo.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void removeFinalState(ActionEvent event) {
        finalStateList.getItems().remove(finalStateList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void createAutomata(ActionEvent event) {
        String[] type = new String[0]; //For type conversion in the "toArray" method
        String[] stateArray = stateList.getItems().toArray(type);
        String states = Arrays.toString(stateArray);
        states = states.substring(1, states.length() - 1);
        String[] alphabetArray = symbolList.getItems().toArray(type);
        String alphabet = Arrays.toString(alphabetArray);
        alphabet = alphabet.substring(1, alphabet.length() - 1);
        String[] transitionArray = transitionList.getItems().toArray(type);
        String transition = Arrays.toString(transitionArray);
        transition = transition.substring(1, transition.length() - 1);
        System.err.println("Transitions client-side: " + transition);
        String[] finalStateArray = finalStateList.getItems().toArray(type);
        String finalStates = Arrays.toString(finalStateArray);
        finalStates = finalStates.substring(1, finalStates.length() - 1);
        String name = nameField.getText();
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save automata...");
        fileChooser.setInitialDirectory(BoardController.config.getPath());
        File file = fileChooser.showSaveDialog(this.addSymbol.getScene().getWindow());
        BoardController.config.setPath(file.getParentFile());
        connecter.createAutomata(file.getAbsolutePath(), name, states, alphabet, transition,
                initialStateChoice.getSelectionModel().getSelectedItem(), finalStates);
        addTransitionButton.getScene().getWindow().hide();
    }

    public void setConnecter(Connecter c) {
        this.connecter = c;
    }

    public static BooleanBinding orEncadenado(ObservableBooleanValue b1, ObservableBooleanValue b2, ObservableBooleanValue... rest) {
        BooleanBinding res = Bindings.or(b1, b2);
        for (ObservableBooleanValue bi : rest) {
            res = Bindings.or(res, bi);
        }
        return res;
    }
}
