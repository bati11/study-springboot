package example.controllers.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserForm implements Serializable {

    public static final long serialVersionUID = 1L;

    @Length(min=1, max=50)
    private String name;

    @Length(min=6, max=255)
    @Email
    private String email;

    @Length(min=6, max=255)
    private String password;

    @Length(min=6, max=255)
    private String passwordConfirmation;
}
