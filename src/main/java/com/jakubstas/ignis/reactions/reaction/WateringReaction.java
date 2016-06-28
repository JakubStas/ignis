package com.jakubstas.ignis.reactions.reaction;

import com.jakubstas.ignis.configuration.IgnisConfiguration;
import com.jakubstas.ignis.personalities.PersonalityMessageSource;
import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
@Order(HIGHEST_PRECEDENCE)
public class WateringReaction extends Reaction {

    private Logger logger = LoggerFactory.getLogger(WateringReaction.class);

    @Autowired
    private PersonalityMessageSource personalityMessageSource;

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    @Override
    public boolean shouldReact(final Readings readings) {
        final int wateringThreshold = configuration.getSensorsConfiguration().getMoisture().getWateringThreshold();

        if (readings.getMoisture() <= wateringThreshold) {
            final String latestTweet = twitterService.getLatestTweet().getText();

            if (!doesMessageMatchTheRegexp(latestTweet)) {
                logger.info("Watering reaction triggered based on moisture level of {}", readings.getMoisture());

                return true;
            }
        }

        logger.info("Watering reaction was not triggered!");

        return false;
    }

    @Override
    protected ReactionResult reactInternal(Readings readings) {
        final String userHandle = configuration.getTwitterConfiguration().getUserHandle();

        final String message = personalityMessageSource.getMessage(getReactionTemplateKey(), userHandle, getTimestamp());

        twitterService.postTweet(message);

        logger.info("Watering reaction successfully tweeted!");

        return REACTED;
    }

    @Override
    protected String getReactionRegexp() {
        return personalityMessageSource.getMessage(getReactionRegexpKey());
    }

    @Override
    protected String getReactionMessageKey() {
        return "reaction.watering";
    }
}
