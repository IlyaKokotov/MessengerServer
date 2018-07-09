package com.ServerApi.MessengerServerAPI.restController;

import com.ServerApi.MessengerServerAPI.customException.ResourceNotFoundException;
import com.ServerApi.MessengerServerAPI.model.Dialog;
import com.ServerApi.MessengerServerAPI.model.Message;
import com.ServerApi.MessengerServerAPI.repository.DialogRepository;
import com.ServerApi.MessengerServerAPI.repository.MessageRepository;
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
     * Получить все сообщения
     *
     * @return List<Message>
     */
    @GetMapping
    public List<Message> getDialogMessage() {
        return messageRepository.findAll();
    }

    /**
     * Создать сообщение по айдишнику диалога, а также по тексту в теле запроса
     *
     * @param dialogId
     * @param message
     * @return ResponseEntity<?>
     */
    @PostMapping("/{dialogId}")
    public ResponseEntity<?> createMessage(@PathVariable(value = "dialogId") Long dialogId,
                                           @Valid @RequestBody String message) {
        Message newMessage = new Message(message);
        Dialog dialog = dialogRepository
                .findById(dialogId)
                .orElseThrow(() -> new ResourceNotFoundException("Dialog", "id", dialogId));
        newMessage.setDialog(dialog);
        messageRepository.save(newMessage);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{dialogId}")
                .buildAndExpand(newMessage.getMessageId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Удалить сообщение по айдишнику
     *
     * @param messageId
     * @return ResponseEntity<?>
     */
    @DeleteMapping
    public ResponseEntity<?> deleteMessage(@Valid @RequestBody Long messageId) {
        Message message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Dialog", "id", messageId));
        messageRepository.delete(message);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновить сообщение по айдишнику
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
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedMessage
                        .getMessageId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
