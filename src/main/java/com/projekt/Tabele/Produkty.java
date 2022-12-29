package com.projekt.Tabele;

import org.hibernate.annotations.Type;

import javax.persistence.*;
/**
 * Klasa mapująca wygląd tabeli Produkty w Bazie danych
 * @author Michał Wenc
 * @version 1.0
 */
@Entity
@Table(name = "Produkty")
public class Produkty {

    private Integer idProduktu;
    private String nazwa;
    private String model;
    private Float cena;
    private String producent;
    private String opis;
    private Typy_produktow idTypuProduktu;

    public Produkty(String nazwa, String model, Float cena, String producent, String opis, Typy_produktow idTypuProduktu) {
        this.nazwa = nazwa;
        this.model = model;
        this.cena = cena;
        this.producent = producent;
        this.opis = opis;
        this.idTypuProduktu = idTypuProduktu;
    }

    public Produkty() {

    }
    /**
     * Metoda ta zmienia parametry produktu na podanego w parametrze
     *
     * @param produkt Produkt ze zmienionymi parametrami
     */
    public void merge(Produkty produkt) {
        setIdProduktu(produkt.getIdProduktu());
        setNazwa(produkt.getNazwa());
        setModel(produkt.getModel());
        setCena(produkt.getCena());
        setProducent(produkt.getProducent());
        setOpis(produkt.getOpis());
        setIdTypuProduktu(produkt.getIdTypuProduktu());
    }

    @Override
    public String toString() {
        return "Produkty{" +
                "idProduktu=" + idProduktu +
                ", nazwa='" + nazwa + '\'' +
                ", model='" + model + '\'' +
                ", cena=" + cena +
                ", producent='" + producent + '\'' +
                ", idTypuProduktu=" + idTypuProduktu +
                '}';
    }

    @Id
    @Column(name = "id_produktu")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdProduktu() {
        return idProduktu;
    }

    public void setIdProduktu(Integer idProduktu) {
        this.idProduktu = idProduktu;
    }

    @Column(name = "nazwa", nullable = false)
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @Column(name = "model", nullable = false)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "cena", nullable = false)
    public Float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    @Column(name = "producent", nullable = false)
    public String getProducent() {
        return producent;
    }

    public void setProducent(String producent) {
        this.producent = producent;
    }

    @Column(name = "opis", length = 65535, columnDefinition = "TEXT", nullable = false)
    @Type(type = "text")
    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @ManyToOne(targetEntity = Typy_produktow.class)
    @JoinColumn(name = "id_typu", referencedColumnName = "id_typu", nullable = false)
    public Typy_produktow getIdTypuProduktu() {
        return idTypuProduktu;
    }

    public void setIdTypuProduktu(Typy_produktow idTypuProduktu) {
        this.idTypuProduktu = idTypuProduktu;
    }
}
