package example.controllers;

import example.auth.LoginAccountRepository;
import example.controllers.exceptions.NotFoundException;
import example.controllers.forms.UserInputForm;
import example.controllers.forms.UserEditForm;
import example.model.User;
import example.repositories.UserRepository;
import example.view.Pager;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    private UserRepository userRepository;
    private LoginAccountRepository loginAccountRepository;

    public UsersController(UserRepository userRepository, LoginAccountRepository loginAccountRepository) {
        this.userRepository = userRepository;
        this.loginAccountRepository = loginAccountRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView input() {
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
            RedirectAttributes redirectAttributes
    ) throws ServletException {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("users/input");
            modelAndView.addObject("userInputForm", userInputForm);
            return modelAndView;
        }
        User user = userRepository.add(userInputForm.getName(), userInputForm.getEmail(), userInputForm.getPassword());

        UserDetails loginAccount = loginAccountRepository.loadUserByUsername(userInputForm.getEmail());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginAccount, userInputForm.getPassword()));

        redirectAttributes.addFlashAttribute("success", "Welcome to the Sample App!");
        return new ModelAndView("redirect:/users/" + user.getId());
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
        User updatedUser = userRepository.update(user, userEditForm.getName(), userEditForm.getEmail(), userEditForm.getPassword());

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
}
