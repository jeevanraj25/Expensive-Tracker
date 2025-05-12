//package com.example.userservice.service;
//
//
//import com.example.userservice.entities.UserInfo;
//import com.example.userservice.entities.UserInfoDto;
//import com.example.userservice.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//@Service
//public class UserService {
//
//     @Autowired
//    private UserRepository userRepository;
//
//     public UserInfoDto createOrUpdate(UserInfoDto userInfoDto){
//         Function<UserInfo, UserInfo> updateingUser =user -> {
//             return userRepository.save(userInfoDto.transformToUserInfo());
//         };
//
//         Supplier<UserInfo> createUser = () ->{
//             return userRepository.save(userInfoDto.transformToUserInfo());
//         };
//
//         UserInfo userInfo = userRepository.findByUserId(userInfoDto.getUserId())
//                 .map(updateingUser)
//                 .orElseGet(createUser);
//
//         return new UserInfoDto(
//                 userInfo.getUserId(),
//                 userInfo.getFirstName(),
//                 userInfo.getLastName(),
//                 userInfo.getPhoneNumber(),
//                 userInfo.getEmail(),
//                 userInfo.getProfilePic()
//         );
//     }
//
//     public UserInfoDto getUser(UserInfoDto userInfoDto) throws Exception{
//         Optional<UserInfo> userInfoOpt = userRepository.findByUserId(userInfoDto.getUserId());
//
//         if(userInfoOpt.isEmpty()){
//             throw new Exception("User not found");
//         }
//
//         UserInfo userInfo =userInfoOpt.get();
//         return new UserInfoDto(
//                 userInfo.getUserId(),
//                 userInfo.getFirstName(),
//                 userInfo.getLastName(),
//                 userInfo.getPhoneNumber(),
//                 userInfo.getEmail(),
//                 userInfo.getProfilePic()
//         );
//     }
//
//
//}
package com.example.userservice.service;

import com.example.userservice.entities.UserInfo;
import com.example.userservice.entities.UserInfoDto;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoDto createOrUpdate(UserInfoDto userInfoDto) {
        Function<UserInfo, UserInfo> updateUser = user -> {
            return userRepository.save(userInfoDto.transformToUserInfo());
        };

        Supplier<UserInfo> createUser = () -> {
            return userRepository.save(userInfoDto.transformToUserInfo());
        };

        UserInfo userInfo = userRepository.findByUserId(userInfoDto.getUserId())
                .map(updateUser)
                .orElseGet(createUser);

        return new UserInfoDto(
                userInfo.getUserId(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhoneNumber(),
                userInfo.getEmail(),
                userInfo.getProfilePic()
        );
    }

    public UserInfoDto getUser(UserInfoDto userInfoDto) throws Exception {
        Optional<UserInfo> userInfoOpt = userRepository.findByUserId(userInfoDto.getUserId());

        if (userInfoOpt.isEmpty()) {
            throw new Exception("User not found");
        }

        UserInfo userInfo = userInfoOpt.get();
        return new UserInfoDto(
                userInfo.getUserId(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhoneNumber(),
                userInfo.getEmail(),
                userInfo.getProfilePic()
        );
    }
}