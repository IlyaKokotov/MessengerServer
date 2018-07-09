package com.ServerApi.MessengerServerAPI.restController;

import com.ServerApi.MessengerServerAPI.customException.ResourceNotFoundException;
import com.ServerApi.MessengerServerAPI.model.Dialog;
import com.ServerApi.MessengerServerAPI.repository.DialogRepository;
import com.ServerApi.MessengerServerAPI.repository.UserRepository;
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
     * Получить все диалоги
     *
     * @return List<Dialog></>
     */
    @GetMapping("/getAll")
    public List<Dialog> getAllDialogs() {
        return dialogRepository.findAll();
    }

    /**
     * Получить диалоги пользователя
     *
     * @param id
     * @return List<Dialog></>
     */
    @GetMapping("/getDialogById/{id}")
    public List<Dialog> getUserDialogs(@PathVariable(value = "id") Long id) {
        return userRepository.getOne(id).getDialogs();
    }

    /**
     * Создать диалог
     *
     * @param userIdList
     * @return ResponseEntity<?>
     */
    @PostMapping
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
     * Удалить пользователя по айдишнику
     *
     * @param id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDialog(@PathVariable(value = "id") Long id) {
        Dialog dialog = dialogRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Dialog", "id", id));
        dialogRepository.delete(dialog);
        return ResponseEntity.ok().build();
    }

    /**
     * Добавить пользователя к диалогу. Айдишник пользователя в теле запроса,
     * айдишник диалога - в шапке запроса
     *
     * @param id
     * @param userId
     * @return
     */
    @PutMapping("/{id}")
    public Dialog updateDialog(@PathVariable(value = "id") Long id,
                               @Valid @RequestBody Long userId) {
        Dialog dialog = dialogRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Dialog", "id", id));
        dialog.addUser(userRepository.getOne(userId));
        Dialog updateDialog = dialogRepository.save(dialog);
        return updateDialog;
    }
}
