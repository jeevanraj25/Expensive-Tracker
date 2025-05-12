package ExpenseTracker.app.service;


import ExpenseTracker.app.entites.RefreshToken;
import ExpenseTracker.app.entites.UserInfo;
import ExpenseTracker.app.repository.RefreshTokenRepository;
import ExpenseTracker.app.repository.UserRepository;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){

        UserInfo userInfoExtracted =userRepository.findByUsername(username);
        RefreshToken refreshToken =RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000))
                .build();

        return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+" Refresh Token is expired. Please make a new login");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }


}
