package pw.io.booker.repo;

import org.springframework.data.repository.CrudRepository;

import pw.io.booker.model.Authentication;
import pw.io.booker.model.Customer;

public interface AuthenticationRepository extends CrudRepository<Authentication, Integer> {
	
	public Authentication findByCustomer(Customer customer);
	
	public Authentication findByToken(String token);
	
}
