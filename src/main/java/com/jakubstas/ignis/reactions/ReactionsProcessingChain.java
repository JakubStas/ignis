package com.jakubstas.ignis.reactions;

import com.jakubstas.ignis.reactions.reaction.Reaction;
import com.jakubstas.ignis.reactions.reaction.ReactionResult;
import com.jakubstas.ignis.readings.model.Readings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.REACTED_WITH_BREAK;

@Component
public class ReactionsProcessingChain {

    private Logger logger = LoggerFactory.getLogger(ReactionsProcessingChain.class);

    @Autowired
    private List<Reaction> reactions;

    public void run(final Readings readings) {
        for (Reaction reaction : reactions) {
            if (reaction.shouldReact(readings)) {
                final ReactionResult reactionResult = reaction.react(readings);

                if (reactionResult == REACTED_WITH_BREAK) {
                    logger.info("Breaking the reaction chain!");
                    break;
                }
            }
        }
    }
}
