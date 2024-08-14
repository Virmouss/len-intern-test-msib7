package com.example.internLEN.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.plaf.TreeUI;

import io.grpc.InternalMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Pinjaman { // Renamed class to follow PascalCase

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Mahasiswa mahasiswa; 

    @ManyToOne
    private Buku buku; 

    @Column(nullable = false)
    private LocalDate tglPinjam;

    @Column(nullable = false)
    private LocalDate tglBatasanPengembalian;

    @Column(nullable = true)
    private LocalDate tglPengembalian;

    @Column(nullable = true)
    private BigDecimal denda;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa; 
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa; 
    }

    public Buku getBuku() {
        return buku; 
    }

    public void setBuku(Buku buku) {
        this.buku = buku; 
    }

    public LocalDate getTglPinjam() { 
        return tglPinjam;
    }

    public void setTglPinjam(LocalDate tglPinjam) { 
        this.tglPinjam = tglPinjam;
    }

    public LocalDate getTglBatasanPengembalian() { 
        return tglBatasanPengembalian;
    }

    public void setTglBatasanPengembalian(LocalDate tglBatasanPengembalian) { 
        this.tglBatasanPengembalian = tglBatasanPengembalian;
    }

    public LocalDate getTglPengembalian() { 
        return tglPengembalian;
    }

    public void setTglPengembalian(LocalDate tglPengembalian) { 
        this.tglPengembalian = tglPengembalian;
    }

    public BigDecimal getDenda(){
        return denda;
    }
    public void setDenda(BigDecimal denda){
        this.denda = denda;
    }
    public BigDecimal calculateDenda(BigDecimal fineRatePerDay) {
        if (tglPengembalian != null && tglPengembalian.isAfter(tglBatasanPengembalian)) {
            long daysLate = ChronoUnit.DAYS.between(tglBatasanPengembalian, tglPengembalian);
            return fineRatePerDay.multiply(BigDecimal.valueOf(daysLate));
        }
        return BigDecimal.ZERO;
    }
}
