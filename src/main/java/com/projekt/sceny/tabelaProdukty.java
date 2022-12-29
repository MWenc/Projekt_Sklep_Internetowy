package com.projekt.sceny;

import com.projekt.GUI;
import com.projekt.Tabele.Produkty;
import com.projekt.Tabele.Typy_produktow;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Michał Wenc
 * @version 1.0
 */

public class tabelaProdukty implements Initializable {

    private static final ObservableList<Typy_produktow> ltp = FXCollections.observableArrayList();
    private static ObservableList<Produkty> lp = FXCollections.observableArrayList();
    private String s = "";
    private Integer idProduktu;
    @FXML
    private TableColumn<Produkty, Float> cenaCol;
    @FXML
    private ComboBox<Typy_produktow> edytujComboBox;
    @FXML
    private Label comboBoxL;
    @FXML
    private ComboBox<Typy_produktow> comboBoxTypProduktu;
    @FXML
    private TextField doWyszukiwaniaF;
    @FXML
    private Label doWyszukiwaniaL;
    @FXML
    private TextField dodajCena;
    @FXML
    private ComboBox<Typy_produktow> dodajComboTypProduktu;
    @FXML
    private TextField dodajModel;
    @FXML
    private TextField dodajNazwa;
    @FXML
    private TextArea dodajOpis;
    @FXML
    private TextField dodajProducent;
    @FXML
    private VBox dodajProdukt;
    @FXML
    private TextField edytujCena;
    @FXML
    private VBox edytujProdukt;
    @FXML
    private Label edytujProduktL;
    @FXML
    private TableColumn<Produkty, Integer> idProduktuCol;
    @FXML
    private TableColumn<Produkty, String> modelCol;
    @FXML
    private TableColumn<Produkty, String> nazwaCol;
    @FXML
    private TableColumn<Produkty, String> opisCol;
    @FXML
    private TableColumn<Produkty, String> producentCol;
    @FXML
    private TableView<Produkty> table;
    @FXML
    private TableColumn<Produkty, String> typProduktuCol;
    private TableColumn<Produkty, Void> edytujCol;
    private TableColumn<Produkty, Void> usunCol;
    @FXML
    private VBox wyszukiwanieBox;
    @FXML
    private TextField edytujModel;
    @FXML
    private TextField edytujNazwa;
    @FXML
    private TextArea edytujOpis;
    @FXML
    private TextField edytujProducent;
    @FXML
    private Menu mSzukaj;
    @FXML
    private Menu mRekordy;

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
        opisCol.setCellFactory(tc -> {
            TableCell<Produkty, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(opisCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        typProduktuCol.setCellValueFactory(new PropertyValueFactory<>("idTypuProduktu"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        Session session = initHibernate.sessionFactory.openSession();
        ltp.addAll(session.createQuery("from Typy_produktow", Typy_produktow.class).list());
        comboBoxTypProduktu.setItems(ltp);
        dodajComboTypProduktu.setItems(ltp);
        edytujComboBox.setItems(ltp);
        session.close();
        this.usunCol = new TableColumn<>();
        Callback<TableColumn<Produkty, Void>, TableCell<Produkty, Void>> usunCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Produkty, Void> call(final TableColumn<Produkty, Void> param) {
                        final TableCell<Produkty, Void> cell = new TableCell<>() {
                            final Image image = new Image("usun.png");
                            private final Button btn = new Button();

                            {
                                btn.setGraphic(new ImageView(image));
                                btn.setOnAction((ActionEvent event) -> {
                                    try {
                                        Session session = initHibernate.sessionFactory.openSession();
                                        Transaction transaction = session.beginTransaction();
                                        Integer idProduktu = getTableView().getItems().get(getIndex()).getIdProduktu();
                                        Produkty produkty = session.get(Produkty.class, idProduktu);
                                        session.remove(produkty);
                                        transaction.commit();
                                        session.close();
                                        pokazTabeleProdukty();
                                    } catch (PersistenceException e) {
                                        Alert a = new Alert(Alert.AlertType.NONE, "Usuń najpierw zamowienia z podanym produktem!", new ButtonType("Zamknij"));
                                        a.show();
                                    }
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
        this.usunCol.setCellFactory(usunCellFactory);
        this.usunCol.setText("Usuń");
        this.usunCol.setResizable(false);
        this.usunCol.setPrefWidth(70.0);
        this.table.getColumns().add(usunCol);
        this.edytujCol = new TableColumn<>();
        Callback<TableColumn<Produkty, Void>, TableCell<Produkty, Void>> edytujCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Produkty, Void> call(final TableColumn<Produkty, Void> param) {
                        final TableCell<Produkty, Void> cell = new TableCell<>() {
                            final Image image = new Image("edycja.png");
                            private final Button btn = new Button();

                            {
                                btn.setGraphic(new ImageView(image));
                                btn.setOnAction((ActionEvent event) -> {
                                    zmianaInterfejsuEdytuj(getTableView().getItems().get(getIndex()));
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
        this.edytujCol.setCellFactory(edytujCellFactory);
        this.edytujCol.setText("Edytuj");
        this.edytujCol.setResizable(false);
        this.edytujCol.setPrefWidth(70.0);
        this.table.getColumns().add(edytujCol);
    }


    /**
     * Metoda ta zmienia scene na scenę z panelem Admina
     */
    public void zmianaScenyPanelAdmina() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("admin.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage window = (Stage) table.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia scene na scenę z tabelą zamowienia
     */
    public void zmianaScenyTabelaZamowienia() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaZamowienia.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage window = (Stage) table.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia scene na scenę z tabeą typów produktów
     */
    public void zmianaScenyTabelaTypyProduktow() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaTypyProduktow.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage window = (Stage) table.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta zmienia scene na scenę z Tabelą klientów
     */
    public void zmianaScenyTabelaKlienci() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaKlienci.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage window = (Stage) table.getScene().getWindow();
        window.setScene(scene);
        window.show();
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
            this.mRekordy.setDisable(true);
            this.comboBoxL.setVisible(false);
            this.edytujCol.setVisible(false);
            this.doWyszukiwaniaF.setPromptText("Tu wpisz " + s + " i wciśnij enter");
        } else {
            this.doWyszukiwaniaL.setText("Wyszukiwanie po " + s);
            this.wyszukiwanieBox.setDisable(false);
            this.doWyszukiwaniaF.setVisible(false);
            this.mRekordy.setDisable(true);
            this.edytujCol.setVisible(false);
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
        this.table.setVisible(true);
        this.mRekordy.setDisable(false);
        this.mSzukaj.setDisable(false);
        this.dodajProdukt.setVisible(false);
        this.edytujProdukt.setVisible(false);
        this.doWyszukiwaniaF.setPromptText("");
        this.doWyszukiwaniaF.setText("");
        this.edytujCol.setVisible(true);
        this.dodajCena.setText("");
        this.dodajModel.setText("");
        this.dodajNazwa.setText("");
        this.dodajProducent.setText("");
        this.dodajOpis.setText("");
        this.edytujComboBox.setValue(null);
        this.dodajComboTypProduktu.setValue(null);
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
        try {
            List<Produkty> List = session.createQuery("from Produkty where idProduktu =" + doWyszukiwaniaF.getText()).list();
            lp = FXCollections.observableArrayList(List);
            session.close();
            table.setItems(lp);
            table.refresh();
            wyczyscInterfejs();
        } catch (IllegalArgumentException e) {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź liczby", new ButtonType("Zamknij"));
            a.show();
        }
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

    /**
     * Metoda ta mienia interfejs na interfejs do dodania produktu
     */
    public void zmianaInterfejsuDodaj() {
        table.setVisible(false);
        dodajProdukt.setVisible(true);
        edytujProdukt.setVisible(false);
        mSzukaj.setDisable(true);
    }

    /**
     * Metoda ta zmienia interfejs na interfejs do edycji produktu
     */
    public void zmianaInterfejsuEdytuj(Produkty produkt) {
        table.setVisible(false);
        dodajProdukt.setVisible(false);
        idProduktu = produkt.getIdProduktu();
        edytujProduktL.setText("Edytujesz produkt o id = " + produkt.getIdProduktu());
        edytujCena.setText(produkt.getCena() + "");
        edytujModel.setText(produkt.getModel());
        edytujNazwa.setText(produkt.getNazwa());
        edytujProducent.setText(produkt.getProducent());
        edytujOpis.setWrapText(true);
        edytujOpis.setText(produkt.getOpis());
        edytujComboBox.setValue(produkt.getIdTypuProduktu());
        edytujProdukt.setVisible(true);
        mSzukaj.setDisable(true);
    }

    /**
     * Metoda ta dodaje produkt do bazy danych
     */
    public void dodajProdukt() {
        Session session = initHibernate.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (!(dodajNazwa.getText().isEmpty() || dodajOpis.getText().isEmpty() || dodajModel.getText().isEmpty() || dodajProducent.getText().isEmpty())) {
            try {
                Typy_produktow typy_produktow = session.createQuery("from Typy_produktow where idTypu=" + dodajComboTypProduktu.getValue().getIdTypu(), Typy_produktow.class).uniqueResult();
                try {
                    String cena = dodajCena.getText();
                    if (cena.indexOf(',') > 0) {
                        cena = cena.replace(',', '.');
                    }
                    if(Float.parseFloat(cena)>0) {
                        session.save(new Produkty(dodajNazwa.getText(), dodajModel.getText(), Float.parseFloat(cena), dodajProducent.getText(), dodajOpis.getText(), typy_produktow));
                        transaction.commit();
                        session.close();
                        pokazTabeleProdukty();
                        wyczyscInterfejs();
                    }
                    else{
                        Alert a = new Alert(Alert.AlertType.NONE, "Cena nie może być ujemna lub zerowa", new ButtonType("Zamknij"));
                        a.show();
                    }
                } catch (NumberFormatException e) {
                    Alert a = new Alert(Alert.AlertType.NONE, "Zły format ceny\n\nSpróbuj wpisać ją w sposób ciągły", new ButtonType("Zamknij"));
                    a.show();
                }
            } catch (NullPointerException e) {
                Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź dane!", new ButtonType("Zamknij"));
                a.show();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź dane!", new ButtonType("Zamknij"));
            a.show();
        }
    }

    /**
     * Metoda ta edytuje wybrane produkty w bazie danych
     */
    public void edytujProdukt() {
        Session session = initHibernate.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Produkty zaktualizowane = session.find(Produkty.class, idProduktu);
        String cena = edytujCena.getText();
        if (cena.indexOf(',') > 0) {
            cena = cena.replace(',', '.');
        }
        try {
            if(Float.parseFloat(cena)>0) {
                zaktualizowane.setCena(Float.parseFloat(cena));
                zaktualizowane.setProducent(edytujProducent.getText());
                zaktualizowane.setNazwa(edytujNazwa.getText());
                zaktualizowane.setModel(edytujModel.getText());
                zaktualizowane.setOpis(edytujOpis.getText());
                zaktualizowane.setIdTypuProduktu(edytujComboBox.getValue());
                Produkty doEdycji = session.find(Produkty.class, idProduktu);
                doEdycji.merge(zaktualizowane);
                session.update(doEdycji);
                transaction.commit();
                session.close();
                pokazTabeleProdukty();
                wyczyscInterfejs();
            }
            else {
                Alert a = new Alert(Alert.AlertType.NONE, "Cena nie może być ujemna lub zerowa", new ButtonType("Zamknij"));
                a.show();
            }
        } catch (NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.NONE, "Zły format ceny\n\nSpróbuj wpisać ją w sposób ciągły", new ButtonType("Zamknij"));
            a.show();
        }

    }

}
