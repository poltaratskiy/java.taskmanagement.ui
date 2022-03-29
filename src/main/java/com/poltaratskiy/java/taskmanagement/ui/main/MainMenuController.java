package com.poltaratskiy.java.taskmanagement.ui.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class MainMenuController {
    @Autowired
    Environment environment;

    @Value("${keykloak.loginpage}")
    private String keykloakLoginPageTemplate;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String mainMenu(HttpServletRequest request, HttpServletResponse response, Model model) throws UnknownHostException {
        // check token and find role
        // return view
        // Создать задачи, смотреть свои - все, ассайнить - только менеджеры и админы

        var accessToken = WebUtils.getCookie(request, "access_token");

        /*if (accessToken == null) {
            var host = InetAddress.getLoopbackAddress().getHostAddress();
            var port = environment.getProperty("local.server.port");
            var redirectAddress = "http://" + host + ":" + port + "/login/success";
            var nonce = java.util.UUID.randomUUID().toString();
            var keykloakLoginPage = keykloakLoginPageTemplate.replaceAll("\\{redirect_uri}", redirectAddress);
            keykloakLoginPage = keykloakLoginPage.replaceAll("\\{nonce}", nonce);

            System.out.println("Keykloak login page: " + keykloakLoginPage);
            return "redirect:" + keykloakLoginPage;
        }*/

        return "mainMenu";
    }
}
