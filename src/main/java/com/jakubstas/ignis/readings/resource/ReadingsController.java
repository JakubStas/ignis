package com.jakubstas.ignis.readings.resource;

import com.jakubstas.ignis.pubnub.PubNubClient;
import com.jakubstas.ignis.reactions.ReactionsProcessingChain;
import com.jakubstas.ignis.readings.model.Readings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ReadingsController {

    @Autowired
    private ReactionsProcessingChain reactionsProcessingChain;

    @Autowired
    private PubNubClient pubNubClient;

    private Readings latestReadings = new Readings();

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    public void processReadings(@RequestBody Readings readings) {
        latestReadings = readings;

        reactionsProcessingChain.run(readings);

        pubNubClient.publishReadings(readings);
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public Readings getLatestReadings() {
        return latestReadings;
    }
}
