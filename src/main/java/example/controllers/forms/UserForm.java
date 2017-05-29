package example.controllers.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserForm implements Serializable {

    public static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirmation;
}
