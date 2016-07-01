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
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED_WITH_BREAK;

@Component
@Order(ReactionPriorities.FERTILIZING)
public class FertilizingReaction extends Reaction {

    private final ZoneId zoneId = ZoneId.of("Europe/Dublin");

    private Logger logger = LoggerFactory.getLogger(FertilizingReaction.class);

    @Autowired
    private PersonalityMessageSource personalityMessageSource;

    @Autowired
    private IgnisConfiguration configuration;

    @Autowired
    private TwitterService twitterService;

    /**
     * Decides whether to ask for fertilizers or not. If all of the following conditions are met, the reaction is triggered and this method returns <code>true</code>:
     * <ul>
     * <li>the current moisture reading is less or equal to the watering threshold</li>
     * <li>it is Wednesday</li>
     * <li>the latest tweet was not asking for fertilizer</li>
     * </ul>
     * Otherwise this method returns <code>false</code>.
     */
    @Override
    public boolean shouldReact(final Readings readings) {
        final int wateringThreshold = configuration.getSensorsConfiguration().getMoisture().getWateringThreshold();

        if (readings.getMoisture() <= wateringThreshold && isItWednesday()) {
            final Tweet latestTweet = twitterService.getLatestTweet();

            if (!StringUtils.hasText(latestTweet.getText())) {
                return true;
            }

            if (!doesMessageMatchTheRegexp(latestTweet.getText())) {
                logger.info("Fertilizing reaction triggered based on moisture level of {} and the day being Wednesday", readings.getMoisture());

                return true;
            }
        }

        logger.info("Fertilizing reaction was not triggered!");

        return false;
    }

    private boolean isItWednesday() {
        return LocalDateTime.now(zoneId).getDayOfWeek() == DayOfWeek.WEDNESDAY;
    }

    @Override
    protected ReactionResult reactInternal(Readings readings) {
        final String userHandle = configuration.getTwitterConfiguration().getUserHandle();
        final String message = personalityMessageSource.getMessage(getReactionTemplateKey(), userHandle, getTimestamp());

        twitterService.postTweet(message);

        logger.info("Fertilizing reaction successfully tweeted!");

        return REACTED_WITH_BREAK;
    }

    @Override
    protected String getReactionRegexp() {
        return personalityMessageSource.getMessage(getReactionRegexpKey());
    }

    @Override
    protected String getReactionMessageKey() {
        return "reaction.fertilizing";
    }
}
