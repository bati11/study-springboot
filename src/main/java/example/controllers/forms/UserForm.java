package example.controllers.forms;

import example.controllers.forms.validators.EqualsPropertyValues;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsPropertyValues(property = "password", comparingProperty = "passwordConfirmation")
public class UserForm implements Serializable {

    public static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max=50)
    private String name;

    @NotBlank
    @Size(max=255)
    @Email
    private String email;

    @Size(min=6, max=255)
    private String password;

    @Size(min=6, max=255)
    private String passwordConfirmation;
}
