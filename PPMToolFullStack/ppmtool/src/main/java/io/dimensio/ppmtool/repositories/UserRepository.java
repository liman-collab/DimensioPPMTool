package io.dimensio.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.dimensio.ppmtool.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	User findByUsername(String username);
	User getById(Long id);//nese e bojm findById duhet me bo Optional<User>...
	
	
	
}
