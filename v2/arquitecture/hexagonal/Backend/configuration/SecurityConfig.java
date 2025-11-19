package [packageName].configuration;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(RsaKeyConfigProperties.class)
public class SecurityConfig {

	//private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	@Autowired
	private RsaKeyConfigProperties rsaKeyConfigProperties;

	@Autowired
	private UserDetailsService userDetailsService;	
	
	private final CustomInterceptor customInterceptor;
	 
	public SecurityConfig(CustomInterceptor customInterceptor) {
        this.customInterceptor = customInterceptor;
	}

	@SuppressWarnings("deprecation")
	@Bean
	public AuthenticationManager authManager() {
		var authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		return http
		.csrf(csrf -> {
			csrf.disable();
		})
		.cors(Customizer.withDefaults())
		.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/error/**").permitAll();
			auth.requestMatchers("/api/auth/**").permitAll();
			auth.requestMatchers("/ClientFile/**").permitAll();
			auth.requestMatchers("/PayrollPaymentType/**").permitAll();
			auth.requestMatchers("/User/**").permitAll();
			auth.requestMatchers("/FileType/**").permitAll();
			auth.requestMatchers("/Employee/**").permitAll();
			auth.requestMatchers("/UserType/**").permitAll();
			auth.requestMatchers("/UserProfile/**").permitAll();
			auth.requestMatchers("/Access/**").permitAll();
			auth.requestMatchers("/AccessProfile/**").permitAll();
			auth.requestMatchers("/Layout/**").permitAll();
			auth.requestMatchers("/Status/**").permitAll();
			auth.requestMatchers("/AccessProfileDetail/**").permitAll();
			auth.requestMatchers("/Client/**").permitAll();
			auth.requestMatchers("/LayoutField/**").permitAll();
			auth.requestMatchers("/UserEmployee/**").permitAll();
//			auth.requestMatchers("/seguridad/usuario/save").hasAnyRole("ROLE_ADMIN");			
			auth.anyRequest().authenticated();
		}).sessionManagement(
				s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		)
		.oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.decoder(jwtDecoder())))
		.userDetailsService(userDetailsService).httpBasic(Customizer.withDefaults())
		.build();
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(rsaKeyConfigProperties.getPublicKey()).build();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(rsaKeyConfigProperties.getPublicKey())
				.privateKey(rsaKeyConfigProperties.getPrivateKey()).build();

		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("https://localhost:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public MappedInterceptor loginInter() {
		return new MappedInterceptor(null, customInterceptor);
	}

}
