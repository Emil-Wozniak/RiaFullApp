package org.ria.ifzz.RiaApp.security;

import org.ria.ifzz.RiaApp.models.User.User;
import org.ria.ifzz.RiaApp.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.ria.ifzz.RiaApp.security.SecurityConstants.HEADER_STRING;
import static org.ria.ifzz.RiaApp.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter
//        extends OncePerRequestFilter
{
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        try {
//            String jwt = getJwtFromRequest(request);
//            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) ;
//            Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
//            User userDetails = customUserDetailsService.loadUserById(userId);
//
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, Collections.emptyList());
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        } catch (Exception ex) {
//            logger.error("Couldn't set user authentication in security context", ex);
//        }
//
//        filterChain.doFilter(request, response);
//
//    }
//
//    /**
//     * @param request
//     * @return token content in String excluding Bearer
//     */
//    private String getJwtFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader(HEADER_STRING);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
//            return bearerToken.substring(7, bearerToken.length());
//        }
//        return null;
//    }
}
