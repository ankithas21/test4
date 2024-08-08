package com.example.VulnerableApplication;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("/api")
public class VulnerableController {

    // Vulnerable Endpoint: SQL Injection
    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable String id) {
        String result = "";
        try {
            // Vulnerable: Directly using user input in SQL query
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users WHERE id = '" + id + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                result = "User found: " + rs.getString("name");
            } else {
                result = "User not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "An error occurred";
        }
        return result;
    }

    // Vulnerable Endpoint: Cross-Site Scripting (XSS)
    @GetMapping("/welcome")
    public String welcome(@RequestParam String userInput) {
        // Vulnerable: Directly including user input in HTML response
        return "<html><body>Welcome, " + userInput + "!</body></html>";
    }

    // Vulnerable: Hardcoded secrets
    @GetMapping("/secret")
    public String getSecret() {
        // Vulnerable: Hardcoded secret in code
        String secret = "SuperSecret123!";
        return "The secret is: " + secret;
    }
}

