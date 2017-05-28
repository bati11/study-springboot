package example.controllers;

import example.controllers.exceptions.NotFoundException;
import example.controllers.forms.UserForm;
import example.model.User;
import example.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class UsersController {

    private UserRepository userRepository;

    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public ModelAndView add(UserForm form) {
        System.out.println(form);
        return null;
    }
}
