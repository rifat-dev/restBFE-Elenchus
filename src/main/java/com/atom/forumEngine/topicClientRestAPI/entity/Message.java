package com.atom.forumEngine.topicClientRestAPI.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "message_h2table")
public class Message {

    @Id
    private UUID id;
    private UUID topicId;
    @NotBlank(message = "Поле text не может быть пустым")
    private String text;
    @NotBlank(message = "Поле author не может быть пустым")
    @Pattern(regexp = "^[\\p{L}\\-\\_\\ ]{3,25}$", message = "Поле author может содержать только буквы, -, _ и отступы.")
    private String author;
    private String created;

    public Message() {
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public Message(UUID topicId, String text, String author) {
        this.id = UUID.randomUUID();
        this.topicId = topicId;
        this.text = text;
        this.author = author;
        this.created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getTopicId() {
        return this.topicId;
    }

    public void setTopicId(UUID topicId) {
        this.topicId = topicId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreated() {
        return this.created;
    }
    
}
