package com.poltaratskiy.java.taskmanagement.ui.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

@Controller
public class LoginController {
    @Autowired
    Environment environment;

    @Value("${keykloak.loginpage}")
    private String keykloakLoginPageTemplate;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        var host = InetAddress.getLoopbackAddress().getHostAddress();
        var port = environment.getProperty("local.server.port");
        var redirectAddress = "http://" + host + ":" + port + "/login/success";
        var nonce = java.util.UUID.randomUUID().toString();
        var keykloakLoginPage = keykloakLoginPageTemplate.replaceAll("\\{redirect_uri}", redirectAddress);
        keykloakLoginPage = keykloakLoginPage.replaceAll("\\{nonce}", nonce);

        System.out.println("Keykloak login page: " + keykloakLoginPage);
        return "redirect:" + keykloakLoginPage;
    }

    @RequestMapping(value = "/login/success", method = RequestMethod.GET)
    public String successLogin(HttpServletRequest request,
                               HttpServletResponse response) {

        // In this view js code founds anchor tag (such as https://..../success#access_token=...)
        // and redirects to controller below with address such as https://.../success?access_token=...
        return "setAccessTokenToCookies";
    }

    @RequestMapping(value = "/login/success/token", method = RequestMethod.GET)
    public String successLoginWithToken(@RequestParam("access_token") String accessToken, HttpServletResponse response) {

        // removing old cookies
        var oldCookie = new Cookie("access_token", null);
        oldCookie.setPath("/");
        response.addCookie(oldCookie);

        // adding new cookies
        var cookie = new Cookie("access_token", accessToken);
        cookie.setMaxAge(86400);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
