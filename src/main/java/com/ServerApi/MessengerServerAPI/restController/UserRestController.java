package com.ServerApi.MessengerServerAPI.restController;

import com.ServerApi.MessengerServerAPI.customException.ResourceIsAlreadyExistException;
import com.ServerApi.MessengerServerAPI.customException.ResourceNotFoundException;
import com.ServerApi.MessengerServerAPI.model.User;
import com.ServerApi.MessengerServerAPI.repository.DialogRepository;
import com.ServerApi.MessengerServerAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/userApi")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DialogRepository dialogRepository;

    /**
     * Получить всех пользователей
     *
     * @return List<User></>
     */
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Создать нового пользователя
     *
     * @param user
     * @return ResponseEntity<?>
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        boolean userIsAlreadyCreate = true;

        for (User userIterator : userRepository.findAll()) {
            if (userIterator.getLogin().equals(user.getLogin())) {
                throw new ResourceIsAlreadyExistException
                        ("User", "id", userRepository.findById(userIterator.getUserId()));
            }
        }
        userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getUserId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Получить юзера по айдишнику
     *
     * @param id
     * @return User
     */
    @GetMapping("/findById/{id}")
    public User getUserById(@PathVariable(value = "id") Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    /**
     * Обновить юзера
     *
     * @param id
     * @param userDetails
     * @return ResponseEntity<?>
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long id,
                                        @Valid @RequestBody User userDetails) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setLogin(userDetails.getLogin());
        user.setPassword(userDetails.getPassword());
        User updatedUser = userRepository.save(user);
        return ResponseEntity.noContent().build();

    }

    /**
     * Удалить юзера по айдишнику
     *
     * @param id
     * @return ResponseEntity<?>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    /**
     * Проверить по логину, существует ли такой юзер
     *
     * @param login
     * @return ResponseEntity<?>
     */
    @GetMapping("/isUserExist/{login}")
    public ResponseEntity<?> isUserExist(@PathVariable(value = "login") String login) {
        for (User user : userRepository.findAll()) {
            if (user.getLogin().equals(login)) {
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Проверить правильность введённого пароля
     *
     * @param user
     * @return ResponseEntity<?>
     */
    @PostMapping("/passwordIsCorrect")
    public ResponseEntity<?> passwordIsCorrect(@Valid @RequestBody User user) {
        boolean passwordIsCorrect = false;
        for (User userCheck : userRepository.findAll()) {
            if (userCheck.getLogin().equals(user.getLogin())) {
                if (userCheck.getPassword().equals(user.getPassword())) {
                    passwordIsCorrect = true;
                }
                break;
            }
        }
        return ResponseEntity.ok().build();
    }

}