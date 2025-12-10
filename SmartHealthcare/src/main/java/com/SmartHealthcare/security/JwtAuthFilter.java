package com.SmartHealthcare.security;

import com.SmartHealthcare.config.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt=null;
        final String username;
        final String authHeader =request.getHeader("Authorization");

        String path = request.getServletPath();
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(authHeader!=null && !authHeader.startsWith("Bearer ")){
            jwt=authHeader.substring(7);
        }
        else {
            jwt=getJwtFromCookies(request);
        }
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        username=jwtService.extractUsername(jwt);

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=userDetailsService.loadUserByUsername(username);
            if(jwtService.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

                String newToken = jwtService.generateToken(userDetails);
                Cookie refreshed = new Cookie("jwt", newToken);
                refreshed.setHttpOnly(true);
                refreshed.setSecure(false);
                refreshed.setPath("/");
                refreshed.setMaxAge((int) jwtService.getInactivitySeconds());
                response.addCookie(refreshed);
            }
        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        if(request.getCookies()!=null ){
            for(Cookie cookie: request.getCookies()){
                if("jwt".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

}
