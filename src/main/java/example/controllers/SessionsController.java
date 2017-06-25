package example.controllers;

import example.controllers.forms.LoginForm;
import example.model.LoginAccount;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ModelAndView input(
            LoginForm loginForm,
            @RequestParam Optional<Boolean> error,
            @RequestParam Optional<Boolean> unlogin
    ) {
        ModelAndView modelAndView = new ModelAndView("sessions/input");
        if (error.orElse(false)) {
            modelAndView.addObject("danger", "Invalid email/password combination");
        } else if (unlogin.orElse(false)) {
            modelAndView.addObject("danger", "Please log in.");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView create() {
        return new ModelAndView();
    }

    @RequestMapping(value = "/login/success")
    public ModelAndView loginSuccess(@AuthenticationPrincipal LoginAccount loginAccount) throws UserPrincipalNotFoundException {
        if (loginAccount == null) throw new UserPrincipalNotFoundException("loginSuccess");
        return new ModelAndView("redirect:/users/" + loginAccount.getUserId().toString());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView destroy() {
        return new ModelAndView();
    }
}
