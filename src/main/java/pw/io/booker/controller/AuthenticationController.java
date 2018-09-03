package pw.io.booker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pw.io.booker.model.Customer;
import pw.io.booker.repo.AuthenticationRepository;
import pw.io.booker.util.TokenCreator;

@RestController
public class AuthenticationController {
	
	private AuthenticationRepository authenticationRepository;
	private TokenCreator tokenCreator;
	
	public AuthenticationController(AuthenticationRepository authenticationRepository, TokenCreator tokenCreator) {
		super();
		this.authenticationRepository = authenticationRepository;
		this.tokenCreator = tokenCreator;
	}

	@PostMapping("/login")
	public String findByCustomer(@RequestBody Customer customer) {
		String token = null;
		Customer activeCustomer = authenticationRepository.findByCustomer(customer);
			if (activeCustomer != null) {
				 token = tokenCreator.encode(activeCustomer);
			} else {
				System.out.println("Account already exists.");
			}
			return token;
	}

}
