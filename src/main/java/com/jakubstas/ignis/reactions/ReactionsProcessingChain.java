package com.jakubstas.ignis.reactions;

import com.jakubstas.ignis.reactions.reaction.Reaction;
import com.jakubstas.ignis.readings.model.Readings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReactionsProcessingChain {

    @Autowired
    private List<Reaction> reactions;

    public void run(final Readings readings) {
        for (Reaction reaction : reactions) {
            if (reaction.shouldReact(readings)) {
                reaction.react(readings);
            }
        }
    }
}
