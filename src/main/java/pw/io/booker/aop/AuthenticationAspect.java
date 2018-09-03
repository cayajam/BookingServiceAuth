package pw.io.booker.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import pw.io.booker.exception.CustomException;
import pw.io.booker.repo.AuthenticationRepository;

public class AuthenticationAspect {
	
	private AuthenticationRepository authenticationRepository;

	public AuthenticationAspect(AuthenticationRepository authenticationRepository) {
		super();
		this.authenticationRepository = authenticationRepository;
	}
	
	@Around("execution(* pw.io.booker.controller..*(..) && args(token,..)")
	public Object checkAccess(ProceedingJoinPoint jointPoint, String token) throws Throwable {
		
		if (token == null) {
			throw new CustomException("Invalid token.");
		}
		
		if (authenticationRepository.findByToken(token) == null) {
			throw new CustomException("Token does not exist.");
		}
		
		return jointPoint.proceed();
	}

}
