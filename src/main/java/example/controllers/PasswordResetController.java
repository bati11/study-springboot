package example.controllers;

import example.auth.LoginAccountRepository;
import example.controllers.forms.PasswordResetEmailForm;
import example.model.LoginAccount;
import example.model.PasswordResetMail;
import example.service.SendMailService;
import example.util.Digest;
import example.util.DigestFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Controller
public class PasswordResetController {

    private LoginAccountRepository loginAccountRepository;
    private SendMailService sendMailService;

    public PasswordResetController(LoginAccountRepository loginAccountRepository, SendMailService sendMailService) {
        this.loginAccountRepository = loginAccountRepository;
        this.sendMailService = sendMailService;
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

    private String getPasswordResetUrl(UriComponentsBuilder uriComponentsBuilder, String resetToken, LoginAccount loginAccount) {
        try {
            String url = uriComponentsBuilder
                    .pathSegment("password_resets", resetToken, "edit")
                    .queryParam("email", URLEncoder.encode(loginAccount.getUsername(), StandardCharsets.UTF_8.displayName()))
                    .toUriString();
            return url;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
