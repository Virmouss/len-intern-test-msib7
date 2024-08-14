package com.example.internLEN.repository;

import com.example.internLEN.entity.Buku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BukuRepo extends JpaRepository<Buku, Long> {
}