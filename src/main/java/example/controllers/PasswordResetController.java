package example.controllers;

import example.auth.LoginAccountRepository;
import example.auth.SessionHelper;
import example.controllers.forms.PasswordResetEmailForm;
import example.controllers.forms.PasswordResetForm;
import example.model.LoginAccount;
import example.model.PasswordResetMail;
import example.service.SendMailService;
import example.util.Digest;
import example.util.DigestFactory;
import example.util.URLEncodeUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

@Controller
public class PasswordResetController {

    private final LoginAccountRepository loginAccountRepository;
    private final SendMailService sendMailService;
    private final SessionHelper sessionHelper;

    public PasswordResetController(
            LoginAccountRepository loginAccountRepository,
            SendMailService sendMailService,
            SessionHelper sessionHelper) {
        this.loginAccountRepository = loginAccountRepository;
        this.sendMailService = sendMailService;
        this.sessionHelper = sessionHelper;
    }

    @RequestMapping(value = "/password_resets/new", method = RequestMethod.GET)
    public ModelAndView input() {
        ModelAndView modelAndView = new ModelAndView("password_resets/input");
        modelAndView.addObject("passwordResetEmailForm", new PasswordResetEmailForm());
        return modelAndView;
    }

    @RequestMapping(value = "/password_resets", method = RequestMethod.POST)
    public ModelAndView add(
            @Validated PasswordResetEmailForm passwordResetEmailForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("password_resets/input");
            modelAndView.addObject("passwordResetEmailForm", passwordResetEmailForm);
            return modelAndView;
        }

        LoginAccount loginAccount;
        try {
            loginAccount = loginAccountRepository.loadUserByUsername(passwordResetEmailForm.getEmail());
        } catch (UsernameNotFoundException e) {
            ModelAndView modelAndView = new ModelAndView("password_resets/input");
            modelAndView.addObject("passwordResetEmailForm", passwordResetEmailForm);
            modelAndView.addObject("danger", "Email address not found");
            return modelAndView;
        }

        String resetToken = LoginAccount.newToken();
        Digest resetDigest = DigestFactory.create(resetToken);
        loginAccountRepository.updatePasswordReset(loginAccount, resetDigest, Instant.now());

        String url = getPasswordResetUrl(uriComponentsBuilder, resetToken, loginAccount);
        PasswordResetMail mail = new PasswordResetMail(loginAccount.getUsername(), url);
        sendMailService.execute(mail);

        redirectAttributes.addFlashAttribute("success", "Email sent with password reset instructions");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/password_resets/{token}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable String token,
            @RequestParam Optional<String> email,
            RedirectAttributes redirectAttributes
    ) {
        Optional<String> decodedEmail = email.map(s -> URLEncodeUtil.decode(s));
        return executeIfValidToken(token, redirectAttributes, decodedEmail, (loginAccount -> {
            ModelAndView modelAndView = new ModelAndView("password_resets/edit");
            PasswordResetForm passwordResetForm = new PasswordResetForm();
            passwordResetForm.setEmail(loginAccount.getEmail());
            modelAndView.addObject("passwordResetForm", passwordResetForm);
            modelAndView.addObject("token", token);
            return modelAndView;
        }));
    }

    @RequestMapping(path = "/password_resets/{token}/update", method = RequestMethod.POST)
    public ModelAndView update(
            @PathVariable String token,
            @Validated PasswordResetForm passwordResetForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("password_resets/edit");
            modelAndView.addObject("passwordResetForm", passwordResetForm);
            modelAndView.addObject("token", token);
            return modelAndView;
        }
        Optional<String> decodedEmail = Optional.of(passwordResetForm.getEmail());
        return executeIfValidToken(token, redirectAttributes, decodedEmail, (loginAccount -> {
            LoginAccount updatedLoginAccount = loginAccountRepository.updatePassword(
                    loginAccount,
                    DigestFactory.create(passwordResetForm.getPassword()));
            sessionHelper.login(updatedLoginAccount);
            redirectAttributes.addFlashAttribute("success", "Password has been reset.");
            return new ModelAndView("redirect:/users/" + updatedLoginAccount.getUserId());
        }));
    }

    private String getPasswordResetUrl(UriComponentsBuilder uriComponentsBuilder, String resetToken, LoginAccount loginAccount) {
        String url = uriComponentsBuilder
                .pathSegment("password_resets", resetToken, "edit")
                .queryParam("email", URLEncodeUtil.encode(loginAccount.getUsername()))
                .toUriString();
        return url;
    }

    private ModelAndView executeIfValidToken(
            String token,
            RedirectAttributes redirectAttributes,
            Optional<String> emailOpt, Function<LoginAccount, ModelAndView> f) {
        Optional<LoginAccount> loginAccountOpt = emailOpt
                .map(s -> loginAccountRepository.loadUserByUsername(s))
                .filter(loginAccount -> loginAccount.getActivated() && loginAccount.authenticatedReset(token));
        if (loginAccountOpt.isPresent()) {
            LoginAccount loginAccount = loginAccountOpt.get();
            if (loginAccount.isResetExpiration()) {
                redirectAttributes.addFlashAttribute("danger", "Password reset has expired.");
                return new ModelAndView("redirect:/password_resets/new");
            } else {
                return f.apply(loginAccount);
            }
        } else {
            redirectAttributes.addFlashAttribute("danger", "Invalid password reset link");
            return new ModelAndView("redirect:/");
        }
    }

}
