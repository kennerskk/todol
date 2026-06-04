package com.example.todol.list.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.todol.list.model.Listmodel;

public interface Listrepository extends JpaRepository<Listmodel, String> {
    java.util.List<Listmodel> findByUserId(String userId);
}