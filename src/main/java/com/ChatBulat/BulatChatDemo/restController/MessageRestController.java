package com.ChatBulat.BulatChatDemo.restController;

import com.ChatBulat.BulatChatDemo.customException.ResourceNotFoundException;
import com.ChatBulat.BulatChatDemo.model.Dialog;
import com.ChatBulat.BulatChatDemo.model.Message;
import com.ChatBulat.BulatChatDemo.repository.DialogRepository;
import com.ChatBulat.BulatChatDemo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/messageApi")
public class MessageRestController {

    @Autowired
    DialogRepository dialogRepository;

    @Autowired
    MessageRepository messageRepository;

    /**
     * Get all messages
     *
     * @return List<Message>
     */
    @GetMapping("/getAll")
    public List<Message> getDialogMessage() {
        return messageRepository.findAll();
    }

    /**
     * Create message by dialogId in Path, and String message in request body
     *
     * @param dialogId
     * @param message
     * @return ResponseEntity<?>
     */
    @PostMapping("/{dialogId}")
    public ResponseEntity<?> createMessage(@PathVariable(value = "dialogId") Long dialogId,
                                           @Valid @RequestBody String message) {
        Message newMessage = new Message(message);
        Dialog dialog = dialogRepository.findById(dialogId)
                .orElseThrow(() -> new ResourceNotFoundException("Dialog", "id", dialogId));
        newMessage.setDialog(dialog);
        messageRepository.save(newMessage);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{dialogId}")
                .buildAndExpand(newMessage.getMessageId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Delete message by messageId
     *
     * @param messageId
     * @return ResponseEntity<?>
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMessage(@Valid @RequestBody Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Dialog", "id", messageId));
        messageRepository.delete(message);
        return ResponseEntity.ok().build();
    }

    /**
     * Update message by messageId
     *
     * @param messageId
     * @param newMessageText
     * @return ResponseEntity<?>
     */
    @PutMapping("{messageId}")
    public ResponseEntity<?> updateMessage
    (@PathVariable(value = "messageId") Long messageId,
     @Valid @RequestBody String newMessageText) {

        Message updatedMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        updatedMessage.setMessage(newMessageText);
        messageRepository.save(updatedMessage);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(updatedMessage.getMessageId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
