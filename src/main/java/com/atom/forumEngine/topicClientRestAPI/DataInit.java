package com.atom.forumEngine.topicClientRestAPI;

import java.util.List;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.atom.forumEngine.topicClientRestAPI.entity.Message;
import com.atom.forumEngine.topicClientRestAPI.entity.MessageDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.Topic;
import com.atom.forumEngine.topicClientRestAPI.repository.MessageRepository;
import com.atom.forumEngine.topicClientRestAPI.repository.TopicRepository;

import jakarta.transaction.Transactional;

@Component
public class DataInit implements CommandLineRunner {

    MessageRepository messageRepository;
    TopicRepository topicRepository;

    public DataInit(MessageRepository messageRepository, TopicRepository topicRepository) {
        this.messageRepository = messageRepository;
        this.topicRepository = topicRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        List<MessageDTO> msg1 = new ArrayList<>();
        List<MessageDTO> msg2 = new ArrayList<>();
        List<MessageDTO> msg3 = new ArrayList<>();
        List<MessageDTO> msg4 = new ArrayList<>();
        List<MessageDTO> msg5 = new ArrayList<>();
        List<MessageDTO> msg6 = new ArrayList<>();

        msg1.add(new MessageDTO("This is first message here", "Rifat"));
        msg1.add(new MessageDTO("Hello, I am Bob!", "Bob"));
        msg1.add(new MessageDTO("Hi, I'm Mark!", "Mark"));
        msg1.add(new MessageDTO("Hello!", "Rifat"));
        msg1.add(new MessageDTO("Test from Alice", "Alice"));
        msg1.add(new MessageDTO("I have my topic", "Bob"));

        msg2.add(new MessageDTO("This is topic 2", "Mark"));

        msg3.add(new MessageDTO("Hello I am Bob!", "Bob"));

        msg4.add(new MessageDTO("Hello I am Alice", "Alice"));
        msg4.add(new MessageDTO("Hey, Alice!", "Bob"));

        msg5.add(new MessageDTO("My message here", "Rifat"));

        msg6.add(new MessageDTO("Bobs message here", "Bob"));

        loadTopicAndMessage("Topic number 1", msg1);
        loadTopicAndMessage("Topic number 2", msg2);
        loadTopicAndMessage("Topic number 3", msg3);
        loadTopicAndMessage("Topic number 4", msg4);
        loadTopicAndMessage("Topic number 5", msg5);
        loadTopicAndMessage("Topic number 6", msg6);
    }

    @Transactional
    private void loadTopicAndMessage(String topicName, List<MessageDTO> messages) {
        Topic topic = new Topic(topicName);
        this.topicRepository.save(topic);

        for (MessageDTO messageItem : messages) {
            Message message = new Message(topic.getId(), messageItem.getText(), messageItem.getAuthor());
            this.messageRepository.save(message);
        }
    }
}
