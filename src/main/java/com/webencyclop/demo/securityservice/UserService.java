package com.webencyclop.demo.securityservice;

import com.webencyclop.demo.model.User;

public interface UserService {

	public void saveUser(User user);
	
	public boolean isUserAlreadyPresent(User user);
}
