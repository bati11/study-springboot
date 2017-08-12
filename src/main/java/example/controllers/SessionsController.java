package example.controllers;

import example.auth.ForwardingUrl;
import example.controllers.forms.LoginForm;
import example.model.LoginAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Controller
public class SessionsController {

    @Autowired
    public ForwardingUrl forwardingUrl;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView input(
            LoginForm loginForm,
            @RequestParam Optional<Boolean> error,
            @RequestParam Optional<Boolean> unlogin,
            @RequestParam Optional<Boolean> unactivated
    ) {
        ModelAndView modelAndView = new ModelAndView("sessions/input");
        if (error.orElse(false)) {
            modelAndView.addObject("danger", "Invalid email/password combination");
        } else if (unlogin.orElse(false)) {
            modelAndView.addObject("danger", "Please log in.");
        } else if (unactivated.orElse(false)) {
            modelAndView.addObject("warning", "Account not activated. Check your email for the activation link.");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView create() {
        return new ModelAndView();
    }

    @RequestMapping(value = "/login/success")
    public ModelAndView loginSuccess(
            @AuthenticationPrincipal LoginAccount loginAccount,
            RedirectAttributes redirectAttributes
    ) throws UserPrincipalNotFoundException {
        if (loginAccount == null) throw new UserPrincipalNotFoundException("loginSuccess");

        if (loginAccount.getActivated()) {
            if (forwardingUrl != null && forwardingUrl.getUrl() != null) {
                String url = forwardingUrl.getUrl();
                forwardingUrl.setUrl(null);
                return new ModelAndView("redirect:" + url);
            } else {
                return new ModelAndView("redirect:/users/" + loginAccount.getUserId().toString());
            }
        } else {
            redirectAttributes.addFlashAttribute("warning", "Account not activated. Check your email for the activation link.");
            return new ModelAndView("redirect:/");
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView destroy() {
        return new ModelAndView();
    }
}
