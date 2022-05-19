package com.example.testrestfulwebservice.service;

import com.example.testrestfulwebservice.model.entity.UserEntity;
import com.example.testrestfulwebservice.model.service.UserServiceModel;

import java.util.List;

public interface UserService {
    UserEntity save(UserEntity userEntity);//?void?

    List<UserEntity> getAll();

    UserEntity getById(Long id);

    UserEntity update(UserEntity userEntity, Long id);//?void?

    void delete(Long id);

    UserEntity registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsernameAndPassword(String username, String password);

    void loginUser(Long id, String username);

    void logout();

    UserServiceModel findById(Long id);

    boolean isNameExists(String username);

    UserEntity findCurrentLoginUserEntity();
}
