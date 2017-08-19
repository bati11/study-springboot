package example.controllers.forms;

import example.controllers.forms.validators.EqualsPropertyValues;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsPropertyValues(property = "password", comparingProperty = "passwordConfirmation")
public class PasswordResetForm implements Serializable {

    public static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max=255)
    private String email;

    @NotBlank
    @Size(min=6, max=255)
    private String password;

    @NotBlank
    @Size(min=6, max=255)
    private String passwordConfirmation;
}
