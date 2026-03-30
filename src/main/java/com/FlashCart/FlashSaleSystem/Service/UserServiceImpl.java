package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.UserDTO;
import com.FlashCart.FlashSaleSystem.ErrorControl.APIException;
import com.FlashCart.FlashSaleSystem.Models.User;
import com.FlashCart.FlashSaleSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public String signUp(UserDTO userDTO) {
        //check if user already exits or not
        User existingUser  = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser!=null) throw new APIException("User with email -"+userDTO.getEmail()+" already exists");
        //convert Dto to entity
        User user = modelMapper.map(userDTO,User.class);
        // save the user to repository
        userRepository.save(user);

        return "User with email -"+userDTO.getEmail()+" successfully Registered";
    }
}
