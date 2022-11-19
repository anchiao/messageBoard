package com.anchiao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.anchiao.entity.User;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(@Param("username") String name);
	
	//=http://localhost:8081/user/search/findByEmail
	User findByEmail (@Param("email") String email);
}
