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
@Order(ReactionPriorities.COLD_DAY)
public class LowTemperatureReaction extends Reaction {

    private Logger logger = LoggerFactory.getLogger(LowTemperatureReaction.class);

    @Autowired
    private PersonalityMessageSource personalityMessageSource;

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    /**
     * Decides whether to compliant about cold day or not. If all of the following conditions are met, the reaction is triggered and this method returns
     * <code>true</code>:
     * <ul>
     * <li>the current temperature reading is less than temperature median - tolerance threshold</li>
     * <li>the latest tweet was not about cold weather</li>
     * </ul>
     * Otherwise this method returns <code>false</code>.
     */
    @Override
    public boolean shouldReact(final Readings readings, final Tweet latestTweet) {
        final int temperatureMedian = configuration.getSensorsConfiguration().getTemperature().getMedian();
        final int temperatureTolerance = configuration.getSensorsConfiguration().getTemperature().getTolerance();
        final int coldTemperatureThreshold = temperatureMedian - temperatureTolerance;

        if (readings.getTemperature() < coldTemperatureThreshold) {
            if (!StringUtils.hasText(latestTweet.getText())) {
                return true;
            }

            if (!doesMessageMatchTheRegexp(latestTweet.getText())) {
                logger.info("Cold day reaction triggered based on temperature level of {}", readings.getTemperature());

                return true;
            }
        }

        logger.info("Cold day reaction was not triggered!");

        return false;
    }

    @Override
    protected ReactionResult reactInternal(Readings readings) {
        final String userHandle = configuration.getTwitterConfiguration().getUserHandle();
        final String message = personalityMessageSource.getMessage(getReactionTemplateKey(), userHandle, getTimestamp());

        twitterService.postTweet(message);

        logger.info("Cold day reaction successfully tweeted!");

        readings.getReaction().reactedWithLowTemperature();

        return REACTED;
    }

    @Override
    protected String getReactionRegexp() {
        return personalityMessageSource.getMessage(getReactionRegexpKey());
    }

    @Override
    protected String getReactionMessageKey() {
        return "reaction.temperatureLow";
    }
}


