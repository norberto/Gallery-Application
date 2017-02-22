package edu.norbertzardin.service;

import edu.norbertzardin.dao.MessageDao;
import edu.norbertzardin.entities.MessageEntity;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("messageService")
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Transactional
    public void createMessage(String message) {

        messageDao.createMessage(new MessageEntity(message));
    }

    public List<MessageEntity> getMessageList(){
        return messageDao.getMessageList();
    }


}
