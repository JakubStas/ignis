package com.jakubstas.ignis.resource;

import com.jakubstas.ignis.model.Readings;
import com.jakubstas.ignis.service.ReadingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadingsController {

    @Autowired
    private ReadingsService readingsService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void processReadings(@RequestBody Readings readings) {
        readingsService.processReadings(readings);
    }
}
