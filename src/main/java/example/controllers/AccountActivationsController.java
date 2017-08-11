package example.controllers;

import example.auth.SessionHelper;
import example.model.User;
import example.repositories.UserRepository;
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

    private final UserRepository userRepository;
    private final SessionHelper sessionHelper;

    public AccountActivationsController(UserRepository userRepository, SessionHelper sessionHelper) {
        this.userRepository = userRepository;
        this.sessionHelper = sessionHelper;
    }

    @RequestMapping(path = "account_activation/{token}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable String token,
            @RequestParam Optional<String> email,
            RedirectAttributes redirectAttributes
    ) {
        Optional<User> userOpt = email
                .flatMap(x -> {
                    try {
                        String decodedEmail = URLDecoder.decode(x, StandardCharsets.UTF_8.displayName());
                        return userRepository.findByEmail(decodedEmail);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(x -> !x.getActivated() && x.authenticatedActivation(token));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            userRepository.updateActivate(user, Instant.now());

            sessionHelper.login(user);

            redirectAttributes.addFlashAttribute("success", "Account activated!");
            return new ModelAndView("redirect:/users/" + user.getId());
        } else {
            redirectAttributes.addFlashAttribute("danger", "Invalid activation link");
            return new ModelAndView("redirect:/");
        }
    }
}
