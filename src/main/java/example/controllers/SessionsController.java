package example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SessionsController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView input() {
        return new ModelAndView("sessions/input");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView create() {
        return new ModelAndView();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView destroy() {
        return new ModelAndView();
    }
}
