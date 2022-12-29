package com.projekt.sceny;

import com.projekt.GUI;
import com.projekt.Tabele.Klienci;
import com.projekt.initHibernate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 *
 * @author Michał Wenc
 * @version 1.0
 */
public class Rejestracja implements Initializable {

    private Session session;
    private Transaction transaction;
    @FXML
    private TextField adres;
    @FXML
    private Button bzarejestruj;
    @FXML
    private DatePicker dataUrodzenia;
    @FXML
    private TextField email;
    @FXML
    private TextField haslo;
    @FXML
    private TextField imie;
    @FXML
    private TextField kodPocztowy;
    @FXML
    private TextField login;
    @FXML
    private TextField nazwisko;

    /**
     * Metoda ta wywołuje metode do rejestracji klienta gdy
     * wciśnie on klawisz enter na klawiaturze
     *
     * @param keyEvent Przechowuje dane na temat wciśniętego klawisza na klawiaturze
     */
    @FXML
    private void keyPressedRejestracja(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            RejestracjaKlienta();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate localDate = LocalDate.of(LocalDate.now().getYear() - 18, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        this.dataUrodzenia.setValue(localDate);
    }

    /**
     * Metoda ta zmienia scene na scene z logowaniem
     *
     * @param actionEvent
     */
    public void zmianaScenyHyperlinkL(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("logowanie.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Stage window = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        window.setScene(scene);
        window.show();
    }

    /**
     * Metoda ta sprawdza wprowadzone dane do pól
     * po prawidłowym wpisaniu wywołuje funkcje dodanieKlienta(klient)
     * oraz zmiania scene na scene logowania
     */
    public void RejestracjaKlienta() {
        LocalDate now = LocalDate.now();
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("logowanie.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            EmailValidator validator = EmailValidator.getInstance();
            if (this.login.getText().length()<6||this.login.getText().length()>30) {
                if(this.login.getText().length()<6) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Login nie może zwierać mniej niż 6 znaków!");
                    a.show();
                }
                else{
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Login nie może zwierać więcej niż 30 znaków!");
                    a.show();
                }
            } else if (this.haslo.getText().length()<6||this.haslo.getText().length()>30) {
                if(this.haslo.getText().length()<6) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Hasło nie może zwierać mniej niż 6 znaków!");
                    a.show();
                }
                else{
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Hasło nie może zwierać więcej niż 30 znaków!");
                    a.show();
                }
            } else if ((!this.imie.getText().matches("[a-zA-Z]{3,20}"))||this.imie.getText().isEmpty()) {
                if (this.imie.getText().isEmpty()){
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj imię!");
                    a.show();
                }else {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Imię\n-nie może zawierać liczb \n-nie może zawierać mniej niż 3 litery \n-nie może zawierać więcej niż 20 liter!");
                    a.show();
                }
            } else if ((!this.nazwisko.getText().matches("[a-zA-Z]{2,30}"))||this.nazwisko.getText().isEmpty()) {
                if (this.nazwisko.getText().isEmpty()){
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj nazwisko!");
                    a.show();
                }else {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Nazwisko\n-nie może zawierać liczb \n-nie może zawierać mniej niż 2 litery\n-nie może zawierać więcej niż 30 liter!");
                    a.show();
                }
            } else if (this.dataUrodzenia.getValue() == null||this.dataUrodzenia.getValue().isAfter(now)) {
                if(this.dataUrodzenia.getValue() == null) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj date urodzenia!");
                    a.show();
                }else {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Niepoprawna data!");
                    a.show();
                }
            } else if (this.adres.getText().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Podaj adres!");
                a.show();
            } else {
                if (validator.isValid(this.email.getText()) && this.kodPocztowy.getText().matches("[0-9]{2}[-]([0-9]){3}")) {
                    Klienci klient = new Klienci(this.login.getText(), this.haslo.getText(), this.imie.getText(), this.nazwisko.getText(), this.email.getText(), Date.valueOf(this.dataUrodzenia.getValue()), this.adres.getText(), this.kodPocztowy.getText());
                    if (dodanieKlienta(klient)) {
                        Stage window = (Stage) bzarejestruj.getScene().getWindow();
                        window.setScene(scene);
                        window.show();
                        Alert a = new Alert(Alert.AlertType.INFORMATION, "Poprawnie stworzono konto");
                        a.show();
                    }
                } else if(!validator.isValid(this.email.getText())){
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Nieprawidłowy email!");
                    a.show();
                }else{
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Nieprawidłowy kod pocztowy!");
                    a.show();
                }
            }
        } catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Nie otwarto sesji!");
            a.show();
        }

    }

    /**
     * Metoda ta dodaje Klienta do bazy danych podanej w pliku hibernate.cfg.xml
     * Metoda sprawdza również czy w bazie nie ma juz użytkownika z podanym loginem
     * lub czy podany login to nie admin
     *
     * @param klient Przechowuje obiekt klienta który ma zostać dodany do bazy danych
     * @return Zwracany jest typ boolean w zalezności od porawności podanych kryteriów
     */
    public boolean dodanieKlienta(Klienci klient) {
        session = initHibernate.sessionFactory.openSession();
        if (!session.createQuery("from Klienci where login='" + this.login.getText() + "'").getResultList().isEmpty()) {
            session.close();
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Taki login juz istnieje!");
            a.show();
            return false;
        } else if (!session.createQuery("from Klienci where login='admin'").getResultList().isEmpty()) {
            session.close();
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Nie można użyć takiego loginu!");
            a.show();
            return false;
        } else {
            try {
                transaction = session.beginTransaction();
                session.save(klient);
                session.getTransaction().commit();
                session.close();
            } catch (RuntimeException runtimeException) {
                transaction.rollback();
                System.out.println("Problem z zapisem danych do bazy");
                return false;
            } finally {
                if (session != null) {
                    session.close();
                }
            }
            return true;
        }
    }

}
