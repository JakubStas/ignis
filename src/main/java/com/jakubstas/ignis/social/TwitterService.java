package com.jakubstas.ignis.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterService {

    private Twitter twitter;

    @Autowired
    public TwitterService(Twitter twitter) {
        this.twitter = twitter;
    }

    public void postTweet(final String message) {
        twitter.timelineOperations().updateStatus(message);
    }

    /**
     * This method expects that the twitter account has at least one tweet published.
     */
    public Tweet getLatestTweet() {
        return twitter.timelineOperations().getHomeTimeline(1).get(0);
    }

    public List<Tweet> getTweets() {
        return twitter.timelineOperations().getHomeTimeline();
    }
}
