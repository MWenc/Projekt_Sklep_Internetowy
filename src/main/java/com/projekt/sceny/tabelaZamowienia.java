package com.projekt.sceny;


import com.projekt.GUI;
import com.projekt.Tabele.Klienci;
import com.projekt.Tabele.Produkty;
import com.projekt.Tabele.Zamowienia;
import com.projekt.ZamowieniaTabela;
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
public class tabelaZamowienia implements Initializable {

    private static final ObservableList<ZamowieniaTabela> lz = FXCollections.observableArrayList();
    private String s = "";
    private Integer idEdytowanegoZamowienia;
    @FXML
    private TableColumn<ZamowieniaTabela, String> adresCol;
    @FXML
    private TextField doWyszukiwaniaF;
    @FXML
    private Label doWyszukiwaniaL;
    @FXML
    private TableColumn<ZamowieniaTabela, Integer> idKlientaCol;
    @FXML
    private TableColumn<ZamowieniaTabela, Integer> idProduktuCol;
    @FXML
    private TableColumn<ZamowieniaTabela, Integer> idZamowieniaCol;
    @FXML
    private TableColumn<ZamowieniaTabela, String> kodPocztowyCol;
    @FXML
    private TableView<ZamowieniaTabela> table;
    @FXML
    private TableColumn<ZamowieniaTabela, String> nazwaProduktuCol;
    @FXML
    private TableColumn<ZamowieniaTabela, String> producentProduktuCol;
    @FXML
    private VBox wyszukiwanieBox;
    @FXML
    private TextField edytujIdKlienta;
    @FXML
    private TextField edytujIdProduktu;
    @FXML
    private VBox edytujZam;
    @FXML
    private Label edytujZamL;
    private TableColumn<ZamowieniaTabela, Void> edytujCol;
    private TableColumn<ZamowieniaTabela, Void> usunCol;
    @FXML
    private VBox dodajZam;
    @FXML
    private Menu mRekordy;
    @FXML
    private Menu mSzukaj;
    @FXML
    private TextField idKlientaZam;
    @FXML
    private TextField idProduktuZam;

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
                case "id klienta" -> wyszukajPoIdKlienta();
                case "id zamowienia" -> wyszukajPoIdZamowienia();
                case "id produktu" -> wyszukajPoIdProduktu();
                case "kodzie pocztowym" -> wyszukajPoKodziePocztowym();
            }
        }
    }

    /**
     * Metoda która wywołuje się po otwarciu przypisanej
     * sceny do tej klasy.Inicjalizuje wartości
     * jakie ma zawierać tabelview zamowień, wywołuje metode
     * pokazTabeleZamowien() na początku otwarcia sceny
     * oraz dodaje 2 kolumny do edycji oraz usuwania rekordów
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pokazTabeleZamowienia();
        idProduktuCol.setCellValueFactory(new PropertyValueFactory<>("idProduktu"));
        idKlientaCol.setCellValueFactory(new PropertyValueFactory<>("idKlient"));
        idZamowieniaCol.setCellValueFactory(new PropertyValueFactory<>("idZamowienia"));
        adresCol.setCellValueFactory(new PropertyValueFactory<>("adres"));
        kodPocztowyCol.setCellValueFactory(new PropertyValueFactory<>("kodPocztowy"));
        nazwaProduktuCol.setCellValueFactory(new PropertyValueFactory<>("nazwaProduktu"));
        producentProduktuCol.setCellValueFactory(new PropertyValueFactory<>("producentProduktu"));
        this.usunCol = new TableColumn<>();
        Callback<TableColumn<ZamowieniaTabela, Void>, TableCell<ZamowieniaTabela, Void>> usunCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<ZamowieniaTabela, Void> call(final TableColumn<ZamowieniaTabela, Void> param) {
                        final TableCell<ZamowieniaTabela, Void> cell = new TableCell<>() {
                            final Image image = new Image("usun.png");
                            private final Button btn = new Button();

                            {
                                btn.setGraphic(new ImageView(image));
                                btn.setOnAction((ActionEvent event) -> {
                                    Session session = initHibernate.sessionFactory.openSession();
                                    Transaction transaction = session.beginTransaction();
                                    Integer idZamowieniau = getTableView().getItems().get(getIndex()).getIdZamowienia();
                                    Zamowienia zamowienia = session.get(Zamowienia.class, idZamowieniau);
                                    session.remove(zamowienia);
                                    transaction.commit();
                                    session.close();
                                    pokazTabeleZamowienia();
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
        Callback<TableColumn<ZamowieniaTabela, Void>, TableCell<ZamowieniaTabela, Void>> edytujCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<ZamowieniaTabela, Void> call(final TableColumn<ZamowieniaTabela, Void> param) {
                        final TableCell<ZamowieniaTabela, Void> cell = new TableCell<>() {
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
     * Metoda ta zmienia scene na scenę z tabelą produkty
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
     * Metoda ta zczytuje z bazy danych zamowienia
     * i wrzuca je do listy a następnie tą liste do
     * TableView znajdujace sie na bieżacej scenie
     */
    public void pokazTabeleZamowienia() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lz.isEmpty()) {
            lz.clear();
        }
        List<Zamowienia> List = session.createQuery("from Zamowienia", Zamowienia.class).list();
        for (Zamowienia zam : List) {
            lz.add(new ZamowieniaTabela(zam));
        }
        session.close();
        table.getItems().setAll(lz);
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
        this.mRekordy.setDisable(true);
        this.doWyszukiwaniaF.setPromptText("Tu wpisz " + s + " i wciśnij enter");
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
        this.dodajZam.setVisible(false);
        this.edytujZam.setVisible(false);
        this.doWyszukiwaniaF.setPromptText("");
        this.doWyszukiwaniaF.setText("");
        this.mRekordy.setDisable(false);
        this.mSzukaj.setDisable(false);
        this.idKlientaZam.setText("");
        this.idProduktuZam.setText("");
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Zamowienia
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoIdZamowienia() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lz.isEmpty()) {
            lz.clear();
        }
        try {
            List<Zamowienia> List = session.createQuery("from Zamowienia where idZamowienia=" + doWyszukiwaniaF.getText() + "", Zamowienia.class).list();
            for (Zamowienia zam : List) {
                lz.add(new ZamowieniaTabela(zam));
            }
            session.close();
            table.setItems(lz);
            table.refresh();
            wyczyscInterfejs();
        } catch (IllegalArgumentException e) {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź liczby", new ButtonType("Zamknij"));
            a.show();
        }
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Klienta
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoIdKlienta() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lz.isEmpty()) {
            lz.clear();
        }
        try {
            List<Zamowienia> List = session.createQuery("from Zamowienia where klient.id_klienta=" + doWyszukiwaniaF.getText() + "", Zamowienia.class).list();
            for (Zamowienia zam : List) {
                lz.add(new ZamowieniaTabela(zam));
            }
            session.close();
            table.setItems(lz);
            table.refresh();
            wyczyscInterfejs();
        } catch (IllegalArgumentException e) {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź liczby", new ButtonType("Zamknij"));
            a.show();
        }
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Produktu
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoIdProduktu() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lz.isEmpty()) {
            lz.clear();
        }
        try {
            List<Zamowienia> List = session.createQuery("from Zamowienia where idProduktu.idProduktu=" + doWyszukiwaniaF.getText() + "", Zamowienia.class).list();
            for (Zamowienia zam : List) {
                lz.add(new ZamowieniaTabela(zam));
            }
            session.close();
            table.setItems(lz);
            table.refresh();
            wyczyscInterfejs();
        } catch (IllegalArgumentException e) {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź liczby", new ButtonType("Zamknij"));
            a.show();
        }
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od Kodzie pocztowym
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoKodziePocztowym() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lz.isEmpty()) {
            lz.clear();
        }
        List<Zamowienia> List = session.createQuery("from Zamowienia where kodPocztowy='" + doWyszukiwaniaF.getText() + "'", Zamowienia.class).list();
        for (Zamowienia zam : List) {
            lz.add(new ZamowieniaTabela(zam));
        }
        session.close();
        table.setItems(lz);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta zmienia interfejs na interfejs do dodania zamówienia
     */
    public void zmianaInterfejsuDodaj() {
        table.setVisible(false);
        dodajZam.setVisible(true);
        edytujZam.setVisible(false);
        mSzukaj.setDisable(true);
    }

    /**
     * Metoda ta zmienia interfejs na interfejs do edycji zamówienia
     */
    public void zmianaInterfejsuEdytuj(ZamowieniaTabela zam) {
        table.setVisible(false);
        idEdytowanegoZamowienia = zam.getIdZamowienia();
        edytujZamL.setText("Edytujesz zamówienie o id = " + zam.getIdZamowienia());
        edytujIdKlienta.setText(zam.getIdKlient() + "");
        edytujIdProduktu.setText(zam.getIdProduktu() + "");
        dodajZam.setVisible(false);
        edytujZam.setVisible(true);
        mSzukaj.setDisable(true);
    }

    /**
     * Metoda ta dodaje zamówienie do bazy danych
     */
    public void dodajZamowienie() {
        Session session = initHibernate.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Klienci klient = session.createQuery("from Klienci where id_klienta=" + idKlientaZam.getText(), Klienci.class).uniqueResult();
            Produkty produkt = session.createQuery("from Produkty where idProduktu=" + idProduktuZam.getText(), Produkty.class).uniqueResult();
            if (klient == null) {
                Alert a = new Alert(Alert.AlertType.NONE, "Nie ma takiego klienta!\n\nUpewnij się sprawdzając tabele klientów", new ButtonType("Zamknij"));
                a.show();
            } else if (produkt == null) {
                Alert a = new Alert(Alert.AlertType.NONE, "Nie ma takiego produktu!\n\nUpewnij się sprawdzając tabele produktów", new ButtonType("Zamknij"));
                a.show();
            } else {
                session.save(new Zamowienia(klient, produkt));
                transaction.commit();
                session.close();
                pokazTabeleZamowienia();
                wyczyscInterfejs();
            }
        } catch (IllegalArgumentException e) {
            Alert a = new Alert(Alert.AlertType.NONE, "Wprowadź liczby", new ButtonType("Zamknij"));
            a.show();
        }
    }

    /**
     * Metoda ta edytuje wybrane zamówienie w bazie danych
     */
    public void edytujZamowienie() {
        Session session = initHibernate.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Klienci klient = session.createQuery("from Klienci where id_klienta=" + edytujIdKlienta.getText(), Klienci.class).uniqueResult();
        Produkty produkt = session.createQuery("from Produkty where idProduktu=" + edytujIdProduktu.getText(), Produkty.class).uniqueResult();
        if (klient == null) {
            Alert a = new Alert(Alert.AlertType.NONE, "Nie ma takiego klienta!\n\nUpewnij się sprawdzając tabele klientów", new ButtonType("Zamknij"));
            a.show();
        } else if (produkt == null) {
            Alert a = new Alert(Alert.AlertType.NONE, "Nie ma takiego produktu!\n\nUpewnij się sprawdzając tabele produktów", new ButtonType("Zamknij"));
            a.show();
        } else {
            Zamowienia zaktualizowane = session.find(Zamowienia.class, idEdytowanegoZamowienia);
            zaktualizowane.setIdProduktu(produkt);
            zaktualizowane.setKlient(klient);
            Zamowienia doEdycji = session.find(Zamowienia.class, idEdytowanegoZamowienia);
            doEdycji.merge(zaktualizowane);
            session.update(doEdycji);
            transaction.commit();
            session.close();
            pokazTabeleZamowienia();
            wyczyscInterfejs();
        }
    }

}

