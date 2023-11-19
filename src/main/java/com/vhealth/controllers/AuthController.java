package com.vhealth.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.vhealth.dto.CloudBaseException;
import com.vhealth.dto.ResponseCode;
import com.vhealth.exception.TokenRefreshException;
import com.vhealth.models.RefreshToken;
import com.vhealth.models.Role;
import com.vhealth.models.User;
import com.vhealth.payload.request.LogOutRequest;
import com.vhealth.payload.request.LoginRequest;
import com.vhealth.payload.request.SignupRequest;
import com.vhealth.payload.request.TokenRefreshRequest;
import com.vhealth.payload.response.JwtResponse;
import com.vhealth.payload.response.MessageResponse;
import com.vhealth.payload.response.TokenRefreshResponse;
import com.vhealth.repository.RoleRepository;
import com.vhealth.repository.UserRepository;
import com.vhealth.security.jwt.JwtUtils;
import com.vhealth.security.services.RefreshTokenService;
import com.vhealth.security.services.UserDetailsImpl;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth/")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	/*
	 * @Autowired RestTemplate restTemplate;
	 */
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		System.out.println("AuthController.authenticateUser()");
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			System.out.println("AuthController.authenticateUser() step 2");
			SecurityContextHolder.getContext().setAuthentication(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			String jwt = jwtUtils.generateJwtToken(userDetails);

			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			// RefreshToken refreshToken =
			// refreshTokenService.createRefreshToken(userDetails.getId());

			return ResponseEntity.ok(new JwtResponse(jwt, "", userDetails.getId(), userDetails.getUsername(),
					userDetails.getEmail(), roles));
		} catch (DisabledException e) {
			throw new CloudBaseException(ResponseCode.DEACTIVATED_USER);
		} catch (BadCredentialsException e) {
			System.out.println("AuthController.authenticateUser()");
			// e.printStackTrace();
			throw new CloudBaseException(ResponseCode.BAD_CREDENTIALS);
		} catch (InternalAuthenticationServiceException e) {
			System.out.println("AUTHENTICATION ERROR");
			throw new CloudBaseException(ResponseCode.INVALID_USER);
		}

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println("AuthController.registerUser()");
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName("user")
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName("admin")

							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName("mod")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName("user")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String token = jwtUtils.generateTokenFromUsername(user.getUsername());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
		refreshTokenService.deleteByUserId(logOutRequest.getUserId());
		return ResponseEntity.ok(new MessageResponse("Log out successful!"));
	}

	@SuppressWarnings("unused")
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new CloudBaseException(ResponseCode.DEACTIVATED_USER);
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.BAD_CREDENTIALS);
		} catch (InternalAuthenticationServiceException e) {
			System.out.println("AUTHENTICATION ERROR");
			throw new CloudBaseException(ResponseCode.INVALID_USER);
		}
	}

	@GetMapping("/get")
	public String get() {
		
		RestTemplate rrestTemplate=new RestTemplate();
		System.out.println("AuthController.get()");
		
		String response = rrestTemplate.getForObject(
				  "http://localhost:8080/V23Services_Java/selectListOfEmploye",String.class);
		
		
		System.out.println("AuthController.get()"+response);
		
		
		//getRefreshToken();
		

		return "srikanth";
	}
	  @Cacheable(value = "repo")
	private void getRefreshToken() {
System.out.println("AuthController.getRefreshToken() from Db");
		userRepository.findAll();
	}
	  
	  // commit test
	  // commit test2
	  
	// jhfhfgkjggh

}
