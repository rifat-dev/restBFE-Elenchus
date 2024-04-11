package com.atom.forumEngine.topicClientRestAPI.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class NewTopicDTO {
    
    @NotBlank(message = "Поле topicName не может быть пустым")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,'!?-]{2,150}$", message = "Поле topicName должен содержать от 2 до 150 символов и может содержать только буквы, цифры, пробелы и следующие специальные символы: . , ' ! ? -.")
    private String topicName;
    @Valid
    private MessageDTO message;

    public NewTopicDTO(String topicName, MessageDTO message) {
        this.topicName = topicName;
        this.message = message;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public MessageDTO getMessage() {
        return this.message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

}
