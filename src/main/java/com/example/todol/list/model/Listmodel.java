package com.example.todol.list.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import com.example.todol.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listmodel {
    @Id
    private String id;
    @Column(nullable = false)
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private float positionorder;
    
    @Column(nullable = false)
    private String userId;

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = IdGenerator.generateRandomId(8);
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}