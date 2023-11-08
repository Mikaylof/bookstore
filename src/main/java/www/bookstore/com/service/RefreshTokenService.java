package www.bookstore.com.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import www.bookstore.com.entity.RefreshToken;
import www.bookstore.com.repository.RefreshTokenRepository;
import www.bookstore.com.repository.UserInfoRepository;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshTokenl=refreshTokenRepository.findByUserInfo_Email(username);
        if(refreshTokenl!=null){
            return refreshTokenl;
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoRepository.findUserInfoByEmail(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(1000*60*50))//10
                .build();
        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            log.info("fejfhsrjfhjksfgjkg : "+token);
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signing request");
        }
        return token;
    }

}
