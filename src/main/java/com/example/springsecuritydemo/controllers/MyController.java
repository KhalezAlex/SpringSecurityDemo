package com.example.springsecuritydemo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.plaf.IconUIResource;
import java.util.LinkedList;


@Controller
public class MyController {
    private LinkedList<String> categories = new LinkedList<>();
    private LinkedList<String> suggestCat = new LinkedList<>();

    private void addModelAttributes(Model model, Authentication auth) {
        model.addAttribute("list", categories.toArray());
        model.addAttribute("suggestList", suggestCat.toArray());
        model.addAttribute("isAdmin", auth.getAuthorities().toString().contains("ROLE_ADMIN"));
    }

    @GetMapping("/request")
    public String addCat(Model model, Authentication auth, @RequestParam(required = false) String category) {
        if (auth != null) {
            model.addAttribute("isAdmin", auth.getAuthorities().toString().contains("ROLE_ADMIN"));
            if (auth.getAuthorities().toString().contains("ROLE_ADMIN") && category != null)
                categories.add(category);
        }
        addModelAttributes(model, auth);
        System.out.println(categories);
        return "request";
    }

    @GetMapping("/delete")
    public String delete(Model model, Authentication auth, @RequestParam String category) {
        delCategory(category);
        addModelAttributes(model, auth);
        System.out.println(categories);
        return "request";
    }

    private void delCategory(String category) {
        int counter = 0;
        for (String str : categories) {
            if (str.equals(category)) {
                categories.remove(counter);
                return;
            } else counter++;
        }
    }

    @GetMapping("/update")
    public String update(Model model, Authentication auth, @RequestParam String formCat, @RequestParam String futCat) {
        updCategory(formCat, futCat);
        addModelAttributes(model, auth);
        System.out.println(categories);
        return "request";
    }

    private void updCategory(String formCat, String futCat) {
        int counter = 0;
        for (String str : categories) {
            if (str.equals(formCat)) {
                categories.remove(counter);
                categories.add(counter, futCat);
                return;
            } else counter++;
        }
    }

    @GetMapping("/suggest")
    public String suggest(Model model, Authentication auth, @RequestParam String suggest) {
        if (!listsContains(suggest))
            suggestCat.add(suggest);
        addModelAttributes(model, auth);
        System.out.println(suggestCat);
        return "request";
    }

    private boolean listsContains(String category) {
        if (listContains(category)) return true;
        return suggestListContains(category);
    }

    private boolean suggestListContains(String category) {
        for (String str: suggestCat)
            if (str.equalsIgnoreCase(category))
                return true;
        return false;
    }

    private boolean listContains(String category) {
        for (String str : categories)
            if (str.equalsIgnoreCase(category))
                return true;
        return false;
    }

    @GetMapping("/accept")
    public String accept(Model model, Authentication auth, @RequestParam String suggest) {
        if (suggestListContains(suggest)) {
            categories.add(suggest);
            delSuggest(suggest);
        }
        addModelAttributes(model, auth);
        return "request";
    }

    private void delSuggest(String suggest) {
        int counter = 0;
        for (String str : suggestCat)
            if (str.equals(suggest)) {
                suggestCat.remove(counter);
                return;
            } else counter++;
    }
}