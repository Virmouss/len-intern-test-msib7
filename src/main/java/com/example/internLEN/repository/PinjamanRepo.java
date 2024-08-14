package com.example.internLEN.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.internLEN.entity.Pinjaman;

public interface PinjamanRepo extends JpaRepository<Pinjaman, Long> {
}
