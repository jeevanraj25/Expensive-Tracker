package com.example.userservice.deserializer;


import com.example.userservice.entities.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class UserInfoDeserializer implements Deserializer<UserInfoDto> {


    @Override
    public UserInfoDto deserialize(String s,byte[] args) {
        ObjectMapper objectMapper=new ObjectMapper();
        UserInfoDto user =null;
        try{
            user =objectMapper.readValue(args,UserInfoDto.class);
        } catch (Exception e) {
            System.out.println("Can't deserialize");
            e.printStackTrace();
        }
        return user;
    }
}
