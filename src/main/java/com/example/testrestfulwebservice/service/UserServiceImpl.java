package com.example.testrestfulwebservice.service;

import com.example.testrestfulwebservice.exception.ResourceNotFoundException;
import com.example.testrestfulwebservice.model.entity.LevelEnum;
import com.example.testrestfulwebservice.model.entity.UserEntity;
import com.example.testrestfulwebservice.model.service.UserServiceModel;
import com.example.testrestfulwebservice.repository.UserRepository;
import com.example.testrestfulwebservice.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CurrentUser currentUser;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, CurrentUser currentUser, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return this.userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> getAll() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("UserEntity","Id",id));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        this.userRepository.deleteById(id);
    }

    @Override
    public UserEntity update(UserEntity userEntity, Long id) {
        UserEntity existingEmployee= getById(id);
        existingEmployee.setAge(userEntity.getAge());
        existingEmployee.setEmail(userEntity.getEmail());
        existingEmployee.setFullName(userEntity.getFullName());
        existingEmployee.setPassword(userEntity.getPassword());
        existingEmployee.setUsername(userEntity.getUsername());
        this.userRepository.save(userEntity);
        return existingEmployee;
    }

    @Override
    public UserEntity registerUser(UserServiceModel userServiceModel) {
        UserEntity userEntity = modelMapper.map(userServiceModel, UserEntity.class);
        userEntity.setLevel(LevelEnum.BEGINNER);
        return save(userEntity);
    }

    @Override
    public UserServiceModel findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password)
                .map(user -> modelMapper.map(user,UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public void loginUser(Long id, String username) {
        currentUser.setUsername(username);
        currentUser.setId(id);
    }

    @Override
    public void logout() {
        currentUser.setId(null);
        currentUser.setUsername(null);
    }

    @Override
    public UserServiceModel findById(Long id) {
        return userRepository.findById(id)
                .map(user->modelMapper.map(user,UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public boolean isNameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity findCurrentLoginUserEntity() {
        return userRepository.findById(currentUser.getId()).orElse(null);
    }
}
