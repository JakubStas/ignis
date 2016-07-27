package com.jakubstas.ignis.wemo;

import com.jakubstas.ignis.wemo.wsdl.SetBinaryState;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

@Service
public class WemoService extends WebServiceGatewaySupport {

    private static final String SOAP_ACTION_URI = "urn:Belkin:service:basicevent:1#SetBinaryState";

    private static final String ON = "1";

    private static final String OFF = "0";

    public void turnOn() {
        sendStateToWifiPlug(ON);
    }

    public void turnOff() {
        sendStateToWifiPlug(OFF);
    }

    private void sendStateToWifiPlug(final String state) {
        final SetBinaryState setBinaryState = new SetBinaryState();
        setBinaryState.setBinaryState(state);

        getWebServiceTemplate().marshalSendAndReceive(setBinaryState, message -> ((SoapMessage) message).setSoapAction(SOAP_ACTION_URI));
    }
}
