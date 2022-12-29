package com.projekt.Tabele;

import javax.persistence.*;

/**
 * Klasa mapująca wygląd tabeli Zamowienia w Bazie danych
 * @author Michał Wenc
 * @version 1.0
 */
@Entity
@Table(name = "Zamowienia")
public class Zamowienia {

    private Integer idZamowienia;
    private String adres;
    private String kodPocztowy;
    private Klienci Klient;
    private Produkty idProduktu;

    public Zamowienia(Klienci klient, Produkty produkt) {
        this.adres = klient.getAdres();
        this.kodPocztowy = klient.getKod_pocztowy();
        this.Klient = klient;
        this.idProduktu = produkt;
    }

    public Zamowienia() {

    }

    /**
     * Metoda ta zmienia parametry zamówienia aktualnego na podanego w parametrze
     *
     * @param zam Zamowienie ze zmienionymi parametrami
     */
    public void merge(Zamowienia zam) {
        setKlient(zam.getKlient());
        setIdProduktu(zam.getIdProduktu());
        setIdZamowienia(zam.getIdZamowienia());
        setAdres(zam.getKlient().getAdres());
        setKodPocztowy(zam.getKlient().getKod_pocztowy());
    }

    @Id
    @Column(name = "id_zamowienia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdZamowienia() {
        return idZamowienia;
    }

    public void setIdZamowienia(Integer idZamowienia) {
        this.idZamowienia = idZamowienia;
    }

    @Column(name = "adres", nullable = false)
    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    @Column(name = "kod_pocztowy", nullable = false)
    public String getKodPocztowy() {
        return kodPocztowy;
    }

    public void setKodPocztowy(String kod_pocztowy) {
        this.kodPocztowy = kod_pocztowy;
    }

    @ManyToOne(targetEntity = Klienci.class)
    @JoinColumn(name = "id_klienta", referencedColumnName = "id_klienta", nullable = false)
    public Klienci getKlient() {
        return Klient;
    }

    public void setKlient(Klienci Klient) {
        this.Klient = Klient;
    }

    @ManyToOne(targetEntity = Produkty.class)
    @JoinColumn(name = "id_produktu", referencedColumnName = "id_produktu", nullable = false)
    public Produkty getIdProduktu() {
        return idProduktu;
    }

    public void setIdProduktu(Produkty idProduktu) {
        this.idProduktu = idProduktu;
    }
}
