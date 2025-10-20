package [packageName].application.usecases;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import [packageName].application.ports.input.AuthService;
import [packageName].infrastructure.adapters.input.rest.AuthController;
import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO;
import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO.LoginRequest;
import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO.Response;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtEncoder jwtEncoder;

	public AuthServiceImpl() {

	}
	
	

	@Override
	public Response authentication(LoginRequest userLogin) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		log.info("Token requested for user:{}", authentication.getAuthorities());

		String token = generateToken(authentication);
		AuthDTO.Response response = new AuthDTO.Response("User logged in successfully", token);

//		LoginWrapper loginWrapper = new LoginWrapper();
//		loginWrapper.setUsuario(userLogin.username());
//		loginWrapper.setContrasena(userLogin.password());
//		interfazIANUXService.connectToIANUX(loginWrapper);

		return response;
	}

	@Override
	public String generateToken(Authentication authentication) {

		Instant now = Instant.now();
		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(now)
				.expiresAt(now.plus(10, ChronoUnit.MINUTES))
				.subject(authentication.getName()).claim("scope", scope)
				.build();

		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

}
