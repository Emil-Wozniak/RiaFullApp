package org.ria.ifzz.RiaApp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
//
//    private final MapValidationErrorService mapValidationErrorService;
//    private final UserService userService;
//    private final JwtTokenProvider jwtTokenProvider;
////    private final AuthenticationManager authenticationManager;
//    private final UserValidator userValidator;
//
//    public UserController(MapValidationErrorService mapValidationErrorService, UserService userService, JwtTokenProvider jwtTokenProvider, UserValidator userValidator) {
//        this.mapValidationErrorService = mapValidationErrorService;
//        this.userService = userService;
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userValidator = userValidator;
//    }
//
//    /**
//     * authenticateUser was provided to handle login with black
//     *
//     * @param loginRequest represent the message received when credentials are incorrect
//     */
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
//        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//        if (errorMap != null) return errorMap;
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsername(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
//
//        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
//    }
//
//    /**
//     * @param user   is user object
//     * @param result validate passwords match
//     * @return Valid user object
//     */
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody user user, BindingResult result) throws UserNameAlreadyExistsException {
//        // Validate passwords match
//        userValidator.validate(user, result);
//        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//        if (errorMap != null) return errorMap;
//        user newUser = userService.saveUser(user);
//        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
//    }
}
