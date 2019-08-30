package com.franfonse.app.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoUser extends CrudRepository<User, Long>{
	
	public User findByUsername(String username);
	public User findByEmail(String email);
}