package example.controllers;

import example.auth.LoginAccountRepository;
import example.auth.SessionHelper;
import example.model.LoginAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Controller
public class AccountActivationsController {

    private final LoginAccountRepository loginAccountRepository;
    private final SessionHelper sessionHelper;

    public AccountActivationsController(LoginAccountRepository loginAccountRepository, SessionHelper sessionHelper) {
        this.loginAccountRepository = loginAccountRepository;
        this.sessionHelper = sessionHelper;
    }

    @RequestMapping(path = "account_activation/{token}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable String token,
            @RequestParam Optional<String> email,
            RedirectAttributes redirectAttributes
    ) {
        Optional<LoginAccount> loginAccountOpt = email
                .map(x -> {
                    try {
                        String decodedEmail = URLDecoder.decode(x, StandardCharsets.UTF_8.displayName());
                        return loginAccountRepository.loadUserByUsername(decodedEmail);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(x -> !x.getActivated() && x.authenticatedActivation(token));
        if (loginAccountOpt.isPresent()) {
            LoginAccount loginAccount = loginAccountOpt.get();
            loginAccountRepository.updateActivate(loginAccount, Instant.now());

            sessionHelper.login(loginAccount);

            redirectAttributes.addFlashAttribute("success", "Account activated!");
            return new ModelAndView("redirect:/users/" + loginAccount.getUserId());
        } else {
            redirectAttributes.addFlashAttribute("danger", "Invalid activation link");
            return new ModelAndView("redirect:/");
        }
    }
}
