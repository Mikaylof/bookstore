package www.bookstore.com.controller;


import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import www.bookstore.com.dto.request.*;
import www.bookstore.com.dto.response.JwtResponse;
import www.bookstore.com.dto.response.RespStatusList;
import www.bookstore.com.dto.response.RespUserDetail;
import www.bookstore.com.dto.response.Response;
import www.bookstore.com.entity.RefreshToken;
import www.bookstore.com.exception.ExceptionConstants;
import www.bookstore.com.exception.MyException;
import www.bookstore.com.service.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
@Slf4j
@Transactional
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/v/signUp")
    public RespStatusList registerUser(@RequestBody ReqUserInfo reqUserInfo) {
        log.info("user " + reqUserInfo.getEmail());
        return userInfoService.saveUserInfo(reqUserInfo);

    }

    @PostMapping("/v/getUser")
    public Response<RespUserDetail> getUserInfo(@RequestBody ReqUserMail reqUserMail) {
        log.info("user " + reqUserMail.getUsername());
        return userInfoService.getUserInfo(reqUserMail);
    }


    @PostMapping("/v/login")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) throws MyException {
        log.info("get request............ "+authRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
                    .token(refreshToken.getToken()).build();
        } else {
            throw new MyException(ExceptionConstants.NOT_FOUND, "user not found in database!");
        }
    }

    @PostMapping("/v/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws MyException {
        log.info("refresh token : " + refreshTokenRequest.getToken());

        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getDisplayName());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new MyException(ExceptionConstants.NOT_FOUND,
                        "Refresh token is not in database!"));
    }


    @PostMapping("/v/addRoleToUser")
    public RespStatusList addRoleToUser(@RequestBody FormClass form) throws MyException {
        log.info("user request " + form.email, form.role);
        return userInfoService.addRoleToUser(form.email, form.role);

    }

    @PostMapping("/v/addRole")
    public RespStatusList addRole(@RequestBody ReqRole reqRole) throws MyException {
        log.info("Role request " + reqRole.getRole());
        return roleService.addRole(reqRole);
    }

    @Data
    public static class FormClass {
        private String email;
        private String role;
    }

    @PostMapping("/v/AddBookToUser")
    public RespStatusList addBookToUser(@RequestBody BookClass bookForm) throws MyException {
        log.info("user request " + bookForm.email, bookForm.bookId);
        return userInfoService.addBookToUser(bookForm.email, bookForm.bookId);

    }

    // getUsersByBookId
    @GetMapping ("/v/GetUserListByBookId/{bookId}")
    public Response<List<RespUserDetail>> getUserListByBookId(@PathVariable Long bookId){
        return userInfoService.getUserListByBookId(bookId);
    }

    @Data
    public static class BookClass {
        private String email;
        private Long bookId;
    }


    @PostMapping("/v/SubscribeToAuthor")
    public RespStatusList addAuthorToUser(@RequestBody AuthorClass authorForm) throws MyException {
        log.info("user request " + authorForm.email, authorForm.authorId);
        return userInfoService.addAuthorToUser(authorForm.email, authorForm.authorId);
    }

    @Data
    public static class AuthorClass {
        private String email;
        private Long authorId;
    }

}
