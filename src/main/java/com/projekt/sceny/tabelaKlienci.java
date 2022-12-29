package com.projekt.sceny;


import com.projekt.GUI;
import com.projekt.LocalDateConverter;
import com.projekt.Tabele.Klienci;
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
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


/**
 *
 * @author Michał Wenc
 * @version 1.0
 */
public class tabelaKlienci implements Initializable {

    private static final ObservableList<Klienci> lk = FXCollections.observableArrayList();
    private String s = "";
    private Integer idEdytowanegoKlienta;
    @FXML
    private TableColumn<?, ?> adresCol;
    @FXML
    private Label dataPickerL;
    @FXML
    private TableColumn<Klienci, Date> dataUrodzeniaCol;
    @FXML
    private DatePicker doWyszukaniaDP;
    @FXML
    private TextField doWyszukiwaniaF;
    @FXML
    private Label doWyszukiwaniaL;
    @FXML
    private TextField edytujAdres;
    @FXML
    private DatePicker edytujDataUrodzenia;
    @FXML
    private TextField edytujEmail;
    @FXML
    private TextField edytujHaslo;
    @FXML
    private TextField edytujImie;
    @FXML
    private VBox edytujKlienta;
    @FXML
    private Label edytujKlientaL;
    @FXML
    private TextField edytujKodPocztowy;
    @FXML
    private TextField edytujLogin;
    @FXML
    private TextField edytujNazwisko;
    @FXML
    private TableColumn<Klienci, String> emailCol;
    @FXML
    private TableColumn<Klienci, String> hasloCol;
    @FXML
    private TableColumn<Klienci, Integer> idKlientaCol;
    @FXML
    private TableColumn<Klienci, String> imieCol;
    @FXML
    private TableColumn<Klienci, String> kodPocztowyCol;
    @FXML
    private TableColumn<Klienci, String> loginCol;
    @FXML
    private Menu mSzukaj;
    @FXML
    private TableColumn<Klienci, String> nazwiskoCol;
    @FXML
    private TableView<Klienci> table;
    @FXML
    private VBox wyszukiwanieBox;
    private TableColumn<Klienci, Void> edytujCol;
    private TableColumn<Klienci, Void> usunCol;

    /**
     * Metoda która wywołuje się po otwarciu przypisanej
     * sceny do tej klasy.Inicjalizuje wartości
     * jakie ma zawierać tabelview zamowień, wywołuje metode
     * pokazTabeleKlienci() na początku otwarcia sceny
     * oraz dodaje 2 kolumny do edycji oraz usuwania rekordów
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pokazTabeleKlienci();
        nazwiskoCol.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        idKlientaCol.setCellValueFactory(new PropertyValueFactory<>("id_klienta"));
        imieCol.setCellValueFactory(new PropertyValueFactory<>("imie"));
        adresCol.setCellValueFactory(new PropertyValueFactory<>("adres"));
        kodPocztowyCol.setCellValueFactory(new PropertyValueFactory<>("kod_pocztowy"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        hasloCol.setCellValueFactory(new PropertyValueFactory<>("haslo"));
        dataUrodzeniaCol.setCellValueFactory(new PropertyValueFactory<>("data_urodzenia"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.usunCol = new TableColumn<>();
        Callback<TableColumn<Klienci, Void>, TableCell<Klienci, Void>> usunCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Klienci, Void> call(final TableColumn<Klienci, Void> param) {
                        final TableCell<Klienci, Void> cell = new TableCell<>() {

                            final Image image = new Image("usun.png");
                            private final Button btn = new Button();

                            {
                                btn.setGraphic(new ImageView(image));
                                btn.setOnAction((ActionEvent event) -> {
                                    try {
                                        Session session = initHibernate.sessionFactory.openSession();
                                        Transaction transaction = session.beginTransaction();
                                        Integer idKlienta = getTableView().getItems().get(getIndex()).getId_klienta();
                                        Klienci klient = session.get(Klienci.class, idKlienta);
                                        session.remove(klient);
                                        transaction.commit();
                                        session.close();
                                        pokazTabeleKlienci();
                                    } catch (PersistenceException e) {
                                        Alert a = new Alert(Alert.AlertType.NONE, "Musisz najpierw usunąć wszystkie zamówienia tego Klienta!", new ButtonType("Zamknij"));
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
        Callback<TableColumn<Klienci, Void>, TableCell<Klienci, Void>> edytujCellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Klienci, Void> call(final TableColumn<Klienci, Void> param) {
                        final TableCell<Klienci, Void> cell = new TableCell<>() {
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
     * Metoda ta zczytuje z bazy danych Klientów
     * i wrzuca je do listy a następnie tą liste do
     * TableView znajdujace sie na bieżacej scenie
     */
    public void pokazTabeleKlienci() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lk.isEmpty()) {
            lk.clear();
        }
        List<Klienci> List = session.createQuery("from Klienci", Klienci.class).list();
        lk.addAll(List);
        session.close();
        table.getItems().setAll(lk);
        table.refresh();
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
     * Metoda ta zmienia scene na scenę z Tabelą Produktów
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
     * Metoda ta w zależności od id przekazanego parametru
     * zmienia interfejs do wyszukiwania
     *
     * @param event Przechowuje dane na temat obiektu który wywołał tą metode
     */
    public void interfejsDoWyszukiwania(ActionEvent event) {
        s = ((MenuItem) event.getSource()).getId();
        if (!s.equals("dacie urodzenia")) {
            this.doWyszukiwaniaL.setText("Wyszukiwanie po " + s);
            this.edytujDataUrodzenia.setVisible(false);
            this.doWyszukiwaniaF.setVisible(true);
            this.wyszukiwanieBox.setDisable(false);
            this.dataPickerL.setVisible(false);
            this.doWyszukaniaDP.setVisible(false);
            this.edytujCol.setVisible(false);
            this.doWyszukiwaniaF.setPromptText("Tu wpisz " + s + " i wciśnij enter");
        } else {
            this.doWyszukiwaniaL.setText("Wyszukiwanie po " + s);
            this.wyszukiwanieBox.setDisable(false);
            this.doWyszukiwaniaF.setVisible(false);
            this.edytujCol.setVisible(false);
            this.doWyszukaniaDP.setVisible(true);
            this.edytujDataUrodzenia.setVisible(true);
            this.dataPickerL.setVisible(true);
        }
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
                case "ID" -> wyszukajPoIdKlienta();
                case "imieniu" -> wyszukajPoImieniu();
                case "nazwisku" -> wyszukajPoNazwisku();
                case "adresie" -> wyszukajPoAdresie();
                case "kodzie pocztowym" -> wyszukajPoKodziePocztowym();
                case "dacie urodzenia" -> wyszukajPoDacieUrodzenia();
            }
        }
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Klienta
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoIdKlienta() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lk.isEmpty()) {
            lk.clear();
        }
        try {
            List<Klienci> List = session.createQuery("from Klienci where id_klienta=" + doWyszukiwaniaF.getText(), Klienci.class).list();
            lk.addAll(List);
            session.close();
            table.setItems(lk);
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
    public void wyszukajPoImieniu() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lk.isEmpty()) {
            lk.clear();
        }
        List<Klienci> List = session.createQuery("from Klienci where imie='" + doWyszukiwaniaF.getText() + "'", Klienci.class).list();
        lk.addAll(List);
        session.close();
        table.setItems(lk);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Klienta
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoNazwisku() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lk.isEmpty()) {
            lk.clear();
        }
        List<Klienci> List = session.createQuery("from Klienci where nazwisko='" + doWyszukiwaniaF.getText() + "'", Klienci.class).list();
        lk.addAll(List);
        session.close();
        table.setItems(lk);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Klienta
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoKodziePocztowym() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lk.isEmpty()) {
            lk.clear();
        }
        List<Klienci> List = session.createQuery("from Klienci where kod_pocztowy='" + doWyszukiwaniaF.getText() + "'", Klienci.class).list();
        lk.addAll(List);
        session.close();
        table.setItems(lk);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Klienta
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoAdresie() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lk.isEmpty()) {
            lk.clear();
        }
        List<Klienci> List = session.createQuery("from Klienci where adres='" + doWyszukiwaniaF.getText() + "'", Klienci.class).list();
        lk.addAll(List);
        session.close();
        table.setItems(lk);
        table.refresh();
        wyczyscInterfejs();
    }

    /**
     * Metoda ta wyszukuje dane w bazie danych w zależności od id Klienta
     * podanego w polu do wyszukiwania
     */
    public void wyszukajPoDacieUrodzenia() {
        Session session = initHibernate.sessionFactory.openSession();
        if (!lk.isEmpty()) {
            lk.clear();
        }
        System.out.println(doWyszukaniaDP.getValue().toString());
        List<Klienci> List = session.createQuery("from Klienci where data_urodzenia='" + doWyszukaniaDP.getValue().toString() + "'", Klienci.class).list();
        lk.addAll(List);
        session.close();
        table.setItems(lk);
        table.refresh();
        wyczyscInterfejs();

    }

    /**
     * Metoda ta zmienia interfejs na interfejs do edycji Klienta
     */
    public void zmianaInterfejsuEdytuj(Klienci k) {
        LocalDateConverter localDateConverter = new LocalDateConverter();
        table.setVisible(false);
        idEdytowanegoKlienta = k.getId_klienta();
        edytujKlientaL.setText("Edytujesz zamówienie o id = " + k.getId_klienta());
        edytujImie.setText(k.getImie());
        edytujNazwisko.setText(k.getNazwisko());
        edytujLogin.setText(k.getLogin());
        edytujHaslo.setText(k.getHaslo());
        edytujEmail.setText(k.getEmail());
        edytujDataUrodzenia.setValue(localDateConverter.convertToEntityAttribute(k.getData_urodzenia()));
        edytujAdres.setText(k.getAdres());
        edytujKodPocztowy.setText(k.getKod_pocztowy());
        edytujKlienta.setVisible(true);
        mSzukaj.setDisable(true);
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
        this.doWyszukaniaDP.setVisible(false);
        this.dataPickerL.setVisible(false);
        this.edytujKlienta.setVisible(false);
        this.doWyszukiwaniaF.setPromptText("");
        this.doWyszukiwaniaF.setText("");
        this.mSzukaj.setDisable(false);
    }

    /**
     * Metoda ta edytuje wybranego klienta w bazie danych
     */
    public void edytujKlienta() {
        LocalDate now = LocalDate.now();
        EmailValidator validator = EmailValidator.getInstance();
        Session session = initHibernate.sessionFactory.openSession();
        if (edytujLogin.getText().length()<6||edytujLogin.getText().length()>30) {
            if(edytujLogin.getText().length()<6) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Login nie może zwierać mniej niż 6 znaków!");
                a.show();
            }
            else{
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Login nie może zwierać więcej niż 30 znaków!");
                a.show();
            }
        } else if (edytujHaslo.getText().length()<6||edytujHaslo.getText().length()>30) {
            if(edytujHaslo.getText().length()<6) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Hasło nie może zwierać mniej niż 6 znaków!");
                a.show();
            }
            else{
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Hasło nie może zwierać więcej niż 30 znaków!");
                a.show();
            }
        } else if ((!edytujImie.getText().matches("[a-zA-Z]{3,20}"))||edytujImie.getText().isEmpty()) {
            if (edytujImie.getText().isEmpty()){
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj imię!");
                a.show();
            }else {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Imię\n-nie może zawierać liczb \n-nie może zawierać mniej niż 3 litery \n-nie może zawierać więcej niż 20 liter!");
                a.show();
            }
        } else if ((!edytujNazwisko.getText().matches("[a-zA-Z]{2,30}"))||edytujNazwisko.getText().isEmpty()) {
            if (edytujNazwisko.getText().isEmpty()){
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj nazwisko!");
                a.show();
            }else {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Nazwisko\n-nie może zawierać liczb \n-nie może zawierać mniej niż 2 litery\n-nie może zawierać więcej niż 30 liter!");
                a.show();
            }
        } else if (edytujDataUrodzenia.getValue() == null||edytujDataUrodzenia.getValue().isAfter(now)) {
            if(edytujDataUrodzenia.getValue() == null) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj date urodzenia!");
                a.show();
            }else {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Niepoprawna data!");
                a.show();
            }
        } else if (edytujAdres.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj adres!");
            a.show();
        } else {
            if (validator.isValid(edytujEmail.getText()) && edytujKodPocztowy.getText().matches("[0-9]{2}[-]([0-9]){3}")) {
                Transaction transaction = session.beginTransaction();
                Klienci zaktualizowane = session.find(Klienci.class, idEdytowanegoKlienta);
                zaktualizowane.setLogin(edytujLogin.getText());
                zaktualizowane.setNazwisko(edytujNazwisko.getText());
                zaktualizowane.setAdres(edytujAdres.getText());
                zaktualizowane.setData_urodzenia(java.sql.Date.valueOf(edytujDataUrodzenia.getValue()));
                zaktualizowane.setKod_pocztowy(edytujKodPocztowy.getText());
                zaktualizowane.setEmail(edytujEmail.getText());
                zaktualizowane.setHaslo(edytujHaslo.getText());
                zaktualizowane.setImie(edytujImie.getText());
                Klienci doEdycji = session.find(Klienci.class, idEdytowanegoKlienta);
                doEdycji.merge(zaktualizowane);
                session.update(doEdycji);
                transaction.commit();
                session.close();
                pokazTabeleKlienci();
                wyczyscInterfejs();
            } else if(!validator.isValid(edytujEmail.getText())){
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Nieprawidłowy email!");
                a.show();
            }else{
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Nieprawidłowy kod pocztowy!");
                a.show();
            }
        }

    }

}

