package org.ria.ifzz.RiaApp.security;

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
//            user userDetails = customUserDetailsService.loadUserById(userId);
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
