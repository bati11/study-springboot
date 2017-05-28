package example.controllers.forms;

import lombok.Data;

@Data
public class UserForm {
    private String name;
    private String email;
    private String password;
    private String passwordConfirmation;
}
