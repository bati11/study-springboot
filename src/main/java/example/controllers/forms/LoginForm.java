package example.controllers.forms;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class LoginForm {
    private String email;
    private String password;
}
