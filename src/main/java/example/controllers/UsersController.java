package example.controllers;

import example.auth.LoginAccountRepository;
import example.controllers.exceptions.NotFoundException;
import example.controllers.forms.UserForm;
import example.model.LoginAccount;
import example.model.User;
import example.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class UsersController {

    private UserRepository userRepository;
    private LoginAccountRepository loginAccountRepository;

    public UsersController(UserRepository userRepository, LoginAccountRepository loginAccountRepository) {
        this.userRepository = userRepository;
        this.loginAccountRepository = loginAccountRepository;
    }

    @ModelAttribute
    UserForm setUpForm() {
        return new UserForm();
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView input(UserForm form) {
        return new ModelAndView("users/input");
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
    public ModelAndView add(@Validated UserForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) throws ServletException {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("users/input");
        }
        User user = userRepository.add(form.getName(), form.getEmail(), form.getPassword());

        UserDetails loginAccount = loginAccountRepository.loadUserByUsername(form.getEmail());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginAccount, form.getPassword()));

        redirectAttributes.addFlashAttribute("success", "Welcome to the Sample App!");
        return new ModelAndView("redirect:/users/" + user.getId());
    }
}
