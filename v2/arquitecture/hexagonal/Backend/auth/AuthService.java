package [packageName].application.ports.input;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO;


public interface AuthService {
	
	public AuthDTO.Response authentication(AuthDTO.LoginRequest loginRequest);
	
	public String generateToken(Authentication authentication);
	
}
