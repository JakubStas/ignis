package com.jakubstas.ignis.wemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WemoEndpoint {

    @Autowired
    private WemoService wemoService;

    @RequestMapping(path = "wemo/on", method = GET, produces = TEXT_PLAIN_VALUE)
    public String turnOn() {
        wemoService.turnOn();

        return "ON";
    }

    @RequestMapping(path = "wemo/off", method = GET, produces = TEXT_PLAIN_VALUE)
    public String turnOff() {
        wemoService.turnOff();

        return "OFF";
    }
}
