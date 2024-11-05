package com.example.controller;

import javax.persistence.PostLoad;

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
import com.example.exception.UsernameAlreadyExistsException;
import com.example.service.AccountService;
import com.example.service.MessageService;

import net.bytebuddy.asm.Advice.OffsetMapping.Factory.Illegal;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
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

        if (exisitingAccount == null) {
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }

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

        if (message == null) {
            return new ResponseEntity<Message>(HttpStatus.OK);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {

        Message newMessage = messageService.createMessage(message).orElse(null);

        if (newMessage == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newMessage, HttpStatus.OK);

    }

    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@RequestBody Message message,
            @PathVariable("message_id") int messageId) {

        message.setMessageId(messageId);

        int rowsAffected = messageService.updateMessage(message);

        if (rowsAffected == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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

}
