package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidLoginCredentialsException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;

@RestController
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;

    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {

        Account newAccount = accountService.register(account);

        return new ResponseEntity<>(newAccount, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {

        Account exisitingAccount = accountService.login(account);

        return new ResponseEntity<Account>(exisitingAccount, HttpStatus.OK);

    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {

        List<Message> messages = messageService.getAllMessages();

        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);

    }

    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable("account_id") int accountId) {

        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);

        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);

    }

    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessagebyId(@PathVariable("message_id") int messageId) {

        Message message = messageService.getMessageById(messageId).orElse(null);

        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {

        Message newMessage = messageService.createMessage(message).orElse(null);

        return new ResponseEntity<>(newMessage, HttpStatus.OK);

    }

    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@RequestBody Message message,
            @PathVariable("message_id") int messageId) {

        message.setMessageId(messageId);

        int rowsAffected = messageService.updateMessage(message);

        return new ResponseEntity<>(rowsAffected, HttpStatus.OK);

    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable("message_id") int messageId) {
        int rowsAffected = messageService.deleteMessage(messageId);

        if (rowsAffected == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(rowsAffected, HttpStatus.OK);

    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUsernameAlreadyExistsExcption(UsernameAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidLoginCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleInvalidLoginCredentialsException(InvalidLoginCredentialsException e) {
        return e.getMessage();
    }

}
