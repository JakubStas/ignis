package com.jakubstas.ignis.reactions.reaction;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jakubstas.ignis.configuration.IgnisConfiguration;
import com.jakubstas.ignis.personality.PersonalityMessageSource;
import com.jakubstas.ignis.reactions.ReactionPriorities;
import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;

@Component
@Order(ReactionPriorities.FERTILIZED)
public class FertilizedReaction extends Reaction {

    private final Logger logger = LoggerFactory.getLogger(FertilizedReaction.class);

    @Autowired
    private PersonalityMessageSource personalityMessageSource;

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private FertilizingReaction fertilizingReaction;

    /**
     * Decides whether to thank for fertilizers or not. If all of the following conditions are met, the reaction is triggered and this method returns
     * <code>true</code>:
     * <ul>
     * <li>the current moisture reading is greater than the watering threshold</li>
     * <li>the latest tweet was asking for fertilizer</li>
     * </ul>
     * Otherwise this method returns <code>false</code>.
     */
    @Override
    public boolean shouldReact(final Readings readings, final Tweet latestTweet) {
        final int wateringThreshold = configuration.getSensorsConfiguration().getMoisture().getWateringThreshold();

        if (readings.getMoisture() > wateringThreshold) {
            if (!StringUtils.hasText(latestTweet.getText())) {
                return true;
            }

            if (fertilizingReaction.doesMessageMatchTheRegexp(latestTweet.getText())) {
                logger.info("Fertilized reaction triggered based on moisture level of {}", readings.getMoisture());

                return true;
            }
        }

        logger.info("Fertilized reaction was not triggered!");

        return false;
    }

    @Override
    protected ReactionResult reactInternal(Readings readings) {
        final String message = personalityMessageSource.getMessage(getReactionTemplateKey(), getTimestamp());

        twitterService.postTweet(message);

        logger.info("Fertilized reaction successfully tweeted!");

        return REACTED;
    }

    @Override
    protected String getReactionRegexp() {
        return personalityMessageSource.getMessage(getReactionRegexpKey());
    }

    @Override
    protected String getReactionMessageKey() {
        return "reaction.fretilized";
    }
}
