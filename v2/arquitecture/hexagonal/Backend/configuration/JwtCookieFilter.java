package [packageName].configuration;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public JwtCookieFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

    	System.out.println(request.getRequestURL().toString());
    	
    	if (request.getRequestURL().toString().equals("http://localhost:8080/api/auth/login")) {
        	chain.doFilter(request, response);
        	return;
    	}
    	
        String token = extractJwtFromCookie(request);
        if (token != null) {
        	
            try {
            	System.out.println("Validando token...");
                Jwt jwt = jwtDecoder.decode(token);
                String username = jwt.getSubject();
                
                List<String> pass = jwt.getClaimAsStringList("password");
                List<String> roles = jwt.getClaimAsStringList("scope");
                
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                		
                    username, pass.get(0), roles.stream().map(SimpleGrantedAuthority::new).toList()
                    
                );
                
                SecurityContextHolder.getContext().setAuthentication(auth);     
                
//    	        Cookie cookie = new Cookie("JWT-TOKEN", token);
//    	        cookie.setSecure(true);
//    	        cookie.setHttpOnly(true);
//    	        cookie.setDomain("localhost");
//    	        cookie.setPath("/");
//    	        cookie.setMaxAge(60 * 10); // 10 min
//    	        cookie.setAttribute("SameSite", "None"); // Required for cross-site cookies
//    	        response.addCookie(cookie);
    			
                
                chain.doFilter(request, response);
                
            } catch (JwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
                return;
            }
        }else {
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
            return;
        }
        
              
    }
    
    public String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("JWT-TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
