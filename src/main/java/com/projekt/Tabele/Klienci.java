package com.projekt.Tabele;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;

/**
 * Klasa mapująca wygląd tabeli Klienci w Bazie danych
 * @author Michał Wenc
 * @version 1.0
 */
@Entity
@Table(name = "Klienci")
public class Klienci {

    private Integer id_klienta;
    private String login;
    private String haslo;
    private String imie;
    private String nazwisko;
    private String email;
    private Date data_urodzenia;
    private String adres;
    private String kod_pocztowy;

    public Klienci(String login, String haslo, String imie, String nazwisko, String email, Date data_urodzenia, String adres, String kod_pocztowy) {
        this.id_klienta = 0;
        this.login = login;
        this.haslo = haslo;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.data_urodzenia = data_urodzenia;
        this.adres = adres;
        this.kod_pocztowy = kod_pocztowy;
    }

    public Klienci() {

    }
    /**
     * Metoda ta zmienia parametry klienta na podanego w parametrze
     *
     * @param k Klient ze zmienionymi parametrami
     */
    public void merge(Klienci k) {
        setId_klienta(k.getId_klienta());
        setLogin(k.getLogin());
        setNazwisko(k.getNazwisko());
        setAdres(k.getAdres());
        setData_urodzenia(k.getData_urodzenia());
        setKod_pocztowy(k.getKod_pocztowy());
        setEmail(k.getEmail());
        setHaslo(k.getHaslo());
        setImie(k.getImie());
    }

    @Override
    public String toString() {
        return id_klienta + "";
    }

    @Id
    @Column(name = "id_klienta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId_klienta() {
        return id_klienta;
    }

    public void setId_klienta(Integer id_klienta) {
        this.id_klienta = id_klienta;
    }

    @Column(name = "login", nullable = false)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "haslo", nullable = false)
    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    @Column(name = "imie", nullable = false)
    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    @Column(name = "nazwisko", nullable = false)
    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "data_urodzenia", nullable = false)
    @Type(type = "date")
    public Date getData_urodzenia() {
        return data_urodzenia;
    }

    public void setData_urodzenia(Date data_urodzenia) {
        this.data_urodzenia = data_urodzenia;
    }

    @Column(name = "adres", nullable = false)
    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    @Column(name = "kod_pocztowy", nullable = false)
    public String getKod_pocztowy() {
        return kod_pocztowy;
    }

    public void setKod_pocztowy(String kod_pocztowy) {
        this.kod_pocztowy = kod_pocztowy;
    }
}
