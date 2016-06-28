package com.jakubstas.ignis.reactions.reaction;

import com.jakubstas.ignis.configuration.IgnisConfiguration;
import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.social.DuplicateStatusException;
import org.springframework.stereotype.Component;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
@Component
public class WateringReaction implements Reaction {

    private Logger logger = LoggerFactory.getLogger(WateringReaction.class);

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    private final String firstTweetTemplate = "Hey %s, we need some water!";

    @Override
    public boolean shouldReact(Readings readings) {
        final int moisture = readings.getMoisture();

        if (moisture <= 750) {
            logger.info("Watering reaction trigger based on moisture level of {}", moisture);

            return true;
        }

        return false;
    }

    @Override
    public ReactionResult react(Readings readings) {
        try {
            tweetFirstWaterRequest();

            return REACTED;
        } catch (DuplicateStatusException e) {
            logger.warn("Watering reaction has already been posted!");

            return DID_NOTHING;
        } catch (Throwable t) {
            logger.error("Watering reaction failed! {}", t.getMessage());

            return FAILED;
        }
    }

    private void tweetFirstWaterRequest() {
        tweetWaterRequest(firstTweetTemplate);
    }

    private void tweetWaterRequest(final String tweetTemplate) {
        final String tweet = String.format(tweetTemplate, configuration.getTwitterConfiguration().getUserHandle());
        twitterService.postTweet(tweet);

        logger.info("Watering reaction successfully tweeted!");
    }
}
