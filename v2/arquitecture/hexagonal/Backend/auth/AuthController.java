package [packageName].infrastructure.adapters.input.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import [packageName].application.ports.input.AuthService;
import [packageName].configuration.SingletonTokenHolder;
import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO;
import [packageName].infrastructure.adapters.input.rest.dto.AuthDTO.Response;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private SingletonTokenHolder valueHolder;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin) throws IllegalAccessException {
        Response response = authService.authentication(userLogin);
		return ResponseEntity.ok(response);
    }
}
