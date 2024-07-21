package com.cwa.crudspringboot.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cwa.crudspringboot.configuration.JwtUtils;
import com.cwa.crudspringboot.entity.User;
import com.cwa.crudspringboot.repository.UserRepository;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

import com.cwa.crudspringboot.entity.Allergen;
import com.cwa.crudspringboot.entity.Card;
import com.cwa.crudspringboot.entity.Dish;
import com.cwa.crudspringboot.entity.Ingredient;
import com.cwa.crudspringboot.entity.Menu;
import com.cwa.crudspringboot.entity.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
    
@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/logout")
    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        Cookie jsessionCookieRemove = new Cookie("JSESSIONID", "");
        jsessionCookieRemove.setMaxAge(0);
        response.addCookie(jsessionCookieRemove);

        Cookie tokenCookieRemove = new Cookie("token", "");
        tokenCookieRemove.setMaxAge(0);
        response.addCookie(tokenCookieRemove);
        return "redirect:/";
    }

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if (userRepository.findByUsername(name) != null) {
            model.addAttribute("current_user", userRepository.findByUsername(name));
        }
        return "index";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/{restaurant}/admin")
    public String adminPage(@PathVariable String restaurant, Model model, HttpServletResponse response) {
                
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        if (userRepository.findByUsername(restaurant).getUsername() != userRepository.findByUsername(name).getUsername()) {
            return "404";
        }
        
        if (userRepository.findByUsername(restaurant) != null) {
            String token = jwtUtils.generateToken(restaurant);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            String token = jwtUtils.generateToken(restaurant);
            Cookie cookie = new Cookie("token", "undefined");
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "login";
        }
        model.addAttribute("user", userRepository.findByUsername(restaurant));
        return "admin";
    }

    @GetMapping("/{restaurant}")
    public String restaurantPage(@PathVariable String restaurant, Model model,HttpServletResponse response) {
        if (userRepository.findByUsername(restaurant) == null) {
            return "404";
        }
        model.addAttribute("user", userRepository.findByUsername(restaurant));
        return "restaurant";
    }
    
    @GetMapping("/{restaurant}/configuration")
    public String configPage(@PathVariable String restaurant, Model model,HttpServletResponse response) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        if (userRepository.findByUsername(restaurant).getUsername() != userRepository.findByUsername(name).getUsername()) {
            return "404";
        }

        if (userRepository.findByUsername(restaurant) != null) {
            String token = jwtUtils.generateToken(restaurant);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            String token = jwtUtils.generateToken(restaurant);
            Cookie cookie = new Cookie("token", "undefined");
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "login";
        }
        model.addAttribute("user", userRepository.findByUsername(restaurant));
        return "config";
    }

    @GetMapping("/redirect")
    public String redirectPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        
        if (userRepository.findByUsername(name).getIsConfigured() == true) {
            return "redirect:/"+ name + "/admin"; // Redirection vers la page d'accueil après succès                    
        } else {
            return "redirect:/"+ name +"/configuration"; // Redirection vers la page d'accueil après succès
        }
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response, Model model) throws Exception {
        // RestTemplate restTemplate = new RestTemplate();
        // String apiUrl = "http://localhost:8080/api/auth/login";

        // Construire le corps de la requête
        // LoginRequest loginRequest = new LoginRequest(username, password);
        try {
            // Envoyer la requête à l'API
            // ApiResponse apiResponse = restTemplate.postForObject(apiUrl, loginRequest, ApiResponse.class);
            
            // Extraire le token de la réponse
            // String token = apiResponse.getToken();
            
            // Stocker le token dans un cookie
            // Cookie cookie = new Cookie("token", token);
            // cookie.setHttpOnly(true);
            // cookie.setPath("/");
            // response.addCookie(cookie);
            // authentication.setAuthenticated(false);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (authentication.isAuthenticated()) {
                if (userRepository.findByUsername(username).getIsConfigured()) {
                    return "redirect:/"+username+"/admin"; // Redirection vers la page d'accueil après succès                    
                } else {
                    return "redirect:/"+username+"/config"; // Redirection vers la page d'accueil après succès
                }
            }

            model.addAttribute("error", "Invalid username or password");
            return "login";
        } catch (AuthenticationException e) {
            // log.error(e.getMessage());
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/signup")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        // model.addAttribute("card", new Card());
        // model.addAttribute("menu", new Menu());
        // model.addAttribute("dish", new Dish());
        // model.addAttribute("ingredient", new Ingredient());
        // model.addAttribute("allergen", new Allergen());
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@ModelAttribute User user, HttpServletResponse response, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/auth/register";

        // Construire le corps de la requête
        RegisterRequest registerRequest = new RegisterRequest(user.getUsername(), user.getPassword(), user.getDisplayName(), user.getUrl(), user.getColor(), user.getLocation(), user.getContact(), user.getCard());

        try {
            // Envoyer la requête à l'API et récupérer la réponse
            ApiResponse apiResponse = restTemplate.postForObject(apiUrl, registerRequest, ApiResponse.class);

            restTemplate = new RestTemplate();
            apiUrl = "http://localhost:8080/api/auth/login";

            // Construire le corps de la requête
            LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());

            // Envoyer la requête à l'API
            apiResponse = restTemplate.postForObject(apiUrl, loginRequest, ApiResponse.class);

            // Extraire le token de la réponse
            String token = apiResponse.getToken();

            // Stocker le token dans un cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/admin"; // Redirection vers la page d'accueil après succès
        } catch (Exception e) {
            model.addAttribute("error", "L'inscription a échouée...");
            return "signup";
        }
    }

    // @PostMapping("/signup")
    // public String register(@ModelAttribute User user,
    //                        @ModelAttribute Card card,
    //                        @RequestParam("menuNames") List<String> menuNames,
    //                        @RequestParam("dishNames") List<List<String>> dishNames,
    //                        @RequestParam("dishTypes") List<List<String>> dishTypes,
    //                        @RequestParam("ingredientNames") List<List<List<String>>> ingredientNames,
    //                        @RequestParam("allergenNames") List<List<List<String>>> allergenNames,
    //                        HttpServletResponse response, Model model) {
    //     RestTemplate restTemplate = new RestTemplate();
    //     String apiUrl = "http://localhost:8080/api/auth/register";

    // // Construire les menus avec leurs plats, ingrédients et allergènes
    //     Set<Menu> menus = new HashSet<>();
    //     for (int i = 0; i < menuNames.size(); i++) {
    //         Menu menu = new Menu();
    //         menu.setName(menuNames.get(i));

    //         Set<Dish> dishes = new HashSet<>();
    //         for (int j = 0; j < dishNames.get(i).size(); j++) {
    //             Dish dish = new Dish();
    //             dish.setName(dishNames.get(i).get(j));
    //             dish.setType(dishTypes.get(i).get(j));

    //             Set<Ingredient> ingredients = new HashSet<>();
    //             for (String ingredientName : ingredientNames.get(i).get(j)) {
    //                 Ingredient ingredient = new Ingredient();
    //                 ingredient.setName(ingredientName);
    //                 ingredients.add(ingredient);
    //             }
    //             dish.setIngredients(ingredients);

    //             Set<Allergen> allergens  = new HashSet<>();
    //             for (String allergenName : allergenNames.get(i).get(j)) {
    //                 Allergen allergen = new Allergen();
    //                 allergen.setName(allergenName);
    //                 allergens.add(allergen);
    //             }
    //             dish.setAllergens(allergens);

    //             dishes.add(dish);
    //         }
    //         menu.setDishes(dishes);
    //         menus.add(menu);
    //     }
    //     card.setMenus(menus);

    //     // Associer la carte au user
    //     user.setCard(card);

    //     // Construire le corps de la requête
    //     RegisterRequest registerRequest = new RegisterRequest(user.getUsername(), user.getPassword(), user.getDisplayName(), user.getUrl(), user.getColor(), user.getLocation(), user.getContact(), user.getCard());

    //     try {
    //         // Envoyer la requête à l'API et récupérer la réponse
    //         ApiResponse apiResponse = restTemplate.postForObject(apiUrl, registerRequest, ApiResponse.class);

    //         restTemplate = new RestTemplate();
    //         apiUrl = "http://localhost:8080/api/auth/login";
    
    //         // Construire le corps de la requête
    //         LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
        
    //         // Envoyer la requête à l'API
    //         apiResponse = restTemplate.postForObject(apiUrl, loginRequest, ApiResponse.class);
    
    //         // Extraire le token de la réponse
    //         String token = apiResponse.getToken();
    
    //         // Stocker le token dans un cookie
    //         Cookie cookie = new Cookie("token", token);
    //         cookie.setHttpOnly(true);
    //         cookie.setPath("/");
    //         response.addCookie(cookie);

    //         return "redirect:/admin"; // Redirection vers la page d'accueil après succès
    //     } catch (Exception e) {
    //         model.addAttribute("error", "L'inscription a échouée...");
    //         return "signup";
    //     }
    // }

}