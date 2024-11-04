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
            return Optional.empty();
        }

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return Optional.empty();
        }

        return Optional.of(messageRepository.save(message));
    }

    public int updateMessage(Message message) {

        Message existingMessage = messageRepository.findById(message.getMessageId()).orElse(null);

        if (existingMessage == null) {
            return 0;
        }

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return 0;
        }

        Message updatedMessage = messageRepository.save(message);

        return updatedMessage != null ? 1 : 0;
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
