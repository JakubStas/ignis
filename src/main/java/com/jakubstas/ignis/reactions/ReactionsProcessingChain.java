package com.jakubstas.ignis.reactions;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED_WITH_BREAK;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import com.jakubstas.ignis.reactions.reaction.Reaction;
import com.jakubstas.ignis.reactions.reaction.ReactionResult;
import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;

@Component
public class ReactionsProcessingChain {

    private Logger logger = LoggerFactory.getLogger(ReactionsProcessingChain.class);

    @Autowired
    private List<Reaction> reactions;

    @Autowired
    private TwitterService twitterService;

    public void run(final Readings readings) {
        final Tweet latestTweet = twitterService.getLatestTweet();

        for (Reaction reaction : reactions) {
            if (reaction.shouldReact(readings, latestTweet)) {
                final ReactionResult reactionResult = reaction.react(readings);

                if (reactionResult == REACTED_WITH_BREAK) {
                    logger.info("Breaking the reaction chain!");
                    break;
                }
            }
        }
    }
}
