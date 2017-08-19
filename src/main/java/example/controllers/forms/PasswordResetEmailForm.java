package example.controllers.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class PasswordResetEmailForm implements Serializable {

    public static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max=255)
    @Email
    private String email;
}
