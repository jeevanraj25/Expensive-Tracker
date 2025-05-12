package ExpenseTracker.app.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {

    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String password;
    private Long phoneNumber;
    private String email;
}
