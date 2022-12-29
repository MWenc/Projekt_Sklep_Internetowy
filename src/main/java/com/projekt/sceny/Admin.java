package com.projekt.sceny;

import com.projekt.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Michał Wenc
 * @version 1.0
 */
public class Admin implements Initializable {


    protected static String hasloa = "admin";
    @FXML
    private TextField haslo;
    @FXML
    private BorderPane root;
    @FXML
    private VBox vboxHaslo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Metoda ta zmieniaj widok kontrolek w scenie
     * tak by można było zmienić hasło Admina
     */
    public void changePanel() {
        this.vboxHaslo.setVisible(true);
    }

    /**
     * Metoda ta pobiera nowe haslo z kontrolki do wpisania textu
     * i zmieniająca hasło admina na nowo podane
     */
    public void changePassword() {
        if (this.haslo.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź hasło", new ButtonType("Zamknij"));
            a.show();
        } else {
            hasloa = this.haslo.getText();
            this.vboxHaslo.setVisible(false);
        }
    }

    /**
     * Metoda ta zmienia sceny z panelu admina na scene z logowaniem
     */
    public void logOutB() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("logowanie.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage window = (Stage) this.root.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia scenę aktualną na scenę z
     * tabelą klientów
     */
    public void zmianaScenyTabelaKlientow() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaKlienci.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Stage window = (Stage) this.root.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia scenę aktualną na scenę z
     * tabelą typy produktów
     */
    public void zmianaScenyTabelaTypyProduktow() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaTypyProduktow.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Stage window = (Stage) this.root.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia scenę aktualną na scenę z
     * tabelą produktów
     */
    public void zmianaScenyTabelaProduktow() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaProdukty.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Stage window = (Stage) this.root.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia scenę aktualną na scenę z
     * zamowieniami
     */
    public void zmianaScenyTabelaZamowienia() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaZamowienia.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Stage window = (Stage) this.root.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
