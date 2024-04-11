package com.atom.forumEngine.topicClientRestAPI.entity;

public class OutAllFieldsMessageDTO {
    
    private String id;
    private String text;
    private String author;
    private String created;

    public OutAllFieldsMessageDTO(String id, String text, String author, String created) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.created = created;
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

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
