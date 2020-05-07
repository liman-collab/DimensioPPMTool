package io.dimensio.ppmtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.dimensio.ppmtool.domain.User;
import io.dimensio.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.dimensio.ppmtool.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			//Username has to be unique(excpetion)
			newUser.setUsername(newUser.getUsername());
			//Make sure that password and confirmPassword match
			//We dont persist or show the confirmPassword
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		} catch (Exception e) {
			throw new UsernameAlreadyExistsException("Username '"+ newUser.getUsername()+"' already exists");
		}
		
		
		
	}
	
}
