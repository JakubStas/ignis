package com.jakubstas.ignis.readings.resource;

import com.jakubstas.ignis.reactions.ReactionsProcessingChain;
import com.jakubstas.ignis.readings.model.Readings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadingsController {

    @Autowired
    private ReactionsProcessingChain reactionsProcessingChain;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void processReadings(@RequestBody Readings readings) {
        reactionsProcessingChain.run(readings);
    }
}
