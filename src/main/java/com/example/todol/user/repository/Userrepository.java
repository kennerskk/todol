package com.example.todol.user.repository;

import com.example.todol.user.model.Usermodel;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Userrepository extends JpaRepository<Usermodel, String> {
    public java.util.Optional<Usermodel> findByEmail(String email);
}