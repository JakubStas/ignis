package com.jakubstas.ignis.reactions.reaction;

import com.jakubstas.ignis.personalities.PersonalityMessageSource;
import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Component
@Order(LOWEST_PRECEDENCE)
public class WateredReaction extends Reaction {

    private final Logger logger = LoggerFactory.getLogger(WateredReaction.class);

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private PersonalityMessageSource personalityMessageSource;

    @Override
    public boolean shouldReact(final Readings readings) {
        final int moisture = readings.getMoisture();

        if (moisture > 750) {
            final String latestTweet = twitterService.getLatestTweet().getText();

            if (!doesMessageMatchTheRegexp(latestTweet)) {
                logger.info("Watered reaction triggered based on moisture level of {}", moisture);

                return true;
            }
        }

        logger.info("Watered reaction was not triggered!");

        return false;
    }

    @Override
    protected ReactionResult reactInternal(Readings readings) {
        final String message = personalityMessageSource.getMessage(getReactionTemplateKey(), getTimestamp());

        twitterService.postTweet(message);

        logger.info("Watered reaction successfully tweeted!");

        return REACTED;
    }

    @Override
    protected String getReactionRegexp() {
        return personalityMessageSource.getMessage(getReactionRegexpKey());
    }

    @Override
    protected String getReactionMessageKey() {
        return "reaction.watered";
    }
}
