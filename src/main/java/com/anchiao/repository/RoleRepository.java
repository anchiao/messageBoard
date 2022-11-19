package com.anchiao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anchiao.entity.security.Role;


public interface RoleRepository extends MongoRepository<Role, String>{

}
