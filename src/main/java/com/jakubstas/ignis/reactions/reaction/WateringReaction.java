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

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
@Component
public class WateringReaction implements Reaction {

    private Logger logger = LoggerFactory.getLogger(WateringReaction.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    private final String firstTweetTemplate = "Hey %s, we need some water! (%s)";

    @Override
    public boolean shouldReact(final Readings readings) {
        final int moisture = readings.getMoisture();

        if (moisture <= 750) {
            final String latestTweet = twitterService.getLatestTweet().getText();
            final String tweetPrefix = String.format("Hey %s, we need some water!", configuration.getTwitterConfiguration().getUserHandle());

            if (!latestTweet.startsWith(tweetPrefix)) {
                logger.info("Watering reaction triggered based on moisture level of {}", moisture);

                return true;
            }
        }

        logger.info("Watering reaction was not triggered!");

        return false;
    }

    @Override
    public ReactionResult react(final Readings readings) {
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
        final String userHandle = configuration.getTwitterConfiguration().getUserHandle();
        final String currentDate = simpleDateFormat.format(new Date());

        final String tweet = String.format(tweetTemplate, userHandle, currentDate);
        twitterService.postTweet(tweet);

        logger.info("Watering reaction successfully tweeted!");
    }
}
