package example.controllers;

import example.controllers.forms.LoginForm;
import example.model.LoginAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Controller
public class SessionsController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView input(LoginForm loginForm, @RequestParam Optional<Boolean> error) {
        ModelAndView modelAndView = new ModelAndView("sessions/input");
        if (error.orElse(false)) {
            modelAndView.addObject("danger", "Invalid email/password combination");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView create() {
        return new ModelAndView();
    }

    @RequestMapping(value = "/login/success")
    public ModelAndView loginSuccess() throws UserPrincipalNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof LoginAccount) {
            Integer userId = ((LoginAccount) authentication.getPrincipal()).getUserId();
            return new ModelAndView("redirect:/users/" + userId.toString());
        } else {
            throw new UserPrincipalNotFoundException("loginSuccess");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView destroy() {
        return new ModelAndView();
    }
}
