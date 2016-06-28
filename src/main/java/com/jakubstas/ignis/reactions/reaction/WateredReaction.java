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
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Order(LOWEST_PRECEDENCE)
@Component
public class WateredReaction implements Reaction {

    private Logger logger = LoggerFactory.getLogger(WateredReaction.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    private final String tweetTemplate = "Yum! Enjoying some fresh water! (%s)";

    @Override
    public boolean shouldReact(final Readings readings) {
        final int moisture = readings.getMoisture();

        if (moisture > 750) {
            final String latestTweet = twitterService.getLatestTweet().getText();

            if (!latestTweet.startsWith("Yum! Enjoying some fresh water!")) {
                logger.info("Watered reaction triggered based on moisture level of {}", moisture);

                return true;
            }
        }

        logger.info("Watered reaction was not triggered!");

        return false;
    }

    @Override
    public ReactionResult react(final Readings readings) {
        try {
            tweetWateredReaction();

            return REACTED;
        } catch (DuplicateStatusException e) {
            logger.warn("Watered reaction has already been posted!");

            return DID_NOTHING;
        } catch (Throwable t) {
            logger.error("Watered reaction failed! {}", t.getMessage());

            return FAILED;
        }
    }

    private void tweetWateredReaction() {
        final String currentDate = simpleDateFormat.format(new Date());

        final String tweet = String.format(tweetTemplate, currentDate);
        twitterService.postTweet(tweet);


        logger.info("Watered reaction successfully tweeted!");
    }
}
