package com.atom.forumEngine.topicClientRestAPI.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atom.forumEngine.topicClientRestAPI.entity.Message;
import com.atom.forumEngine.topicClientRestAPI.entity.MessageDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.NewTopicDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.OutAllFieldsMessageDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.OutTopicWithMessagesDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.Topic;
import com.atom.forumEngine.topicClientRestAPI.entity.TopicDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.UpdateMessageDTO;
import com.atom.forumEngine.topicClientRestAPI.repository.MessageRepository;
import com.atom.forumEngine.topicClientRestAPI.repository.TopicRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;

    public TopicService(TopicRepository topicRepository, MessageRepository messageRepository) {
        this.topicRepository = topicRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public OutTopicWithMessagesDTO createNewTopic(NewTopicDTO newTopicDTO) {

        String topicName = newTopicDTO.getTopicName();
        MessageDTO messageDTO = newTopicDTO.getMessage();

        if (topicName == null) {
            throw new IllegalArgumentException("Topic name cannot be null");
        }
    
        if (messageDTO == null || messageDTO.getText() == null || messageDTO.getAuthor() == null) {
            throw new IllegalArgumentException("Message and his fields text cannot be null");
        }

        Topic topic = new Topic(topicName);
        topic = this.topicRepository.save(topic);

        String text = messageDTO.getText();
        String author = messageDTO.getAuthor();
        Message message = new Message(topic.getId(), text, author);

        this.messageRepository.save(message);

        List<OutAllFieldsMessageDTO> outAllFieldsMessageDTOList = new ArrayList<>();
        outAllFieldsMessageDTOList.add(new OutAllFieldsMessageDTO(message.getId().toString(), message.getText(), 
                                                                  message.getAuthor(), message.getCreated()));

        return new OutTopicWithMessagesDTO(topic.getId().toString(), topic.getName(), outAllFieldsMessageDTOList);
    }

    @Transactional
    public OutTopicWithMessagesDTO updateTopic(TopicDTO topicDTO, PageRequest pageable) {

        String idString = topicDTO.getId();
        String name = topicDTO.getName();
        
        if (idString == null || name == null) {
            throw new IllegalArgumentException("Topic id and name cannot be null");
        }

        UUID idUUID;
        try {
            idUUID = UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Topic id must be UUID");
        }

        Optional<Topic> topicOpt = this.topicRepository.findById(idUUID);

        if (topicOpt.isEmpty()) {
            throw new NoSuchElementException("Topic not found with id: " + idUUID);
        }

        Topic topic = topicOpt.get();
        topic.setName(name);
        this.topicRepository.save(topic);

        Page<Message> messageList = this.messageRepository.findByTopicId(idUUID, pageable);
        List<OutAllFieldsMessageDTO> outAllFieldsMessageDTOList = new ArrayList<>();

        for (Message messageItem : messageList.getContent()) {
            outAllFieldsMessageDTOList.add(new OutAllFieldsMessageDTO(messageItem.getId().toString(), messageItem.getText(), 
                                                                      messageItem.getAuthor(), messageItem.getCreated()));
        }

        return new OutTopicWithMessagesDTO(topic.getId().toString(), topic.getName(), outAllFieldsMessageDTOList);
    }

    public OutTopicWithMessagesDTO getTopicMessages(UUID idUUID, PageRequest pageable) {
        Optional<Topic> topicOpt = this.topicRepository.findById(idUUID);

        if (topicOpt.isEmpty()) {
            throw new NoSuchElementException("Topic not found with id: " + idUUID);
        }

        Topic topic = topicOpt.get();

        Page<Message> messageList = this.messageRepository.findByTopicId(idUUID, pageable);
        List<OutAllFieldsMessageDTO> outAllFieldsMessageDTOList = new ArrayList<>();

        for (Message messageItem : messageList.getContent()) {
            outAllFieldsMessageDTOList.add(new OutAllFieldsMessageDTO(messageItem.getId().toString(), messageItem.getText(), 
                                                                      messageItem.getAuthor(), messageItem.getCreated()));
        }

        return new OutTopicWithMessagesDTO(topic.getId().toString(), topic.getName(), outAllFieldsMessageDTOList);
    }

    @Transactional
    public OutTopicWithMessagesDTO createNewMessage(UUID idUUID, MessageDTO messageDTO, PageRequest pageable) {
        if (messageDTO == null || messageDTO.getText() == null || messageDTO.getAuthor() == null) {
            throw new IllegalArgumentException("Message and his fields cannot be null");
        }

        Optional<Topic> topicOpt = this.topicRepository.findById(idUUID);

        if (topicOpt.isEmpty()) {
            throw new NoSuchElementException("Topic not found with id: " + idUUID);
        }

        Topic topic = topicOpt.get();

        String text = messageDTO.getText();
        String author = messageDTO.getAuthor();
        
        Message message = new Message(topic.getId(), text, author);
        this.messageRepository.save(message);

        Page<Message> messageList = this.messageRepository.findByTopicId(idUUID, pageable);
        List<OutAllFieldsMessageDTO> outAllFieldsMessageDTOList = new ArrayList<>();

        for (Message messageItem : messageList) {
            outAllFieldsMessageDTOList.add(new OutAllFieldsMessageDTO(messageItem.getId().toString(), messageItem.getText(), 
                                                                      messageItem.getAuthor(), messageItem.getCreated()));
        }

        return new OutTopicWithMessagesDTO(topic.getId().toString(), topic.getName(), outAllFieldsMessageDTOList);
    }

    @Transactional
    public OutTopicWithMessagesDTO updateMessage(UUID idUUID, UpdateMessageDTO updateMessageDTO, PageRequest pageable) {
        if (updateMessageDTO == null || updateMessageDTO.getText() == null 
                                     || updateMessageDTO.getAuthor() == null || updateMessageDTO.getId() == null) {
            throw new IllegalArgumentException("Message and his fields cannot be null");
        }

        Optional<Topic> topicOpt = this.topicRepository.findById(idUUID);

        if (topicOpt.isEmpty()) {
            throw new NoSuchElementException("Topic not found with id: " + idUUID);
        }

        Topic topic = topicOpt.get();

        String idMessageString = updateMessageDTO.getId();
        UUID idMessageUUID;
        try {
            idMessageUUID = UUID.fromString(idMessageString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Message id must be UUID");
        }

        String text = updateMessageDTO.getText();
        String author = updateMessageDTO.getAuthor();

        Optional<Message> messageOpt = this.messageRepository.findById(idMessageUUID);

        if (messageOpt.isEmpty()) {
            throw new NoSuchElementException("Message not found with id: " + idMessageUUID);
        }

        Message message = messageOpt.get();
        message.setAuthor(author);
        message.setText(text);
        this.messageRepository.save(message);

        Page<Message> messageList = this.messageRepository.findByTopicId(idUUID, pageable);
        List<OutAllFieldsMessageDTO> outAllFieldsMessageDTOList = new ArrayList<>();

        for (Message messageItem : messageList) {
            outAllFieldsMessageDTOList.add(new OutAllFieldsMessageDTO(messageItem.getId().toString(), messageItem.getText(), 
                                                                      messageItem.getAuthor(), messageItem.getCreated()));
        }

        return new OutTopicWithMessagesDTO(topic.getId().toString(), topic.getName(), outAllFieldsMessageDTOList);
    }

    public Page<Topic> getAllTopics(Pageable pageable) {
        return this.topicRepository.findAll(pageable);
    }

}
