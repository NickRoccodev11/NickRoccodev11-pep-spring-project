package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.Optional;
import java.util.List;

@Service
public class MessageService {

    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getAllMessagesByAccountId(int accountId){
        return messageRepository.findByPostedBy(accountId);
    }

    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    public Optional<Message> createMessage(Message message) {

        Account existingAccount = accountRepository.findById(message.getPostedBy()).orElse(null);

        if (existingAccount == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
           throw new IllegalArgumentException("Message must be between 1 - 255 characters long. ");
        }

        return Optional.of(messageRepository.save(message));
    }

    public int updateMessage(Message message) {

        Message existingMessage = messageRepository.findById(message.getMessageId()).orElse(null);

        if (existingMessage == null) {
            throw new IllegalArgumentException("Could not find the message you are trying to update");
        }

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message must be between 1 - 255 characters long");
        }

        Message updatedMessage = messageRepository.save(message);

        if( updatedMessage == null){
            throw new IllegalArgumentException("Error updating message");
        }

        return 1;
    }

    public int deleteMessage(int messageId) {

        Message existingMessage = messageRepository.findById(messageId).orElse(null);

        if (existingMessage == null) {
            return 0;
        } else {
            messageRepository.deleteById(messageId);
            return 1;
        }
        
    }


}
