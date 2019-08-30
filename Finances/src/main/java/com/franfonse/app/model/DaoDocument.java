package com.franfonse.app.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoDocument extends CrudRepository<Document, Long>{
	
}