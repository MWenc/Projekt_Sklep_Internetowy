package com.projekt.sceny;


import com.projekt.GUI;
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
public class tabelaTypyProduktow implements Initializable {

    private static ObservableList<Typy_produktow> ltp = FXCollections.observableArrayList();
    private TableColumn<Typy_produktow, Void> edytujCol;
    private TableColumn<Typy_produktow, Void> usunCol;
    private Integer idTypuProduktu;
    private String s = "";
    @FXML
    private TextField doWyszukiwaniaF;
    @FXML
    private Label doWyszukiwaniaL;
    @FXML
    private TextField dodajNazwa;
    @FXML
    private VBox dodajTypProduktu;
    @FXML
    private TextField edytujNazwa;
    @FXML
    private Label edytujTypProduktL;
    @FXML
    private VBox edytujTypProduktu;
    @FXML
    private TableColumn<Typy_produktow, Integer> idProduktuCol;
    @FXML
    private TableColumn<Typy_produktow, String> nazwaCol;
    @FXML
    private TableView<Typy_produktow> table;
    @FXML
    private VBox wyszukiwanieBox;
    @FXML
    private Menu mSzukaj;
    @FXML
    private Menu mRekordy;

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
            }
        }
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoId() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!ltp.isEmpty()) {
            ltp.clear();
        }
        try {
            Integer idTypu;
            if (doWyszukiwaniaF.getText().equals("")) {
                idTypu = 0;
            } else {
                idTypu = Integer.parseInt(doWyszukiwaniaF.getText());
            }
            List<Typy_produktow> List = session.createQuery("from Typy_produktow where idTypu =" + idTypu).list();
            ltp = FXCollections.observableArrayList(List);
            session.close();
            table.setItems(ltp);
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
        if (!ltp.isEmpty()) {
            ltp.clear();
        }
        List<Typy_produktow> List = session.createQuery("from Typy_produktow where  nazwa='" + doWyszukiwaniaF.getText() + "'").list();
        ltp = FXCollections.observableArrayList(List);
        session.close();
        table.setItems(ltp);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda która wywołuje się po otwarciu przypisanej
     * sceny do tej klasy.Inicjalizuje wartości
     * jakie ma zawierać tabelview produktów, wywołuje metode
     * pokazTabeleTypyProduktow()
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pokazTabeleTypyProduktow();
        idProduktuCol.setCellValueFactory(new PropertyValueFactory<>("idTypu"));
        nazwaCol.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        this.usunCol = new TableColumn<>();
        Callback<TableColumn<Typy_produktow, Void>, TableCell<Typy_produktow, Void>> usunCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Typy_produktow, Void> call(final TableColumn<Typy_produktow, Void> param) {
                        final TableCell<Typy_produktow, Void> cell = new TableCell<>() {
                            final Image image = new Image("usun.png");
                            private final Button btn = new Button();

                            {
                                btn.setGraphic(new ImageView(image));
                                btn.setOnAction((ActionEvent event) -> {
                                    try {
                                        Session session = initHibernate.sessionFactory.openSession();
                                        Transaction transaction = session.beginTransaction();
                                        Integer idTypuProduktu = getTableView().getItems().get(getIndex()).getIdTypu();
                                        Typy_produktow typy_produktow = session.get(Typy_produktow.class, idTypuProduktu);
                                        session.remove(typy_produktow);
                                        transaction.commit();
                                        session.close();
                                        pokazTabeleTypyProduktow();
                                    } catch (PersistenceException e) {
                                        Alert a = new Alert(Alert.AlertType.NONE, "Usuń najpierw produkty z podanym typem produktu!", new ButtonType("Zamknij"));
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
        Callback<TableColumn<Typy_produktow, Void>, TableCell<Typy_produktow, Void>> edytujCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Typy_produktow, Void> call(final TableColumn<Typy_produktow, Void> param) {
                        final TableCell<Typy_produktow, Void> cell = new TableCell<>() {
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
     * Metoda ta zmienia scene na scenę z Tabelą Zamowienia
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
     * Metoda ta zmienia scene na scenę z tabelą produktów
     */
    public void zmianaScenyTabelaProduktow() {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("tabelaProdukty.fxml"));
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
     * Metoda ta zmienia scene na scenę z tabelą Klienci
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
     * Metoda ta zczytuje z bazy danych typy produkty
     * i wrzuca je do listy a następnie tą liste do
     * TableView znajdujace sie na bieżacej scenie
     */
    public void pokazTabeleTypyProduktow() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!ltp.isEmpty()) {
            ltp.clear();
        }
        List<Typy_produktow> List = session.createQuery("from Typy_produktow ", Typy_produktow.class).list();
        ltp = FXCollections.observableArrayList(List);
        session.close();
        table.setItems(ltp);
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
        this.doWyszukiwaniaL.setText("Wyszukiwanie po " + s);
        this.doWyszukiwaniaF.setVisible(true);
        this.wyszukiwanieBox.setDisable(false);
        this.doWyszukiwaniaF.setPromptText("Tu wpisz " + s + " i wciśnij enter");
        this.mRekordy.setDisable(true);
        this.edytujCol.setVisible(false);
    }

    /**
     * Metoda ta przywraca interfejs do wyszukiwania
     * do domyślnej postaci
     */
    public void wyczyscInterfejs() {
        this.doWyszukiwaniaL.setText("");
        this.wyszukiwanieBox.setDisable(true);
        this.doWyszukiwaniaF.setVisible(true);
        this.table.setVisible(true);
        this.dodajTypProduktu.setVisible(false);
        this.edytujTypProduktu.setVisible(false);
        this.mRekordy.setDisable(false);
        this.mSzukaj.setDisable(false);
        this.edytujCol.setVisible(true);
        this.doWyszukiwaniaF.setPromptText("");
        this.doWyszukiwaniaF.setText("");
        this.dodajNazwa.setText("");
    }

    /**
     * Metoda ta zmienia interfejs na interfejs do dodania produktu
     */
    public void zmianaInterfejsuDodaj() {
        table.setVisible(false);
        edytujTypProduktu.setVisible(false);
        dodajTypProduktu.setVisible(true);
        mSzukaj.setDisable(true);
    }

    /**
     * Metoda ta zmienia interfejs na interfejs do edycji typu produktu
     */
    public void zmianaInterfejsuEdytuj(Typy_produktow typy_produktow) {
        table.setVisible(false);
        idTypuProduktu = typy_produktow.getIdTypu();
        dodajTypProduktu.setVisible(false);
        edytujTypProduktL.setText("Edytujesz produkt o id = " + typy_produktow.getIdTypu());
        edytujNazwa.setText(typy_produktow.getNazwa());
        edytujTypProduktu.setVisible(true);
        mSzukaj.setDisable(true);
    }

    /**
     * Metoda ta dodaje typ produktu do bazy danych
     */
    public void dodajTypProduktu() {
        Session session = initHibernate.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (!(dodajNazwa.getText().isEmpty())) {
            session.save(new Typy_produktow(dodajNazwa.getText()));
            transaction.commit();
            session.close();
            pokazTabeleTypyProduktow();
            wyczyscInterfejs();
        } else {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź dane!", new ButtonType("Zamknij"));
            a.show();
        }
    }

    /**
     * Metoda ta edytuje wybrane typy produkty w bazie danych
     */
    public void edytujTypProduktu() {
        Session session = initHibernate.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Typy_produktow zaktualizowane = session.find(Typy_produktow.class, idTypuProduktu);
        try {
            zaktualizowane.setNazwa(edytujNazwa.getText());
            Typy_produktow doEdycji = session.find(Typy_produktow.class, idTypuProduktu);
            doEdycji.setNazwa(zaktualizowane.getNazwa());
            session.update(doEdycji);
            transaction.commit();
            session.close();
            pokazTabeleTypyProduktow();
            wyczyscInterfejs();
        } catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź dane", new ButtonType("Zamknij"));
            a.show();
        }
    }
}
