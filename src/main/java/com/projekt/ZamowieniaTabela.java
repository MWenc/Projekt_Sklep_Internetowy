package com.projekt;

import com.projekt.Tabele.Zamowienia;

/**
 * Klasa ta pomaga zaimplementować dane Zamówienia do TableView
 * @author Michał Wenc
 * @version 1.0
 */
public class ZamowieniaTabela {

    private Integer idZamowienia;
    private String adres;
    private String kodPocztowy;
    private Integer idKlient;
    private Integer idProduktu;
    private String nazwaProduktu;
    private String producentProduktu;

    public ZamowieniaTabela(Zamowienia zamowienia) {
        this.idZamowienia = zamowienia.getIdZamowienia();
        this.adres = zamowienia.getKlient().getAdres();
        this.kodPocztowy = zamowienia.getKlient().getKod_pocztowy();
        this.idKlient = zamowienia.getKlient().getId_klienta();
        this.idProduktu = zamowienia.getIdProduktu().getIdProduktu();
        this.nazwaProduktu = zamowienia.getIdProduktu().getNazwa();
        this.producentProduktu = zamowienia.getIdProduktu().getProducent();
    }

    public Integer getIdZamowienia() {
        return idZamowienia;
    }

    public void setIdZamowienia(Integer idZamowienia) {
        this.idZamowienia = idZamowienia;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getKodPocztowy() {
        return kodPocztowy;
    }

    public void setKodPocztowy(String kodPocztowy) {
        this.kodPocztowy = kodPocztowy;
    }

    public Integer getIdKlient() {
        return idKlient;
    }

    public void setIdKlient(Integer idKlient) {
        this.idKlient = idKlient;
    }

    public Integer getIdProduktu() {
        return idProduktu;
    }

    public void setIdProduktu(Integer idProduktu) {
        this.idProduktu = idProduktu;
    }

    public String getNazwaProduktu() {
        return nazwaProduktu;
    }

    public void setNazwaProduktu(String nazwaProduktu) {
        this.nazwaProduktu = nazwaProduktu;
    }

    public String getProducentProduktu() {
        return producentProduktu;
    }

    public void setProducentProduktu(String producentProduktu) {
        this.producentProduktu = producentProduktu;
    }
}
