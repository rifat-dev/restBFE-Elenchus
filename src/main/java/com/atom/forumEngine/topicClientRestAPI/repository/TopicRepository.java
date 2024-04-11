package com.atom.forumEngine.topicClientRestAPI.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atom.forumEngine.topicClientRestAPI.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, UUID> {

}
