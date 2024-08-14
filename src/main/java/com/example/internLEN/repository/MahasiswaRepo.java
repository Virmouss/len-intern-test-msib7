package com.example.internLEN.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.internLEN.entity.Mahasiswa;

public interface MahasiswaRepo extends JpaRepository<Mahasiswa, Long> {
}
