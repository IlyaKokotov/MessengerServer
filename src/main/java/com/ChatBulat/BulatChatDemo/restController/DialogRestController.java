package com.ChatBulat.BulatChatDemo.restController;

import com.ChatBulat.BulatChatDemo.customException.ResourceNotFoundException;
import com.ChatBulat.BulatChatDemo.model.Dialog;
import com.ChatBulat.BulatChatDemo.repository.DialogRepository;
import com.ChatBulat.BulatChatDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dialogApi")
public class DialogRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DialogRepository dialogRepository;

    /**
     * Return all dialogs
     *
     * @return List<Dialog></>
     */
    @GetMapping("/getAll")
    public List<Dialog> getAllDialogs() {
        return dialogRepository.findAll();
    }

    /**
     * Get a User's dialogs list. Return List<Dialog>
     *
     * @param id
     * @return List<Dialog></>
     */
    @GetMapping("/getDialogById/{id}")
    public List<Dialog> getUserDialogs(@PathVariable(value = "id") Long id) {
        return userRepository.getOne(id).getDialogs();
    }

    /**
     * Create a dialog
     *
     * @param userIdList
     * @return ResponseEntity<?>
     */
    @PostMapping("/create")
    public ResponseEntity<?> addDialog(@Valid @RequestBody List<Long> userIdList) {
        Dialog dialog = new Dialog();
        for (Long userId : userIdList) {
            userRepository.getOne(userId).addDialog(dialog);
            dialog.addUser(userRepository.getOne(userId));
        }
        dialogRepository.save(dialog);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete dialog by id
     *
     * @param id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDialog(@PathVariable(value = "id") Long id) {
        Dialog dialog = dialogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Dialog", "id", id));
        dialogRepository.delete(dialog);
        return ResponseEntity.ok().build();
    }

    /**
     * Add a User to dialog
     *
     * @param id
     * @param userId
     * @return
     */
    @PutMapping("/{id}")
    public Dialog updateDialog(@PathVariable(value = "id") Long id,
                               @Valid @RequestBody Long userId) {
        Dialog dialog = dialogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Dialog", "id", id));
        dialog.addUser(userRepository.getOne(userId));
        Dialog updateDialog = dialogRepository.save(dialog);
        return updateDialog;
    }
}
