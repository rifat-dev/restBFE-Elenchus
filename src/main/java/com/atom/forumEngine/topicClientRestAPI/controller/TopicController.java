package com.atom.forumEngine.topicClientRestAPI.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atom.forumEngine.topicClientRestAPI.entity.MessageDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.NewTopicDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.Topic;
import com.atom.forumEngine.topicClientRestAPI.entity.TopicDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.UpdateMessageDTO;
import com.atom.forumEngine.topicClientRestAPI.entity.OutTopicWithMessagesDTO;
import com.atom.forumEngine.topicClientRestAPI.service.TopicService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/topic")
public class TopicController {
    
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<?> createTopic(@Valid @RequestBody NewTopicDTO newTopicDTO, BindingResult bindingResult) {
        if (newTopicDTO == null || newTopicDTO.getTopicName() == null || newTopicDTO.getMessage() == null 
                                || !(newTopicDTO.getMessage() instanceof MessageDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid newTopicDTO input"); 
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

        try {
            OutTopicWithMessagesDTO outTopicWithMessagesDTO = topicService.createNewTopic(newTopicDTO);
            return ResponseEntity.ok().body(outTopicWithMessagesDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTopic(@Valid @RequestBody TopicDTO topicDTO, BindingResult bindingResult) {
        if (topicDTO == null || topicDTO.getId() == null || topicDTO.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid topicDTO input");
        }
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

        try {
            OutTopicWithMessagesDTO outTopicWithMessagesDTO = topicService.updateTopic(topicDTO, PageRequest.of(0, 3, Sort.by("created").descending()));
            return ResponseEntity.ok().body(outTopicWithMessagesDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Topic>> getAllTopics(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "3") int size) {
        Page<Topic> listAllTopics = topicService.getAllTopics(PageRequest.of(page, size));
        return ResponseEntity.ok().body(listAllTopics);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<?> getListTopicMessages(@PathVariable("topicId") String topicId, 
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "3") int size) {
        if (topicId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UUID idUUID;
        try {
            idUUID = UUID.fromString(topicId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Topic id must be UUID");
        }

        try {
            OutTopicWithMessagesDTO outTopicWithMessagesDTO = topicService.getTopicMessages(idUUID, PageRequest.of(page, size));
            return ResponseEntity.ok().body(outTopicWithMessagesDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{topicId}/message")
    public ResponseEntity<?> createMessage(@PathVariable("topicId") String topicId, 
                                           @Valid @RequestBody MessageDTO messageDTO, BindingResult bindingResult,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size) {
        if (topicId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UUID idUUID;
        try {
            idUUID = UUID.fromString(topicId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Topic id must be UUID");
        }

        if (messageDTO == null || messageDTO.getAuthor() == null || messageDTO.getText() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid messageDTO input");
        }
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

        try {
            OutTopicWithMessagesDTO outTopicWithMessagesDTO = topicService.createNewMessage(idUUID, messageDTO, PageRequest.of(page, size, Sort.by("created").descending()));
            return ResponseEntity.ok().body(outTopicWithMessagesDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{topicId}/message")
    public ResponseEntity<?> updateMessage(@PathVariable("topicId") String topicId,
                                           @Valid @RequestBody UpdateMessageDTO updateMessageDTO, BindingResult bindingResult,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size) {
        if (topicId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UUID idUUID;
        try {
            idUUID = UUID.fromString(topicId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Topic id must be UUID");
        }

        if (updateMessageDTO == null || updateMessageDTO.getText() == null 
                                     || updateMessageDTO.getAuthor() == null || updateMessageDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid updateMessageDTO input");
        }
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
        }

        try {
            OutTopicWithMessagesDTO outTopicWithMessagesDTO = topicService.updateMessage(idUUID, updateMessageDTO, PageRequest.of(page, size, Sort.by("created").descending()));
            return ResponseEntity.ok().body(outTopicWithMessagesDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
