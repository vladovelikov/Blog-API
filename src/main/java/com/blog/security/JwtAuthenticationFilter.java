package com.blog.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationHelper jwtAuthenticationHelper;

    //this method is called when we hit any API request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. get token from Authorization header in request
        String requestToken = request.getHeader("Authorization");

        System.out.println(requestToken);

        String username = null;
        String token = null;

        if (requestToken != null && requestToken.startsWith("Bearer")) {
            token = requestToken.substring(7); //fetch original token after bearer

            try {
                username = this.jwtAuthenticationHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException ex) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException ex) {
                System.out.println("JWT Token has expired");
            } catch (MalformedJwtException ex) {
                System.out.println("Invalid JWT");
            }
        } else {
            System.out.println("JWT is null or does not have bearer");
        }


        //2. We got the JWT token, now let's validate it

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // We have username and there is no security Authentication being applied over the apis
            // Then we only need to validate the JWT Token

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.jwtAuthenticationHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //set spring security
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                //failed JWT validation
                System.out.println("Invalid JWT token");
            }


        } else {
            System.out.println("Username is null or context is not null");
        }

        // If JWT token is valid then above conditions might have set the authentication context to authorize the api
        // otherwise we won't be able to access the API -> will hit the JwtAuthenticationEntryPoint > commence method
        // request will be forwarded in both cases using below function
        filterChain.doFilter(request, response);
    }
}
