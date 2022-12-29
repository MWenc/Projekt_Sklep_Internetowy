package com.projekt.Tabele;

import javax.persistence.*;
/**
 * Klasa mapująca wygląd tabeli Typy produktów w Bazie danych
 * @author Michał Wenc
 * @version 1.0
 */
@Entity
@Table(name = "Typy_produktu")
public class Typy_produktow {

    private Integer idTypu;
    private String nazwa;

    public Typy_produktow(String nazwa) {
        this.nazwa = nazwa;
    }

    public Typy_produktow() {
    }

    @Override
    public String toString() {
        return nazwa;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_typu")
    public Integer getIdTypu() {
        return idTypu;
    }

    public void setIdTypu(Integer idTypu) {
        this.idTypu = idTypu;
    }

    public String getNazwa() {
        return nazwa;
    }

    @Column(name = "nazwa", nullable = false)
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

}
