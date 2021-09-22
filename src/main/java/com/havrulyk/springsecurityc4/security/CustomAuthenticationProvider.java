package com.havrulyk.springsecurityc4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String name = authentication.getName();
    String password = String.valueOf(authentication.getCredentials());
    UserDetails userDetails = userDetailsService.loadUserByUsername(name);
    if (userDetails != null){
        if (passwordEncoder.matches(password, userDetails.getPassword())){
          return new UsernamePasswordAuthenticationToken(name, password, userDetails.getAuthorities());
        }
    }
    throw new BadCredentialsException("Error");
  }

  @Override
  public boolean supports(Class<?> authType) {
    return UsernamePasswordAuthenticationToken.class.equals(authType );
  }
}
