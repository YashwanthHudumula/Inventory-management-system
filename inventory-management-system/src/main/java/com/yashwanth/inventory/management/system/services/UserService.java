package com.yashwanth.inventory.management.system.services;

import com.yashwanth.inventory.management.system.dtos.LoginRequest;
import com.yashwanth.inventory.management.system.dtos.RegisterRequest;
import com.yashwanth.inventory.management.system.dtos.Response;
import com.yashwanth.inventory.management.system.dtos.UserDTO;
import com.yashwanth.inventory.management.system.models.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}
