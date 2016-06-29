package com.jakubstas.ignis.reactions.reaction;

import com.jakubstas.ignis.configuration.IgnisConfiguration;
import com.jakubstas.ignis.personality.PersonalityMessageSource;
import com.jakubstas.ignis.reactions.ReactionPriorities;
import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED;

@Component
@Order(ReactionPriorities.WATERED)
public class WateredReaction extends Reaction {

    private final Logger logger = LoggerFactory.getLogger(WateredReaction.class);

    @Autowired
    private PersonalityMessageSource personalityMessageSource;

    @Autowired
    private WateringReaction wateringReaction;

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    /**
     * Decides whether to thank for water or not. If all of the following conditions are met, the reaction is triggered and this method returns <code>true</code>:
     * <ul>
     * <li>the current moisture reading is greater than the watering threshold</li>
     * <li>the latest tweet was asking for water</li>
     * </ul>
     * Otherwise this method returns <code>false</code>.
     */
    @Override
    public boolean shouldReact(final Readings readings) {
        final int wateringThreshold = configuration.getSensorsConfiguration().getMoisture().getWateringThreshold();

        if (readings.getMoisture() > wateringThreshold) {
            final String latestTweet = twitterService.getLatestTweet().getText();

            if (wateringReaction.doesMessageMatchTheRegexp(latestTweet)) {
                logger.info("Watered reaction triggered based on moisture level of {}", readings.getMoisture());

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
