package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.UserDTO;

import java.util.List;

public interface UserService {
    String signUp(UserDTO userDTO);
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserDTO userDTO);

    String deleteUser(Long id);
}
