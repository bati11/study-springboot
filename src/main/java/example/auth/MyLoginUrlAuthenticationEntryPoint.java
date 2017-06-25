package example.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public ForwardingUrl forwardingUrl;

    public MyLoginUrlAuthenticationEntryPoint(ForwardingUrl forwardingUrl, String loginFormUrl) {
        super(loginFormUrl);
        this.forwardingUrl = forwardingUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        forwardingUrl.setUrl(request.getRequestURI());
        super.commence(request, response, authException);
    }
}
