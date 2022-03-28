package com.poltaratskiy.java.taskmanagement.ui.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var accessTokenCookie= WebUtils.getCookie(request, "access_token");

        if (accessTokenCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var accessToken = accessTokenCookie.getValue();

        DecodedJWT jwt = null;
        try {
            // Verification here...

            // Decoding
            jwt = JWT.decode(accessToken);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            response.setHeader("303", "/login");
            filterChain.doFilter(request, response);
            return;
        }

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var realmAccess = jwt.getClaim("realm_access");
        var rolesArrayList = (ArrayList<String>)(realmAccess.asMap().get("roles"));
        var roles = rolesArrayList.toArray(new String[0]);
        var userNameClaim = jwt.getClaim("preferred_username");
        var username = userNameClaim.asString();

        var grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (String itVar : roles)
        {
            grantedAuthorities.add(new SimpleGrantedAuthority(itVar));
        }

        UserDetails user = new User(username, "password", grantedAuthorities);
        var authentication = new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);

        var details = new WebAuthenticationDetails(request);
        authentication.setDetails(details);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
