package example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("static_pages")
public class StaticPageController {

    @RequestMapping("/")
    public String home(Model model) {
        return "home";
    }

    @RequestMapping("/help")
    public String help(Model model) {
        return "help";
    }
}
