package edu.norbertzardin.dao;

import edu.norbertzardin.entities.MessageEntity;

import java.util.List;

public interface MessageDao {
    void createMessage(MessageEntity message);

    List<MessageEntity> getMessageList();
}
