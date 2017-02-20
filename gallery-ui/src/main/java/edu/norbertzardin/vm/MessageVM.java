package edu.norbertzardin.vm;

import edu.norbertzardin.entities.MessageEntity;
import edu.norbertzardin.service.MessageService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

public class MessageVM {

    private String messageContent;

    @WireVariable
    private MessageService messageService;

    List<MessageEntity> messageList;

    @Init
    public void init(){}

    public void setMessageContent(String message){
        this.messageContent = message;
    }

    public String getMessageContent(){
        return this.messageContent;
    }

    public void setMessageService(MessageService ms){
        this.messageService = ms;
    }

    public MessageService getMessageService(){
        return messageService;
    }

    @Command
    public void doCommand(){
        messageList = messageService.getMessageList();
    }

    @Command
    public void sendMessage(){
        messageService.createMessage(messageContent);
        System.out.println(messageContent);
    }

}
