package example.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {

    private static String ERROR_PAGE = "/access-denied";

    private SessionHelper sessionHelper;

    private ForwardingUrl forwardingUrl;

    public MyAccessDeniedHandler(SessionHelper sessionHelper, ForwardingUrl forwardingUrl) {
        super();
        this.sessionHelper = sessionHelper;
        this.forwardingUrl = forwardingUrl;
        this.setErrorPage(ERROR_PAGE);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (accessDeniedException instanceof CsrfException || accessDeniedException instanceof AuthorizationServiceException) {
            super.handle(request, response, accessDeniedException);
        } else {
            request.setAttribute(WebAttributes.ACCESS_DENIED_403,
                    accessDeniedException);

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            String rootPage = sessionHelper.currentAccount()
                    .map(loginAccount -> "/users/" + loginAccount.getUserId())
                    .orElse(ERROR_PAGE);
            RequestDispatcher dispatcher = request.getRequestDispatcher(rootPage);
            dispatcher.forward(request, response);
        }
    }

}
