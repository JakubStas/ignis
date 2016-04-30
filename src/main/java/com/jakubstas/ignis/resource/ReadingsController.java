package com.jakubstas.ignis.resource;

import com.jakubstas.ignis.social.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReadingsController {

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Tweet> getFeed() {
        return twitterService.getTweets();
    }
}
