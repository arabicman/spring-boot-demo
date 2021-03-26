package com.demo.demo06;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@EnableAdminServer
@SpringBootApplication
public class DemoSb06AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSb06AdminServerApplication.class, args);
    }

//    @Bean
//    public HttpFirewall allowUrlSemicolonHttpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowSemicolon(true);
//        firewall.setAllowUrlEncodedSlash(true);
//        return firewall;
//    }

}
