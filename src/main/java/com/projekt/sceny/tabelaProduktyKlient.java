package com.projekt.sceny;

import com.projekt.GUI;
import com.projekt.Tabele.Klienci;
import com.projekt.Tabele.Produkty;
import com.projekt.Tabele.Typy_produktow;
import com.projekt.Tabele.Zamowienia;
import com.projekt.initHibernate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
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
public class tabelaProduktyKlient implements Initializable {
    private static final ObservableList<Typy_produktow> ltp = FXCollections.observableArrayList();
    private static ObservableList<Produkty> lp = FXCollections.observableArrayList();
    private String s = "";
    @FXML
    private TableColumn<Produkty, Float> cenaCol;
    @FXML
    private TableColumn<Produkty, Integer> idProduktuCol;
    @FXML
    private TableColumn<Produkty, String> nazwaCol;
    @FXML
    private TableColumn<Produkty, String> opisCol;
    @FXML
    private TableColumn<Produkty, String> producentCol;
    @FXML
    private TableColumn<Produkty, String> modelCol;
    @FXML
    private TableView<Produkty> table;
    @FXML
    private TableColumn<Produkty, String> typProduktuCol;
    @FXML
    private TextField doWyszukiwaniaF;
    @FXML
    private Label doWyszukiwaniaL;
    @FXML
    private VBox wyszukiwanieBox;
    @FXML
    private Label comboBoxL;
    @FXML
    private ComboBox<Typy_produktow> comboBoxTypProduktu;
    private Integer idZalogowanegoK;
    private TableColumn<Produkty,Void> dodajZamCol;
    /**
     * Metoda która wywołuje się po otwarciu przypisanej
     * sceny do tej klasy.Inicjalizuje wartości
     * jakie ma zawierać tabelview produktów, wywołuje metode
     * pokazTabeleProdukty() oraz zczytuje z bazy listę
     * dostępnych typów produktów oraz dodaje je do comboboxa
     * na początku otwarcia sceny
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pokazTabeleProdukty();
        idProduktuCol.setCellValueFactory(new PropertyValueFactory<>("idProduktu"));
        producentCol.setCellValueFactory(new PropertyValueFactory<>("producent"));
        nazwaCol.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        cenaCol.setCellValueFactory(new PropertyValueFactory<>("cena"));
        opisCol.setCellValueFactory(new PropertyValueFactory<>("opis"));
        typProduktuCol.setCellValueFactory(new PropertyValueFactory<>("idTypuProduktu"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        Session session = initHibernate.sessionFactory.openSession();
        ltp.addAll(session.createQuery("from Typy_produktow", Typy_produktow.class).list());
        comboBoxTypProduktu.getItems().setAll(ltp);
        session.close();
        this.dodajZamCol = new TableColumn<>();
        Callback<TableColumn<Produkty, Void>, TableCell<Produkty, Void>> dodajZamCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Produkty, Void> call(final TableColumn<Produkty, Void> param) {
                        final TableCell<Produkty, Void> cell = new TableCell<>() {
                            final Image image = new Image("dodajZam.png");
                            private final Button btn = new Button();

                            {
                                btn.setGraphic(new ImageView(image));
                                btn.setOnAction((ActionEvent event) -> {
                                        Session session = initHibernate.sessionFactory.openSession();
                                        Transaction transaction = session.beginTransaction();
                                        Integer idProduktu = getTableView().getItems().get(getIndex()).getIdProduktu();
                                        Produkty produkt = session.load(Produkty.class, idProduktu);
                                        Klienci klient = session.load(Klienci.class,idZalogowanegoK);
                                        Zamowienia zamowienie = new Zamowienia(klient,produkt);
                                        session.save(zamowienie);
                                        transaction.commit();
                                        pokazTabeleProdukty();
                                        Alert a = new Alert(Alert.AlertType.NONE, "Pomyślnie zamówiłeś produkt!\n\n"+produkt.toString(), new ButtonType("Zamknij"));
                                        a.show();
                                        session.close();
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean pusty) {
                                super.updateItem(item, pusty);
                                if (pusty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                        cell.setAlignment(Pos.CENTER);
                        return cell;
                    }
                };
        this.dodajZamCol.setCellFactory(dodajZamCellFactory);
        this.dodajZamCol.setText("Zamów");
        this.dodajZamCol.setResizable(false);
        this.dodajZamCol.setPrefWidth(70.0);
        this.table.getColumns().add(dodajZamCol);
    }


    /**
     * Metoda ta zmienia scene na scenę z panelem Klienta
     */
    public void zmianaScenyPanelKlienta() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("panelKlienta.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        panelKlienta panelKlienta = fxmlLoader.getController();
        panelKlienta.setIdKlienta(idZalogowanegoK);
        Stage window = (Stage) table.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zapisuje id zalogowanego klienta do zmiennej
     * @param idZalogowanegoK id zalogowanego klienta
     */
    public void setIdZalogowanegoK(Integer idZalogowanegoK){
        this.idZalogowanegoK=idZalogowanegoK;
    }

    /**
     * Metoda ta zczytuje z bazy danych produkty
     * i wrzuca je do listy a następnie tą liste do
     * TableView znajdujace sie na bieżacej scenie
     */
    public void pokazTabeleProdukty() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lp.isEmpty()) {
            lp.clear();
        }
        List<Produkty> List = session.createQuery("from Produkty ", Produkty.class).list();
        lp = FXCollections.observableArrayList(List);
        session.close();
        table.setItems(lp);
        table.refresh();
    }

    /**
     * Metoda ta w zależności od id przekazanego parametru
     * zmienia interfejs do wyszukiwania
     *
     * @param event Przechowuje dane na temat obiektu który wywołał tą metode
     */
    public void interfejsDoWyszukiwania(ActionEvent event) {
        s = ((MenuItem) event.getSource()).getId();
        if (!s.equals("Typie Produktu")) {
            this.doWyszukiwaniaL.setText("Wyszukiwanie po " + s);
            this.comboBoxTypProduktu.setVisible(false);
            this.doWyszukiwaniaF.setVisible(true);
            this.wyszukiwanieBox.setDisable(false);
            this.comboBoxL.setVisible(false);
            this.doWyszukiwaniaF.setPromptText("Tu wpisz " + s + " i wciśnij enter");
        } else {
            this.doWyszukiwaniaL.setText("Wyszukiwanie po " + s);
            this.wyszukiwanieBox.setDisable(false);
            this.doWyszukiwaniaF.setVisible(false);
            this.comboBoxTypProduktu.setVisible(true);
            this.comboBoxL.setVisible(true);
        }
    }

    /**
     * Metoda ta przywraca interfejs do wyszukiwania
     * do domyślnej postaci
     */
    public void wyczyscInterfejs() {
        this.doWyszukiwaniaL.setText("");
        this.wyszukiwanieBox.setDisable(true);
        this.comboBoxL.setVisible(false);
        this.comboBoxTypProduktu.setVisible(false);
        this.doWyszukiwaniaF.setVisible(true);
        this.doWyszukiwaniaF.setPromptText("");
        this.doWyszukiwaniaF.setText("");
    }

    /**
     * Metoda ta po wciśnięciu na klawiaturze przycisku Enter
     * oraz zapisanego wcześniej tekstu z pola do wyszukiwania
     * w zmiennej wywołuje metody obsługujące mechanizm wyszukiwania
     *
     * @param keyEvent Przechowuje dane na temat wciśniętego klawisza na klawiaturze
     */
    @FXML
    private void keyPressedWyszukiwanie(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            switch (s) {
                case "ID" -> wyszukajPoId();
                case "Nazwie" -> wyszukajPoNazwie();
                case "Producencie" -> wyszukajPoProducencie();
                case "Typie Produktu" -> wyszukajPoTypieProduktu();
            }
        }
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoId() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lp.isEmpty()) {
            lp.clear();
        }
        List<Produkty> List = session.createQuery("from Produkty where idProduktu =" + doWyszukiwaniaF.getText()).list();
        lp = FXCollections.observableArrayList(List);
        session.close();
        table.setItems(lp);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od nazwy
     * produktu podanego w polu do wyszukiwania
     */
    public void wyszukajPoNazwie() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lp.isEmpty()) {
            lp.clear();
        }
        List<Produkty> List = session.createQuery("from Produkty where  nazwa='" + doWyszukiwaniaF.getText() + "'").list();
        lp = FXCollections.observableArrayList(List);
        session.close();
        table.setItems(lp);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od producenta
     * produktu podanego w polu do wyszukiwania
     */
    public void wyszukajPoProducencie() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lp.isEmpty()) {
            lp.clear();
        }
        List<Produkty> List = session.createQuery("from Produkty where  producent='" + doWyszukiwaniaF.getText() + "'").list();
        lp = FXCollections.observableArrayList(List);
        session.close();
        table.setItems(lp);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od
     * wybranego w comboboxie typu produktu
     */
    public void wyszukajPoTypieProduktu() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lp.isEmpty()) {
            lp.clear();
        }
        List<Produkty> List = session.createQuery("from Produkty p where idTypuProduktu=" + comboBoxTypProduktu.getValue().getIdTypu()).list();
        lp = FXCollections.observableArrayList(List);
        session.close();
        table.setItems(lp);
        table.refresh();
        wyczyscInterfejs();
        comboBoxTypProduktu.setValue(null);
    }

}
