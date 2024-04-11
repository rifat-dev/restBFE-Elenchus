package com.atom.forumEngine.topicClientRestAPI.entity;

import java.util.List;

public class OutTopicWithMessagesDTO {
    
    private String id;
    private String name;
    private List<OutAllFieldsMessageDTO> messages;

    public OutTopicWithMessagesDTO(String id, String name, List<OutAllFieldsMessageDTO> messages) {
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

    public List<OutAllFieldsMessageDTO> getMessages() {
        return this.messages;
    }

    public void setMessages(List<OutAllFieldsMessageDTO> messages) {
        this.messages = messages;
    }

}
