package com.vhealth.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vhealth.mapping.AddressREpo;
import com.vhealth.mapping.CommentRepository;
import com.vhealth.mapping.OrderRepo;
import com.vhealth.mapping.Post;
import com.vhealth.mapping.PostRepository;

@RestController
public class MappingController {
	@Autowired
	OrderRepo orderRepo;
	@Autowired
	AddressREpo addressRepo;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	PostRepository postRepository;

	@PostMapping("/oneToMany")
	public String save(@RequestBody Post post) {

//		 Post post = new Post("one to many mapping using JPA and hibernate", "one to many mapping using JPA and hibernate");
//
//	        Comment comment1 = new Comment("Very useful");
//	        Comment comment2 = new Comment("informative");
//	        Comment comment3 = new Comment("Great post");
//
//	        post.getComments().add(comment1);
//	        post.getComments().add(comment2);
//	        post.getComments().add(comment3);

		this.postRepository.save(post);
		return "";
	}

	@GetMapping("/getOneTomanyData")
	public ResponseEntity<?> get() {

		return new ResponseEntity<>(postRepository.findAll(), HttpStatus.ACCEPTED);
	}

	public static void main(String[] args) {
		Integer numbers[] = {22, 89, 1, 32, 19, 5};
		//Parallel Sort method for sorting int array
		Arrays.sort(numbers);
		
		System.out.println("MappingController.main()"+numbers[4]);
		//converting the array to stream and displaying using forEach
	}

}
