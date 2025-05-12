package ExpenseTracker.app.serializer;

import ExpenseTracker.app.eventProducer.UserInfoEvent;
import ExpenseTracker.app.model.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class UserInfoSerializer implements Serializer<UserInfoEvent> {


    @Override
    public byte[] serialize(String s, UserInfoEvent userInfoDto) {
         byte[] retVal=null;
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(userInfoDto).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }
}
