package com.atom.forumEngine.topicClientRestAPI.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateMessageDTO {
    
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Поле id должен быть UUID типа.")
    private String id;
    @NotBlank(message = "Поле text не может быть пустым")
    private String text;
    @NotBlank(message = "Поле author не может быть пустым")
    @Pattern(regexp = "^[\\p{L}\\-\\_\\ ]{3,25}$", message = "Поле author может содержать только буквы, -, _ и отступы.")
    private String author;

    public UpdateMessageDTO(String id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

}
