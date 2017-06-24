package example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StaticPageController {

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("static_pages/home");
    }

    @RequestMapping("/help")
    public ModelAndView help() {
        return new ModelAndView("static_pages/help");
    }

    @RequestMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("static_pages/about");
    }

    @RequestMapping("/contact")
    public ModelAndView contact() {
        return new ModelAndView("static_pages/contact");
    }

    @RequestMapping("/access-denied")
    public ModelAndView accessDenied() {
        return new ModelAndView("static_pages/access-denied");
    }
}
