package com.projekt;

import com.projekt.Tabele.Produkty;
import com.projekt.Tabele.Typy_produktow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

/**
 * Klasa główna aplikacji.
 * @author Michał Wenc
 * @version 1.0
 */
public class GUI extends Application {

    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda ta ustawia Stage na scene z logowaniem, nadaje tytuł programowi oraz przy zamknięciu
     * programu zamyka sesje z bazą.
     * Implementuje konfiguracje hibernate do aplikacji przez clase initHibernate oraz metode init().
     *
     * @param primaryStage Scena główna programu
     * @throws IOException Gdy nie znajdzie pliku fxml z podaną nazwą
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("logowanie.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        try {
            final Image icon = new Image("icon.png");
            initHibernate.init();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Sklep Internetowy");
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(windowEvent -> {
                try {
                    initHibernate.sessionFactory.close();
                } catch (NullPointerException e) {
                    System.out.println("Nie ma połączenia z bazą danych!");
                }
            });
            primaryStage.show();
            stage=primaryStage;
            przykladowaBazaDanych();
        } catch (RuntimeException e) {
            System.out.println("Nie znalazłem aktywnego serwera z bazą danych MySQL");
            primaryStage.close();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nie znaleziono serwera lokalnego baz danych w MySQL");
            alert.show();
        }

    }

    /**
     * Metoda ta zamyka scene główną aplikacji
     */
    public static void zamknij(){
        try {
            initHibernate.sessionFactory.close();
        } catch (NullPointerException e) {
            System.out.println("Nie ma połączenia z bazą danych!");
        }
        stage.close();
    }

    /**
     * Metoda ta tworzy przykładową baze danych
     */
    public void przykladowaBazaDanych() {
        Session session = initHibernate.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Typy_produktow[] tp = new Typy_produktow[4];
        tp[0] = new Typy_produktow("AGD - artykuły gospodarstwa domowego");
        tp[1] = new Typy_produktow("Artykuły spożywcze");
        tp[2] = new Typy_produktow("Artykuły przemysłowe");
        tp[3] = new Typy_produktow("RTV - elektornika użytkowa");
        session.save(tp[0]);
        session.save(tp[1]);
        session.save(tp[2]);
        session.save(tp[3]);
        Produkty[] produkty = new Produkty[8];
        produkty[0] = new Produkty("Pralka", "Serie 2 WAJ2006EPL", 1218.69F, "Bosch", "Bosch Serie 2 WAJ2006EPL zadowoli swoją funkcjonalnością osoby z napiętym grafikiem. Z tą pralką możesz dokładnie zaplanować pranie i cieszyć się szybkimi efektami.", tp[0]);
        produkty[1] = new Produkty("Suszarka do włosów", "Hydraluxe Pro EC9001", 598F, "Remington", "Z Hydraluxe Pro jedno jest pewne: całkowicie odmienia komfort i jakość suszenia włosów. Pożegnaj się z uporczywym elektryzowaniem, uszkodzeniami termicznymi czy suchością.", tp[0]);
        produkty[2] = new Produkty("Herbata", "Forest Friut 1 szt.", 3F, "RichMont", "RICHMONT to najwyższej jakości herbata stworzona ze starannie wyselekcjonowanych listków. Powstała by cieszyć podniebienia ludzi na całym świecie.", tp[1]);
        produkty[3] = new Produkty("Kawa rozpuszczalna", "Cronat Gold 200g", 19.99F, "Jacobs", "Kawa rozpuszczalna Jacobs Cronat Gold to niezrównany, harmonijny smak i delikatny aromat ukryty w doskonale rozpuszczających się, drobnych ziarenkach. Dzięki niej możesz niemal natychmiast delektować się subtelnym smakiem wszędzie, gdzie tego zapragniesz. Świętuj bezcenne chwile codziennego relaksu z filiżanką aromatycznego naparu Jacobs Cronat Gold!", tp[1]);
        produkty[4] = new Produkty("Fajerwerki", "Wyrzutnia Silent Sound opk.", 64.90F, "Hestia", "25 strzałów,\nkaliber: 26 mm,\nwysokość strzałów: ok. 40 m,\n czas trwania: ok. 27 s,\n ciche – przyjazne zwierzętom", tp[2]);
        produkty[5] = new Produkty("Czajnik", "Diamond 2.4l", 49.99F, "Ambition", "bezpieczny gwizdek – system otwierania w rączce,\npokrywka ułatwia nalewanie wody,\n rączka pokryta powłoką Soft Touch", tp[2]);
        produkty[6] = new Produkty("Telewizor", "QLED QE65Q60AAU", 2999F, "Samsung", "Rozmiar ekranu \t65 cali / 164 cm\n" + "Format HD / Rozdzielczość\t4K UHD / 3840 x 2160\n" + "Częstotliwość odświeżania ekranu\t50 Hz / 60 Hz\n" + "Technologia obrazu \tQLED, LED\n" + "Technologia i format HDR\ttak / HDR10, HDR10+, HLG (Hybrid Log-Gamma)\n" + "Podświetlenie matrycy \tEdge LED (Dual LED)\n" + "Optymalizacja ruchu\tPicture Quality Index 3100", tp[3]);
        produkty[7] = new Produkty("Słuchawki douszne bezprzewodowe", "FreeBuds 4i ANC", 279F, "Huawei", "Produkt ten jest skierowany do osób, które nie boją się eksperymentować i lubią testować najnowsze technologie. Bezprzewodowe słuchawki HUAWEI FreeBuds 4i są wyposażone w wytrzymałą baterię, aktywną redukcję szumów, która zapewnia doskonałą jakość dźwięku,a także niezwykle funkcjonalny design.", tp[3]);
        session.save(produkty[0]);
        session.save(produkty[1]);
        session.save(produkty[2]);
        session.save(produkty[3]);
        session.save(produkty[4]);
        session.save(produkty[5]);
        session.save(produkty[6]);
        session.save(produkty[7]);
        transaction.commit();
        session.close();
    }
}
