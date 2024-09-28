package com.example.service;

import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    // Constructor should inject repositories, not entities
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * ## 3: Our API should be able to process the creation of new messages.
 
The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a messageId.

- The creation of the message will be successful if and only if the messageText is not blank, is not over 255 characters, 
and postedBy refers to a real, existing user. If successful, the response body should contain a JSON of the message, 
including its messageId. The response status should be 200, which is the default. The new message should be persisted to the database.
- If the creation of the message is not successful, the response status should be 400. (Client error)
     * @param newMessage
     * @return
     */

    
    public Message createMessage(Message newMessage) {
        // Validate message text
        if (newMessage.getMessageText() == null || newMessage.getMessageText().trim().isEmpty()) {
            throw new BadRequestException("Message text cannot be blank.");

        }
        if(newMessage.getMessageText().length() > 255) {
            throw new BadRequestException("Message text cannot exceed 255 characters.");
        }
        //validate the user who posted the message does not exists from that user account 
        if(!accountRepository.existsById(newMessage.getPostedBy())) {
               throw new BadRequestException("The user posting the message does not exist.");
        }
        //Save the message to the database
        return messageRepository.save(newMessage);
    } 

    //Retrives all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    

    public Integer deleteMessageById(Integer messageId) {
          if(messageRepository.existsById(messageId)) {
            // If the message exists, delete it
            messageRepository.deleteById(messageId);
            return 1;
          }
          return 0;   //No row affected if the message did not exist
    }
    
    
}
