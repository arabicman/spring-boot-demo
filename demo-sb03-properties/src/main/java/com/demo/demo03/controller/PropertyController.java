package com.demo.demo03.controller;

import com.demo.demo03.property.ApplicationProperty;
import com.demo.demo03.property.DeveloperProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PropertyController {
    private final ApplicationProperty applicationProperty;
    private final DeveloperProperty developerProperty;

    @Autowired
    public PropertyController(ApplicationProperty applicationProperty, DeveloperProperty developerProperty){
        this.applicationProperty = applicationProperty;
        this.developerProperty = developerProperty;
    }

    @GetMapping("/property")
    public Map index(){
        Map<String, Object> map = new HashMap<>();
        map.put("appProp", applicationProperty);
        map.put("devProp", developerProperty);
        return  map;
    }
}
