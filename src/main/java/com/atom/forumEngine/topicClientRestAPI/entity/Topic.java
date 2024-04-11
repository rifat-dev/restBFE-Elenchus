package com.atom.forumEngine.topicClientRestAPI.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "topic_h2table")
public class Topic {
    
    @Id
    private UUID id;
    @NotBlank(message = "Поле topic name не может быть пустым")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,'!?-]{2,150}$", message = "Поле topic name должен содержать от 2 до 150 символов и может содержать только буквы, цифры, пробелы и следующие специальные символы: . , ' ! ? -.")
    private String name;

    public Topic() {
        this.id = UUID.randomUUID();
    }

    public Topic(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
