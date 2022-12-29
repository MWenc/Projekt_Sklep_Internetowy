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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Michał Wenc
 * @version 1.0
 */
public class Logowanie implements Initializable {

    @FXML
    private Button bzaloguj;

    @FXML
    private PasswordField haslo;

    @FXML
    private TextField login;

    /**
     * Metoda ta wywołuje funkcje zamknij w klassie głównej GUI
     */
    public void zamknij(){
        GUI.zamknij();
    }

    /**
     * Metoda ta wywołuje metode do logowania użytkownika
     * gdy wciśnie on klawisz enter na klawiaturze
     *
     * @param keyEvent Przechowuje dane na temat wciśniętego klawisza na klawiaturze
     */
    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            bLogowanie();
        }
    }

    /**
     * Metoda ta wywołuje sie przy otwarciu sceny
     * aktualnie nie wywołuje ona nic, jest potrzebna by
     * ta klasa mogła implemetować iterfejs Initializable
     *
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Metoda ta zmienia scene na scene z formularzem do rejestracji
     *
     * @param actionEvent
     */
    public void zmianaScenyHyperlinkR(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("rejestracja.fxml"));
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
     * Jest to metoda przez którą odbywa się logowanie do aplikacji, jeżeli nie jest to konto admina to logowanie odbywa się
     * poprzez pobranie z bazy tego klienta który zgadza się z danymi podanymi w kontrolkach login i haslo po czym
     * nastepuje zmiana sceny na scene panelu admina lub panelu klienta
     */
    public void bLogowanie() {

        if (login.getText().equals("admin") && haslo.getText().equals(Admin.hasloa)) {
            FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("admin.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage window = (Stage) bzaloguj.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("panelKlienta.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            panelKlienta panelKlienta = fxmlLoader.getController();

            try {
                Session session = initHibernate.sessionFactory.openSession();
                Query zapytanie = session.createQuery("from Klienci where login='" + login.getText() + "' AND haslo='" + haslo.getText() + "'");
                Klienci klienci = (Klienci) zapytanie.uniqueResult();
                panelKlienta.setIdKlienta(klienci.getId_klienta());
                session.close();
                Stage window = (Stage) bzaloguj.getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (NullPointerException e) {
                Alert a = new Alert(Alert.AlertType.NONE, "Nie znaleziono użytkownika!\n", new ButtonType("Zamknij"));
                a.show();
            }
        }
    }

}

