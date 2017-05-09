package example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UsersController {

    @RequestMapping("/input")
    public ModelAndView input() {
        return new ModelAndView("users/input");
    }
}
