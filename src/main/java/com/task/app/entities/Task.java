package com.task.app.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private LocalDate modificationDate;

    public Task(String name, String description, LocalDate modificationDate) {
        this.name = name;
        this.description = description;
        this.modificationDate = modificationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }
}
