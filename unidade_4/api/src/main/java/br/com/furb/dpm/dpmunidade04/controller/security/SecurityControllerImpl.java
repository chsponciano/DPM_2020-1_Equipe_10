package br.com.furb.dpm.dpmunidade04.controller.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.furb.dpm.dpmunidade04.document.UserDocument;
import br.com.furb.dpm.dpmunidade04.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
class SecurityControllerImpl implements SecurityController {

    ApplicationContext applicationContext;

    PasswordEncoder passwordEncoder;

    @Value("${dpm.jwt.expiration}")
    String expiration;

    OncePerRequestFilter oncePerRequestFilter;

    @Value("${dpm.jwt.secret}")
    String secret;

    UserDetailsService userDetailsService;

    UserRepository userRepository;

    @Autowired
    public SecurityControllerImpl(ApplicationContext applicationContext, UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.oncePerRequestFilter = new OncePerRequestFilterImpl();
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userDetailsService = new UserDetailsServiceImpl();
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(String username, String password) throws TokenGenerationException {
        try {
            var authentication = applicationContext.getBean(AuthenticationManager.class).authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var loggedUser = (UserDetailsImpl) authentication.getPrincipal();
            var today = new Date();
            var expirationDate = new Date(today.getTime() + Long.parseLong(this.expiration));
            return Jwts.builder() //
                    .setIssuer("dpm-unidade-04") //
                    .setSubject(loggedUser.getUserDocument().getId().toString()) //
                    .setIssuedAt(today) //
                    .setExpiration(expirationDate) //
                    .signWith(SignatureAlgorithm.HS256, secret) //
                    .compact();
        } catch (Exception e) {
            throw new TokenGenerationException(e.getMessage(), e);
        }
    }

    @Override
    public String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        protected AuthenticationManager authenticationManager() throws Exception {
            return super.authenticationManager();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests() //
                    .antMatchers(HttpMethod.POST, "/auth/**").permitAll() //
                    .antMatchers(HttpMethod.POST, "/user").permitAll() //
                    .antMatchers(HttpMethod.POST, "/device").permitAll() //
                    .anyRequest().authenticated() //
                    .and().csrf().disable() //
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
                    .and().addFilterBefore(oncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }

    private class OncePerRequestFilterImpl extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            String token = getToken(httpServletRequest);
            if (isValidToken(token)) {
                authenticate(token);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

        private String getToken(HttpServletRequest httpServletRequest) {
            String token = httpServletRequest.getHeader("Authorization");
            if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
                return null;
            } else {
                return token.substring(7);
            }
        }

        public boolean isValidToken(String token) {
            try {
                Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private void authenticate(String token) {
            var userId = getUserIdByToken(token);
            var userDocument = userRepository.findById(userId).orElseThrow();
            var userDetails = UserDetailsImpl.from(userDocument);
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        public String getUserIdByToken(String token) {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims.getSubject();
        }
    }

    private class UserDetailsServiceImpl implements UserDetailsService {

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return UserDetailsImpl.from(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username)));
        }

    }

    @AllArgsConstructor(staticName = "from")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    private static class UserDetailsImpl implements UserDetails {

        UserDocument userDocument;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
        }

        @Override
        public String getPassword() {
            return userDocument.getPassword();
        }

        @Override
        public String getUsername() {
            return userDocument.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

}
