package pw.io.booker.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import pw.io.booker.exception.CustomException;
import pw.io.booker.repo.AuthenticationRepository;

@Aspect
@Service
public class AuthenticationAspect {
	
	Logger logger = Logger.getLogger(AuthenticationAspect.class);
	
	private AuthenticationRepository authenticationRepository;

	public AuthenticationAspect(AuthenticationRepository authenticationRepository) {
		super();
		this.authenticationRepository = authenticationRepository;
	}
	
	@Before("execution(* pw.io.booker.controller..*(..))")
	public void beforeMethod(JoinPoint joinPoint) {
		
		logger.info("Start of method" + joinPoint.getClass().getName());
	}
	
	@Around("execution(* pw.io.booker.controller..*(..)) && args(token,..)")
	public Object checkAccess(ProceedingJoinPoint jointPoint, String token) throws Throwable {
		
		if (token == null) {
			logger.error("Invalid token.");
			throw new CustomException("Invalid token.");
		}
		
		if (authenticationRepository.findByToken(token) == null) {
			logger.error("Token does not exist.");
			throw new CustomException("Token does not exist.");
		}
		
		return jointPoint.proceed();
	}
	
	@After("execution(* pw.io.booker.controller..*(..))")
	public void afterMethod(JoinPoint joinPoint) {
		
		logger.info("End of method" + joinPoint.getClass().getName());
	}

}
