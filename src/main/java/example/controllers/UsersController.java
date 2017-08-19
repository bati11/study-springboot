package example.controllers;

import example.controllers.exceptions.NotFoundException;
import example.controllers.forms.UserEditForm;
import example.controllers.forms.UserInputForm;
import example.model.AccountActivationMail;
import example.model.LoginAccount;
import example.model.User;
import example.repositories.UserRepository;
import example.service.SendMailService;
import example.util.Digest;
import example.util.DigestFactory;
import example.view.Pager;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    private UserRepository userRepository;
    private SendMailService sendMailService;

    public UsersController(
            UserRepository userRepository,
            SendMailService sendMailService) {
        this.userRepository = userRepository;
        this.sendMailService = sendMailService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView input() throws MessagingException, IOException {
        ModelAndView modelAndView = new ModelAndView("users/input");
        modelAndView.addObject("userInputForm", new UserInputForm());

        return modelAndView;
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public ModelAndView show(@PathVariable int id) {
        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt.orElseThrow(NotFoundException::new);
        ModelAndView modelAndView = new ModelAndView("users/show");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ModelAndView add(
            @Validated UserInputForm userInputForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriComponentsBuilder
    ) throws ServletException {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("users/input");
            modelAndView.addObject("userInputForm", userInputForm);
            return modelAndView;
        }
        Digest passwordDigest = DigestFactory.create(userInputForm.getPassword());
        String activationToken = LoginAccount.newToken();
        Digest activationDigest = DigestFactory.create(activationToken);
        User user = userRepository.add(
                User.create(userInputForm.getName(), userInputForm.getEmail()),
                passwordDigest,
                activationDigest);

        String url = getAccountActivationUrl(uriComponentsBuilder, activationToken, user);
        AccountActivationMail mail = new AccountActivationMail(user, url);
        sendMailService.execute(mail);

        redirectAttributes.addFlashAttribute("success", "Please check your email to activate your account.");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = "/users/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(
            @PathVariable int id
    ) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        UserEditForm userEditForm = new UserEditForm();
        userEditForm.setName(user.getName());
        userEditForm.setEmail(user.getEmail());
        ModelAndView modelAndView = new ModelAndView("users/edit");
        modelAndView.addObject("userEditForm", userEditForm);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(path = "/users/{id}/update", method = RequestMethod.POST)
    public ModelAndView update(
            @PathVariable int id,
            @Validated UserEditForm userEditForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("users/edit");
            modelAndView.addObject("userEditForm", userEditForm);
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        User updatedUser = userRepository.update(
                user,
                userEditForm.getName(),
                userEditForm.getEmail(),
                DigestFactory.create(userEditForm.getPassword()));

        redirectAttributes.addFlashAttribute("success", "Profile updated");
        return new ModelAndView(("redirect:/users/" + updatedUser.getId()));
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam Optional<Integer> pageNumber) {
        int countPerPage = 5;
        List<User> users = userRepository.select(countPerPage, offset(countPerPage, pageNumber));
        Integer totalCount = userRepository.count();

        Pager pager = new Pager(pageNumber.orElse(1), totalCount, countPerPage);

        ModelAndView modelAndView = new ModelAndView("/users/index");
        modelAndView.addObject("users", users);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    @RequestMapping(path = "/users/{id}/destroy", method = RequestMethod.POST)
    public ModelAndView destroy(
            @PathVariable int id,
            RedirectAttributes redirectAttributes
    ) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.remove(user);

        redirectAttributes.addFlashAttribute("success", "User deleted");
        return new ModelAndView(("redirect:/users/"));
    }

    private int offset(int countPerPage, Optional<Integer> pageNumber) {
        int n = pageNumber.orElse(0);
        n = n < 1 ? 1 : n;
        return countPerPage * (n - 1);
    }

    private String getAccountActivationUrl(UriComponentsBuilder uriComponentsBuilder, String activationToken, User user) {
        try {
            String url = uriComponentsBuilder
                    .pathSegment("account_activation", activationToken, "edit")
                    .queryParam("email", URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8.displayName()))
                    .toUriString();
            return url;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
