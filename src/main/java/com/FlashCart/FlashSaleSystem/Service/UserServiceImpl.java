package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.UserDTO;
import com.FlashCart.FlashSaleSystem.ErrorControl.APIException;
import com.FlashCart.FlashSaleSystem.ErrorControl.ResourceNotFoundException;
import com.FlashCart.FlashSaleSystem.Models.User;
import com.FlashCart.FlashSaleSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    // GET ALL USERS
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    // GET USER BY ID
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User","UserId",id));

        return modelMapper.map(user, UserDTO.class);
    }

    // UPDATE USER
    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User","UserId",id));

        // update fields
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    // DELETE USER
    @Override
    @Transactional
    public String deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User","UserId",id));

        userRepository.delete(user);

        return "User deleted successfully with id: " + id;
    }
}
