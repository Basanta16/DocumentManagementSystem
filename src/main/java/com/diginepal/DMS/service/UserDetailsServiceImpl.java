package com.diginepal.DMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.diginepal.DMS.Encrypt.CustomUserDetails;
import com.diginepal.DMS.entity.User;
import com.diginepal.DMS.repo.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user =userRepo.getUserByUserName(username);
		
		if (user==null) {
			throw new UsernameNotFoundException("Couldn't found the user....");
		}
		
		CustomUserDetails customuserdetails = new CustomUserDetails(user);
		
		return customuserdetails;
	}

}
