package com.atom.forumEngine.topicClientRestAPI.repository;

import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.atom.forumEngine.topicClientRestAPI.entity.Message;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByTopicId(UUID topicId);
    Page<Message> findByTopicId(UUID topicId, Pageable pageable);
}
