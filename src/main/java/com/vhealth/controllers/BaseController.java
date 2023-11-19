package com.vhealth.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vhealth.dto.TestDto;
import com.vhealth.models.User;
import com.vhealth.repository.UserRepository;

@RestController
public class BaseController {
	@Autowired

	UserRepository userRepository;

	@GetMapping("/all")
	@Cacheable( "repo")

	public List<User> gt() {
		System.out.println("BaseController.gt()");
		return 	getRefreshToken();

	}
	 List<User> getRefreshToken() {
		System.out.println("AuthController.getRefreshToken() from Db");
		return  userRepository.findAll();
	}
	 @CacheEvict( "repo")
	 public static void clearC() {
		 System.out.println("BaseController.clearC()");
	 }
	 
	@PostMapping("/post")
	public String post(@RequestBody TestDto testDto) {
		System.out.println("BaseController.gt()" + testDto.getPname());

		return "bghkcvhkjn";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String k[]) {
		clearC();
	}
}
