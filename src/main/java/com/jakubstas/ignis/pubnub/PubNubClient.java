package com.jakubstas.ignis.pubnub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jakubstas.ignis.configuration.PubNubConfiguration;
import com.jakubstas.ignis.readings.model.Reaction;
import com.jakubstas.ignis.readings.model.Readings;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PubNubClient {

    private final Logger logger = LoggerFactory.getLogger(PubNubClient.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PubNub pubNub;

    @Autowired
    private PubNubConfiguration pubNubConfiguration;

    @Autowired
    public PubNubClient(PubNub pubNub) {
        this.pubNub = pubNub;

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public void publishReadings(final Readings readings) {
        publish(pubNubConfiguration.getReadingsChannel(), objectMapper.valueToTree(readings));
    }

    public void publishReaction(final Reaction reaction) {
        publish(pubNubConfiguration.getReactionChannel(), objectMapper.valueToTree(reaction));
    }

    private void publish(final String channel, final JsonNode jsonNode) {
        pubNub.publish().message(jsonNode).channel(channel).shouldStore(true).usePOST(true).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if (status.isError()) {
                    logger.error("Publishing to PubNub failed");
                }
            }
        });
    }
}
