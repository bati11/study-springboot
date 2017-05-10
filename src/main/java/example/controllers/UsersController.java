package example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsersController {

    @RequestMapping("/signup")
    public ModelAndView input() {
        return new ModelAndView("users/input");
    }
}
