package com.projekt.sceny;

import com.projekt.GUI;
import com.projekt.Tabele.Klienci;
import com.projekt.Tabele.Zamowienia;
import com.projekt.ZamowieniaTabela;
import com.projekt.initHibernate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Michał Wenc
 * @version 1.0
 */
public class panelKlienta implements Initializable {

    private static final ObservableList<ZamowieniaTabela> lz = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ZamowieniaTabela, String> adresCol;
    @FXML
    private TextField idKlienta;
    @FXML
    private TableColumn<ZamowieniaTabela, Integer> idProduktuCol;
    @FXML
    private TableColumn<ZamowieniaTabela, Integer> idZamowieniaCol;
    @FXML
    private TableColumn<ZamowieniaTabela, String> kodPocztowyCol;
    @FXML
    private TableColumn<ZamowieniaTabela, String> producentCol;
    @FXML
    private TableColumn<ZamowieniaTabela, String> nazwaProduktuCol;
    @FXML
    private Label panelKlienta;
    @FXML
    private BorderPane powitanie;
    @FXML
    private TableView<ZamowieniaTabela> tableZamowienia;
    @FXML
    private TextField zmianaHasla;
    @FXML
    private VBox zmianaHaslaBox;
    private Integer idKlientaS;

    /**
     * Metoda ta wywołuje metode do zmiany hasła przez klienta gdy wciśnie on klawisz enter na klawiaturze
     *
     * @param keyEvent Przechowuje dane na temat wciśniętego klawisza na klawiaturze
     */
    @FXML
    private void keyPressedZmianaHasla(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            zmienHaslo();
        }
    }

    /**
     * Metoda ta wywołuje się przy otwarciu sceny, odpowiada za
     * inicjalizacje wartości jakie mają być wpisane do kolumn w
     * tableview z zamówieniami
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idZamowieniaCol.setCellValueFactory(
                new PropertyValueFactory<>("idZamowienia"));
        adresCol.setCellValueFactory(
                new PropertyValueFactory<>("adres"));
        idProduktuCol.setCellValueFactory(
                new PropertyValueFactory<>("idProduktu"));
        kodPocztowyCol.setCellValueFactory(
                new PropertyValueFactory<>("kodPocztowy"));
        nazwaProduktuCol.setCellValueFactory(
                new PropertyValueFactory<>("nazwaProduktu"));
        producentCol.setCellValueFactory(
                new PropertyValueFactory<>("producentProduktu"));
    }

    /**
     * Metoda ta zmienia idKlienta w kontrolerze na to przekazana w parametrze
     *
     * @param idKlienta id zalogowanego klienta
     */
    public void setIdKlienta(Integer idKlienta) {
        idKlientaS = idKlienta;
        this.idKlienta.setText(idKlientaS + "");
    }

    /**
     * Metoda ta zmienia scene bieżącą na scene z interfejsem do logowania
     */
    public void Wyloguj() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("logowanie.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage window = (Stage) panelKlienta.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia widzialnosć kontenerów z zawartością
     * Ustawia true dla kontenera z zmianą hasła
     * a kontener tabeli z zamówieniami , powitaniem
     * oraz z formularzem do składania zamówień ustawia na false
     */
    public void zmianaKonteneraHaslo() {
        this.powitanie.setVisible(false);
        this.tableZamowienia.setVisible(false);
        this.zmianaHaslaBox.setVisible(true);
    }

    /**
     * Metoda ta zmienia hasło dla zalogowanego użytkownika
     * w bazie danych za pomocą jego id
     */
    public void zmienHaslo() {
        if (zmianaHasla.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.NONE, "Hasło nie może być puste\n", new ButtonType("Zamknij"));
            a.show();
        } else {
            Session session = initHibernate.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Klienci klient = session.load(Klienci.class, this.idKlienta.getText());
            klient.setHaslo(this.zmianaHasla.getText());
            session.update(klient);
            transaction.commit();
            session.close();
            this.zmianaHaslaBox.setVisible(false);
            Alert a = new Alert(Alert.AlertType.NONE, "Zmieniono hasło\n", new ButtonType("Zamknij"));
            a.show();
        }
    }

    /**
     * Metoda ta usuwa konto zalogowanego użytkownika
     * z bazie danych po jego id
     */
    public void usunKonto() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Usuń Konto");
        alert.setContentText("Czy napewno chcesz usunąć konto ?");

        ButtonType yes = new ButtonType("Tak", ButtonBar.ButtonData.LEFT);
        alert.getButtonTypes().add(yes);

        ButtonBar.setButtonUniformSize(alert.getDialogPane().lookupButton(yes), false);
        alert.getDialogPane().lookupButton(yes).addEventFilter(ActionEvent.ACTION, event -> {
            Session session = initHibernate.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Klienci klient = session.load(Klienci.class, this.idKlienta.getText());
            List<Zamowienia> zamowienial = session.createQuery("FROM Zamowienia where klient.id_klienta="+this.idKlienta.getText()).getResultList();
            for (Zamowienia zam : zamowienial){
                session.delete(zam);
            }
            session.delete(klient);
            transaction.commit();
            session.close();
            alert.close();
            Wyloguj();
        });
        ButtonType no = new ButtonType("Nie", ButtonBar.ButtonData.LEFT);
        alert.getButtonTypes().add(no);
        alert.show();
    }

    /**
     * Metoda ta zmienia widzialnosć kontenerów z zawartością
     * Ustawia true dla kontenera dla tabeli z zamówieniami klienta
     * a kontener ze zmianą hasła i powitaniem ustawia
     * oraz z formularzem do składania zamówień na false
     */
    public void zmianaKonteneraTabelaZamowienia() {
        this.powitanie.setVisible(false);
        this.zmianaHaslaBox.setVisible(false);
        this.tableZamowienia.setVisible(true);
    }

    /**
     * Metoda ta wyświetla tabele z zamówieniami oraz
     * wywołuje moetode zmianaKonteneraZamowienie
     */
    public void pokazTabeleZamowienia() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lz.isEmpty()) {
            lz.clear();
        }
        List<Zamowienia> List = session.createQuery("from Zamowienia where klient.id_klienta="+idKlienta.getText(), Zamowienia.class).list();
        for (Zamowienia zam : List) {
            lz.add(new ZamowieniaTabela(zam));
        }
        session.close();
        tableZamowienia.setItems(lz);
        tableZamowienia.refresh();
        zmianaKonteneraTabelaZamowienia();
    }
    /**
     * Metoda ta zmienia scenę na scenę z
     * tabelą produktów
     */
    public void zmianaScenyTabelaProduktow() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaProduktyKlient.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tabelaProduktyKlient tabelaProduktyKlient = fxmlLoader.getController();
        tabelaProduktyKlient.setIdZalogowanegoK(idKlientaS);

        Stage window = (Stage) panelKlienta.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
