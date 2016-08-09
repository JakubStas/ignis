package com.jakubstas.ignis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pubnub")
public class PubNubConfiguration {

    private String subscribeKey;

    private String publishKey;

    private String uuid;

    private String channel;

    public String getSubscribeKey() {
        return subscribeKey;
    }

    public void setSubscribeKey(String subscribeKey) {
        this.subscribeKey = subscribeKey;
    }

    public String getPublishKey() {
        return publishKey;
    }

    public void setPublishKey(String publishKey) {
        this.publishKey = publishKey;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "PubNubConfiguration{" +
                "subscribeKey='" + subscribeKey + '\'' +
                ", publishKey='" + publishKey + '\'' +
                ", uuid='" + uuid + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
