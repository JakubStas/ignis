package com.jakubstas.ignis.reactions.reaction;

import com.google.common.base.Joiner;
import com.jakubstas.ignis.readings.model.Readings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.DuplicateStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jakubstas.ignis.reactions.reaction.ReactionResult.DID_NOTHING;
import static com.jakubstas.ignis.reactions.reaction.ReactionResult.FAILED;

public abstract class Reaction {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    private final ZoneId zoneId = ZoneId.of("Europe/Dublin");

    private final String templateKeySuffix = "template";

    private final String regexpKeySuffix = "regexp";

    private Logger logger = LoggerFactory.getLogger(Reaction.class);

    public abstract boolean shouldReact(final Readings readings);

    protected abstract ReactionResult reactInternal(final Readings readings);

    protected abstract String getReactionMessageKey();

    public final ReactionResult react(final Readings readings) {
        try {
            return reactInternal(readings);
        } catch (DuplicateStatusException e) {
            logger.warn("Reaction has already been posted!");

            return DID_NOTHING;
        } catch (Throwable t) {
            logger.error("Reaction failed! {}", t.getMessage());

            return FAILED;
        }
    }

    protected final String getReactionTemplateKey() {
        return Joiner.on('.').join(getReactionMessageKey(), templateKeySuffix);
    }

    protected final String getReactionRegexpKey() {
        return Joiner.on('.').join(getReactionMessageKey(), regexpKeySuffix);
    }

    protected abstract String getReactionRegexp();

    protected final boolean doesMessageMatchTheRegexp(final String message) {
        final Pattern pattern = Pattern.compile(getReactionRegexp());
        final Matcher matcher = pattern.matcher(message);

        return matcher.matches();
    }

    protected final String getTimestamp() {
        return dateTimeFormatter.format(LocalDateTime.now(zoneId));
    }
}
