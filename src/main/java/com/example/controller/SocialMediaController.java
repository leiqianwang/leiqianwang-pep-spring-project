package com.example.controller;

import com.example.entity.*;
import com.example.exception.BadRequestException;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController

public class SocialMediaController {

    private final AccountService accountService;
    private  MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
          this.accountService = accountService;
          this.messageService = messageService;
    }

    /**
     * POST endpint for registering a new account  URL: POST /register
     * Request Body: Account object (without accountId)
     */
   @SuppressWarnings("null")
@PostMapping("/register")
   public ResponseEntity<Account> registerAccount(@RequestBody Account newAccount) {
      try {
         Account createdAccount = accountService.register(newAccount);
         return new ResponseEntity<>(createdAccount, HttpStatus.OK);
      } catch (BadRequestException e) {
        if(e.getMessage().contains("Username already exists")) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);      //409 Conflict code
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);     // 400
      }
   }

   /**
    * POST endpoint for user login
     URL:   POST /login
     RequestBody:  Account object (username and password only)
    */

    @SuppressWarnings("null")
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account){
        try {
                Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
                return new ResponseEntity<>(loggedInAccount, HttpStatus.OK);     //200 OK
        } catch(BadRequestException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * POST endpoint for creating a new message
     * URL POST /messages
     * Request Body: Message Object (without messageId)
     */
    @SuppressWarnings("null")
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        try {
            Message createdMessage = messageService.createMessage(newMessage);
            return new ResponseEntity<>(createdMessage, HttpStatus.OK);
        } catch( Exception e) {
             return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);    //400
        }
    }

    //GET endpoint - Response body: List of messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
          List<Message> messages = messageService.getAllMessages();
          return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * Retrieve message by its' messageId
     * GET request to /messages/{messageId}
     * API should be able to retrieve a message by messageId
     * @param messageId
     * @return
     */

    @SuppressWarnings("null")
    @GetMapping("/messages/{messageId}")

    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        if (message.isPresent()) {
           return new ResponseEntity<>(message.get(), HttpStatus.OK); // Return 200 OK with the message
        }
        return new ResponseEntity<>(null, HttpStatus.OK); //return 200 OK with no content if not found

    }

     @SuppressWarnings("null")
    @DeleteMapping("/messages/{messageId}")
     public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
            Integer rowsAffected = messageService.deleteMessageById(messageId);

            if(rowsAffected == 1) {
            return new ResponseEntity<>(rowsAffected, HttpStatus.OK);
          }else {
            return new ResponseEntity<>(null, HttpStatus.OK);
          }
        }


        /**
         * GET endpoint to retrieve all messages by a particular user
         * URL:   /accounts/{accountId}/messages
         */

         @GetMapping("/accounts/{accountId}/messages")
         public ResponseEntity<List<Message>> getMessagesByUserId(@PathVariable Integer accountId) {
            List<Message> messages = messageService.getMessagesByUserId(accountId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
         }

         /**
          * PATCH to update the message text
          Request Body: A JSON Object containing the new updated messageText
          How to extract the messageText value from the request body? 
          Map<T, T>???   Key:"messageUpdated"  Value: the updated text 
          */

          @SuppressWarnings("null")
          @PatchMapping("/messages/{messageId}")
          public ResponseEntity<Integer> updateMessageText(@PathVariable Integer messageId, 
                      @RequestBody Map<String, String> requestBody) {
                        String updatedText = requestBody.get("messageUpdated"); //Change the KEY to match the new key in the JSON

                        try {
                          int rowsUpdated = messageService.updateMessageById(messageId, updatedText);
                          return new ResponseEntity<>(rowsUpdated, HttpStatus.OK);

                        }catch (BadRequestException e) {
                          return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                        }
               
            }
          
}
          
    
 
