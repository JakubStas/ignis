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
@Order(ReactionPriorities.SUNNY_DAY)
public class SunnyDayReaction extends Reaction {

    private Logger logger = LoggerFactory.getLogger(SunnyDayReaction.class);

    @Autowired
    private PersonalityMessageSource personalityMessageSource;

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    /**
     * Decides whether to celebrate sunny day or not. If all of the following conditions are met, the reaction is triggered and this method returns
     * <code>true</code>:
     * <ul>
     * <li>the current light reading is less or equal to the sunny threshold</li>
     * <li>the latest tweet was not about sunny weather</li>
     * </ul>
     * Otherwise this method returns <code>false</code>.
     */
    @Override
    public boolean shouldReact(final Readings readings) {
        final int sunnyThreshold = configuration.getSensorsConfiguration().getLight().getSunnyThreshold();

        if (readings.getLight() > sunnyThreshold) {
            final Tweet latestTweet = twitterService.getLatestTweet();

            if (!StringUtils.hasText(latestTweet.getText())) {
                return true;
            }

            if (!doesMessageMatchTheRegexp(latestTweet.getText())) {
                logger.info("Sunny day reaction triggered based on light level of {}", readings.getLight());

                return true;
            }
        }

        logger.info("Sunny day reaction was not triggered!");

        return false;
    }

    @Override
    protected ReactionResult reactInternal(Readings readings) {
        final String message = personalityMessageSource.getMessage(getReactionTemplateKey(), getTimestamp());

        twitterService.postTweet(message);

        logger.info("Sunny day reaction successfully tweeted!");

        return REACTED;
    }

    @Override
    protected String getReactionRegexp() {
        return personalityMessageSource.getMessage(getReactionRegexpKey());
    }

    @Override
    protected String getReactionMessageKey() {
        return "reaction.sunnyDay";
    }
}
