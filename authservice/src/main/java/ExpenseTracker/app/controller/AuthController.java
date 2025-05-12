package ExpenseTracker.app.controller;

import ExpenseTracker.app.entites.RefreshToken;
import ExpenseTracker.app.model.UserInfoDto;
import ExpenseTracker.app.response.JwtResponseDTO;
import ExpenseTracker.app.service.JwtService;
import ExpenseTracker.app.service.RefreshTokenService;
import ExpenseTracker.app.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@RestController
public class AuthController {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
//        System.out.println(userInfoDto);
        try{
            Boolean isSignUped = userDetailsService.signupUser(userInfoDto);
           ;
            if(Boolean.FALSE.equals(isSignUped)){
                return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getFirstName());

            String jwtToken = jwtService.GenerateToken(userInfoDto.getFirstName());
            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).
                    token(refreshToken.getToken()).build(), HttpStatus.OK);
        }catch (Exception ex){

            System.out.println(ex.getMessage());
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
