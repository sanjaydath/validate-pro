package com.frontbackend.thymeleaf.bootstrap.controller;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({ "/", "/home" })
public class IndexController {

    @GetMapping
	public String main(
			/* @RequestParam("username") String username, */Model model, RedirectAttributes redirectAttributes) {
//    	redirectAttributes.addFlashAttribute("username", username);
        return "home";
    }
    
    @PostMapping
	public String home(
			/* @RequestParam("username") String username, */ Model model, RedirectAttributes redirectAttributes) {
//    	redirectAttributes.addFlashAttribute("username", username);
        return "home";
    }

    @GetMapping("{tab}")
    public String tab(@PathVariable String tab) {
        if (Arrays.asList("tab1", "tab2", "tab3", "tab4", "tab5")
                  .contains(tab)) {
            return "_" + tab;
        }

        return "empty";
    }
    
    @GetMapping("fileDetail/{file}")
    public String fileDetailTab(Model model, RedirectAttributes redirectAttributes, @PathVariable String file) {
        return "filedetail";
    }
}
