package com.franfonse.app.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoIncome extends CrudRepository<Income, Long>{
}