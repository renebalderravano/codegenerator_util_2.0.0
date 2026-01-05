package [packageName].application.usecases.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import [packageName].application.ports.output.security.UserDAO;
import [packageName].infrastructure.adapters.output.persistence.entity.security.UserEntity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    
    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
//        User users = userDAO.findByUserName(username, "");
//        if (users != null) {
//            return users;
//        }
//        throw new UsernameNotFoundException(username);
    	
    	UserDetails userDetails = null;
    	String correo = "";
		String contrasena = "";
    	
    	UserEntity result = userDAO.findByUserName(username);
    	
    	if (result != null) {
			correo = result.getUsername();
			contrasena = result.getPassword();
			
			if(result.getUserType().getId() == 1) {
				GrantedAuthority authority = new SimpleGrantedAuthority(
						"ROLE_ADMIN");
				userDetails = this.userDetails(correo, contrasena,
						authority);
			} else {
				GrantedAuthority authority = new SimpleGrantedAuthority(
						"ROLE_CLIENT_ADMIN");
				userDetails = this.userDetails(correo, contrasena,
						authority);				
			}

		} else {
			GrantedAuthority authority = new SimpleGrantedAuthority(
					"ROLE_USER");
//			resultClient = this.usuarioClienteService
//					.consultarPorIdentificador(usuario);
//			
//			if(resultClient != null) {
//				userDetails = new org.springframework.security.core.userdetails.User(
//						resultClient.getUserName(),
//						resultClient.getContrasena(),
//						Arrays.asList(authority));
//			}
			
			throw new UsernameNotFoundException(username);
		}
    	
    	return userDetails;
    }
    
    private UserDetails userDetails(String correo, String contrasena,
			GrantedAuthority authority) {

		return new org.springframework.security.core.userdetails.User(
				correo, contrasena, Arrays.asList(authority));
	}
}
