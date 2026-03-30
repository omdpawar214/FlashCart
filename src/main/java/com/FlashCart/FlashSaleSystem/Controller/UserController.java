package com.FlashCart.FlashSaleSystem.Controller;

import com.FlashCart.FlashSaleSystem.DTOs.UserDTO;
import com.FlashCart.FlashSaleSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
