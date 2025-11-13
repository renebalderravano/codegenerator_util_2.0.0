package [packageName].application.ports.input;

import org.springframework.security.core.Authentication;

import [packageName].domain.model.UserModel;
import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO.LoginRequest;
import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO.Response;

import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {
	
	public String generateToken(UserModel userModel, Authentication authentication);

	public Response authentication(LoginRequest userLogin, HttpServletResponse response);
	
}