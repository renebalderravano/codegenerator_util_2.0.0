package [packageName].application.usecases.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import [packageName].application.ports.input.security.AuthService;
import [packageName].application.ports.input.security.UserService;
import [packageName].domain.model.UserModel;
import [packageName].infrastructure.adapters.input.rest.security.AuthController;
import [packageName].infrastructure.adapters.input.dto.security.AuthDTO;
import [packageName].infrastructure.adapters.input.dto.security.AuthDTO.LoginRequest;
import [packageName].infrastructure.adapters.input.dto.security.AuthDTO.Response;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtEncoder jwtEncoder;
	
	@Autowired
	private UserService userService;

	public AuthServiceImpl() {

	}
	
	

	@Override
	public Response authentication(LoginRequest userLogin, HttpServletResponse response) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		log.info("Token requested for user:{}", authentication.getAuthorities());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("username", userDetails.getUsername());
//		data.put("password", userDetails.getPassword());
		
		List<Object> users = userService.findBy(data, UserModel.class);
		
		if(users != null && !users.isEmpty()) {
			UserModel um =	(UserModel) users.get(0);
			
			String token = generateToken(um, authentication);
			AuthDTO.Response resp = new AuthDTO.Response("User logged in successfully", token);
			
	        Cookie cookie = new Cookie("JWT-TOKEN", resp.token());
	        cookie.setSecure(true);
	        cookie.setHttpOnly(true);
	        cookie.setDomain("localhost");
	        cookie.setPath("/");
	        cookie.setMaxAge(60 * 10); // 10 min
	        cookie.setAttribute("SameSite", "None"); // Required for cross-site cookies
	        response.addCookie(cookie);
			
			return resp;
		}
		

		

//		LoginWrapper loginWrapper = new LoginWrapper();
//		loginWrapper.setUsuario(userLogin.username());
//		loginWrapper.setContrasena(userLogin.password());
//		interfazIANUXService.connectToIANUX(loginWrapper);

		return null;
	}

	@Override
	public String generateToken(UserModel userModel, Authentication authentication) {

		Instant now = Instant.now();
		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("userId", userModel.getId());
		data.put("username", userModel.getUsername());
		data.put("password", userModel.getPassword());
		data.put("scope", scope);
//		Consumer<Map<String, Object>> claimsxx = (Consumer<Map<String, Object>>) data;
		
		Consumer<Map<String, Object>> claimsxx = claims -> {
		    claims.put("userId", userModel.getId());
		    claims.put("username", userModel.getUsername());
		    claims.put("password", userModel.getPassword());
		    claims.put("scope", scope);
		};


		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(now)
				.expiresAt(now.plus(10, ChronoUnit.MINUTES))
				.subject(authentication.getName()).claims(claimsxx)
				.build();

		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

}
