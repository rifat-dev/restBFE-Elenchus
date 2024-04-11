package com.atom.forumEngine.topicClientRestAPI.entity;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TopicWithMessagesDTO {
    
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Поле id должен быть UUID типа.")
    private String id;
    @NotBlank(message = "Поле topic name не может быть пустым")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,'!?-]{2,150}$", message = "Поле topic name должен содержать от 2 до 150 символов и может содержать только буквы, цифры, пробелы и следующие специальные символы: . , ' ! ? -.")
    private String name;
    @Valid
    private List<MessageDTO> messages;

    public TopicWithMessagesDTO(String id, String name, List<MessageDTO> messages) {
        this.id = id;
        this.name = name;
        this.messages = messages;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MessageDTO> getMessages() {
        return this.messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

}
