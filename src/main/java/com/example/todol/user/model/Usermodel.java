package com.example.todol.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import com.example.todol.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usermodel {
    @Id
    private String id;
    private String password;
    private String name;
    @Column(unique = true)
    private String email;
    private String role;

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = IdGenerator.generateRandomId(8);
        }
    }
}