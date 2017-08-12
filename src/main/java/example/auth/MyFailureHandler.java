package example.auth;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

public class MyFailureHandler extends ExceptionMappingAuthenticationFailureHandler {
}
