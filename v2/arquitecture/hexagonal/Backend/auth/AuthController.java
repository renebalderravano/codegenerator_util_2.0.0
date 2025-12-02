package [packageName].infrastructure.adapters.input.rest.security;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import [packageName].application.ports.input.security.AuthService;
import [packageName].infrastructure.adapters.input.dto.security.AuthDTO;
import [packageName].infrastructure.adapters.input.dto.security.AuthDTO.Response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    //private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException {
        Response resp = authService.authentication(userLogin, response);        
		return ResponseEntity.ok(resp);
    }
}
