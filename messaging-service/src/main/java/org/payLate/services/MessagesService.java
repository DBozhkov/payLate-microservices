package org.payLate.services;


import jakarta.transaction.Transactional;
import org.payLate.entity.Message;
import org.payLate.repository.MessageRepository;
import org.payLate.requestModels.AdminMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail) {
        Message message = new Message(messageRequest.getUserName(), messageRequest.getTitle(),
                messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void putMessage(AdminMessageRequest adminMessageRequest, String userEmail) throws Exception {
        Optional<Message> message = messageRepository.findById(adminMessageRequest.getId());
        if (!message.isPresent()) {
            throw new Exception("Message not found");
        }

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminMessageRequest.getResponse());
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }
}
