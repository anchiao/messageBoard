package com.anchiao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anchiao.entity.security.UserRole;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {

}
