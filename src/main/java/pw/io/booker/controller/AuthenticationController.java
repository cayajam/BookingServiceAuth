package pw.io.booker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import pw.io.booker.model.Authentication;
import pw.io.booker.model.Customer;
import pw.io.booker.repo.AuthenticationRepository;
import pw.io.booker.repo.CustomerRepository;
import pw.io.booker.util.TokenCreator;

@RestController
public class AuthenticationController {

	private AuthenticationRepository authenticationRepository;
	private CustomerRepository customerRepository;
	private TokenCreator tokenCreator;

	public AuthenticationController(AuthenticationRepository authenticationRepository, TokenCreator tokenCreator,
			CustomerRepository customerRepository) {
		super();
		this.authenticationRepository = authenticationRepository;
		this.tokenCreator = tokenCreator;
		this.customerRepository = customerRepository;
	}
	
	@GetMapping("/login")
	public List<Authentication> findAll() {
		return (List<Authentication>) authenticationRepository.findAll();
	}


	@PostMapping("/login")
	public String findByUsernameAndPassword(@RequestBody Customer customerReq) {		
		Customer customer = customerRepository.findByUsernameAndPassword(customerReq.getUsername(),
				customerReq.getPassword());
		String token = "";
		Authentication authentication;
		if (customer != null) {
			authentication = authenticationRepository.findByCustomer(customer);
			if (authentication != null) {
				authenticationRepository.delete(authentication);
			}
			authentication = new Authentication();
			token = tokenCreator.encode(customer);
			authentication.setToken(token);
			authentication.setCustomer(customer);
			authenticationRepository.save(authentication);
		}
		return token;
	}
	
	@PostMapping("/logout")
	public void deleteTokenOnLogOut(@RequestHeader("token") String token) {		
		if (authenticationRepository.findByToken(token) != null) {
			authenticationRepository.delete(authenticationRepository.findByToken(token));
		} 
	}
	
}
