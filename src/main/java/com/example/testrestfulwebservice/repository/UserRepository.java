package com.example.testrestfulwebservice.repository;

import com.example.testrestfulwebservice.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    Optional<UserEntity>findByUsername(String username);
}
