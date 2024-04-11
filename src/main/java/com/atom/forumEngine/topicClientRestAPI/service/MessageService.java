package com.atom.forumEngine.topicClientRestAPI.service;

import java.util.UUID;
import java.util.Optional;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atom.forumEngine.topicClientRestAPI.entity.Message;
import com.atom.forumEngine.topicClientRestAPI.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void deleteMessage(UUID messageIdUUID) {
        Optional<Message> messageOpt = this.messageRepository.findById(messageIdUUID);

        if (messageOpt.isEmpty()) {
            throw new NoSuchElementException("Message not found with id: " + messageIdUUID);
        }

        Message message = messageOpt.get();
        this.messageRepository.delete(message);
    }
    
}
