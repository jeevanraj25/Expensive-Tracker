package ExpenseTracker.app.service;

import ExpenseTracker.app.entites.UserInfo;
import ExpenseTracker.app.eventProducer.UserInfoEvent;
import ExpenseTracker.app.eventProducer.UserInfoProducer;
import ExpenseTracker.app.model.UserInfoDto;
import ExpenseTracker.app.repository.UserRepository;
import ExpenseTracker.app.validationUtil.validateUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService
{

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private  final UserRepository userRepository;

    @Autowired
    private final UserInfoProducer userInfoProducer;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserInfo user  =userRepository.findByUsername(username);
        if(user ==null){
            throw new UsernameNotFoundException("could not found user...!");
        }
        return new CustomerUserDetails(user);

    }

    public UserInfo checkIfUserAlreadyExits(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getFirstName());
    }


    public Boolean signupUser(UserInfoDto userInfoDto){

//         if(!validateUser.isValidEmail(userInfoDto.getEmail())){
//             return false;
//         }
//
//         if(!validateUser.isValidPassword(userInfoDto.getPassword())){
//             return false;
//         }

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if(Objects.nonNull(checkIfUserAlreadyExits(userInfoDto))){
            return false;
        }

        String userId= UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId,userInfoDto.getFirstName(),userInfoDto.getPassword(),new HashSet<>()));


//            Push to kafka
        // pushEventToQueue
        userInfoProducer.sendEventToKafka(userInfoEventToPublish(userInfoDto, userId));
        System.out.println("send to the kakfa");
        return true;
    }

    private UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto,String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName((userInfoDto.getFirstName()))
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber()).build();
    }

    public String getUserByUsername(String userName){
        return Optional.of(userRepository.findByUsername(userName)).map(UserInfo::getUserId).orElse(null);
    }

}
