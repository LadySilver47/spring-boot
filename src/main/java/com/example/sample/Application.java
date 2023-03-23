package com.example.sample;

import org.ietf.jgss.Oid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * This example controller has endpoints for displaying the user profile info on {code}/{code} and "you have been
     * logged out page" on {code}/post-logout{code}.
     */
    @Controller
    static class ExampleController {

        @GetMapping("/")
        String home() {
            return "home";
        }

        @GetMapping("/profile")
        @PreAuthorize("hasAuthority('SCOPE_profile')")
        ModelAndView userDetails(OAuth2AuthenticationToken authentication) {
            return new ModelAndView("userProfile", Collections.singletonMap("details", authentication.getPrincipal().getAttributes()));
        }
    }

    @RestController
    @CrossOrigin
    static class ExampleRestController {

        @GetMapping("/hello")
        String sayHello(@AuthenticationPrincipal OidcUser oidcUser, @AuthenticationPrincipal Jwt jwt) {
            return "Hello " + (oidcUser != null ? oidcUser.getFullName() : "anonymous");
        }

        @GetMapping("/api/courses")
        public ResponseEntity<String[]> getCourses(@AuthenticationPrincipal OidcUser oidcUser){
            System.out.println(oidcUser.getEmail());
            return ResponseEntity.ok(new String[]{"JSFS", "JFS", "DNFS"});
        }

        @GetMapping("/api/developers/labs")
        @PreAuthorize("hasAuthority('everyone')")
        public ResponseEntity<String> getLab(){
            return ResponseEntity.ok("This is the last lab :(");
        }
        @GetMapping("api/developers/grades")
        @PreAuthorize("hasAuthority('instructors')")
        public ResponseEntity<String> getGrades(){
            return ResponseEntity.ok("Grades schmades");
        }

        @GetMapping(path = "api/developers/{email}")
        public ResponseEntity<String> getName(@AuthenticationPrincipal OidcUser oidcUser, @PathVariable String email){
            if (oidcUser.getEmail().toString().equals(email)){
                return ResponseEntity.ok(oidcUser.getFullName() + " is cool");
            }
            return ResponseEntity.badRequest().body("Not Authorised");
        }
    }
}
