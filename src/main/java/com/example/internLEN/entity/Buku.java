package com.example.internLEN.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Buku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String Judul;

    @Column(nullable = false)
    private String Penulis;

    @Column(nullable = false)
    private int Kuantitas;

    @Column(nullable = false)
    private String tempatPenyimpanan;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String Judul) {
        this.Judul = Judul;
    }

    public String getPenulis() {
        return Penulis;
    }

    public void setPenulis(String Penulis) {
        this.Penulis = Penulis;
    }

    public int getKuantitas() {
        return Kuantitas;
    }

    public void setKuantitas(int Kuantitas) {
        this.Kuantitas = Kuantitas;
    }

    public String getTempatPenyimpanan() {
        return tempatPenyimpanan;
    }

    public void setTempatPenyimpanan(String tempatPenyimpanan) {
        this.tempatPenyimpanan = tempatPenyimpanan;
    }
}
