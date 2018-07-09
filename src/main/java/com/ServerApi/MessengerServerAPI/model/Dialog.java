package com.ServerApi.MessengerServerAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "dialogs")
@EntityListeners(AuditingEntityListener.class)
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dialog_id;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE},
            mappedBy = "dialogs")
    private List<User> usersList = new LinkedList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dialog", cascade = CascadeType.ALL)
    private List<Message> messagesList = new LinkedList<>();

    public Dialog() {
    }

    public Long getDialogId() {
        return dialog_id;
    }

    public void setDialogId(Long dialog_Id) {
        this.dialog_id = dialog_Id;
    }

    @JsonIgnore
    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public void addUser(User user) {
        this.usersList.add(user);
    }

    public void removeMessage(Message message) {
        this.messagesList.remove(message);
    }

    public List<Message> getMessagesList() {
        return messagesList;
    }

    public void addMessage(Message message) {
        this.messagesList.add(message);
    }
}
