package example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("static_pages")
public class StaticPageController {

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("static_pages/home");
    }

    @RequestMapping("/help")
    public ModelAndView help() {
        return new ModelAndView("static_pages/help");
    }
}
