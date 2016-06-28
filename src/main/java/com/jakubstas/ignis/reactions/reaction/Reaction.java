package com.jakubstas.ignis.reactions.reaction;

import com.jakubstas.ignis.readings.model.Readings;

public interface Reaction {

    boolean shouldReact(final Readings readings);

    ReactionResult react(final Readings readings);
}
