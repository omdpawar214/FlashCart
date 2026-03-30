package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.UserDTO;
import com.FlashCart.FlashSaleSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


    //method to create the user
    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.signUp(userDTO), HttpStatus.CREATED);
    }


    // GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.updateUser(id, userDTO), HttpStatus.OK);
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

}
