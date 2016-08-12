package com.jakubstas.ignis.reactions;

import com.jakubstas.ignis.pubnub.PubNubClient;
import com.jakubstas.ignis.reactions.reaction.Reaction;
import com.jakubstas.ignis.reactions.reaction.ReactionResult;
import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED;
import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED_WITH_BREAK;

@Component
public class ReactionsProcessingChain {

    private Logger logger = LoggerFactory.getLogger(ReactionsProcessingChain.class);

    private final long fifteenMinutesInMillis = 900_000L;

    @Autowired
    private List<Reaction> reactions;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private PubNubClient pubNubClient;

    private Date latestTweetPulledTime;

    public void run(final Readings readings) {
        if (latestTweetPulledTime == null || shouldPullDownTheLatestTweet()) {
            final Tweet latestTweet = twitterService.getLatestTweet();

            latestTweetPulledTime = new Date();

            for (Reaction reaction : reactions) {
                if (reaction.shouldReact(readings, latestTweet)) {
                    final ReactionResult reactionResult = reaction.react(readings);

                    if (reactionResult == REACTED || reactionResult == REACTED_WITH_BREAK) {
                        pubNubClient.publishReaction(readings.getReaction());
                    }

                    if (reactionResult == REACTED_WITH_BREAK) {
                        logger.info("Breaking the reaction chain!");
                        break;
                    }
                }
            }
        }
    }

    private boolean shouldPullDownTheLatestTweet() {
        if (latestTweetPulledTime == null) {
            return true;
        }

        final long latestTweetPulledMillis = latestTweetPulledTime.getTime();
        final long nowMillis = new Date().getTime();

        if (nowMillis - latestTweetPulledMillis >= fifteenMinutesInMillis) {
            return true;
        }

        return false;
    }
}