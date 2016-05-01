#include <Digital_Light_TSL2561.h>
#include <Time.h>
#include <Wire.h>
#include <DHT.h>
#include <SoftwareSerial.h>
#include "WizFi250.h"

#define SSID "SSID"
#define KEY "KEY"
#define AUTH "WPA2"

#define spi_CS  8

#define SERVER_IP "SERVER_IP"
#define SERVER_PORT 8080
#define LOCAL_PORT 9000

#define DHTTYPE DHT11

int moistureSensorPin = A0;
int humiditySensorPin = 2;

WizFi250 wizfi250(Serial);
DHT dht(humiditySensorPin, DHTTYPE);

void setup() {
  Serial.begin(115200);
  Wire.begin();
  dht.begin();
  TSL2561.init();

  pinMode(spi_CS,OUTPUT);
  digitalWrite(spi_CS,HIGH);

  delay(1000);
  Serial.println("Joining " SSID );

  delay(10);
  if (wizfi250.join(SSID, KEY, AUTH)) {
    Serial.println("Successfully joined  "  SSID);
    wizfi250.clear();
  } else {
     Serial.println("Failed to join " SSID);
  }
}

void loop() {
  int moistureSensorValue = analogRead(moistureSensorPin);
  int lightSensorValue = TSL2561.readVisibleLux();
  int temperatureSensorValue = (int) dht.readTemperature(); // float
  int humiditySensorValue = (int) dht.readHumidity(); // float

//  Serial.print("moistureSensorValue = ");
//  Serial.println(moistureSensorValue);
//  Serial.print("lightSensorValue = ");
//  Serial.println(lightSensorValue);
//  Serial.print("humiditySensorValue = ");
//  Serial.println(humiditySensorValue);
//  Serial.print("temperatureSensorValue = ");
//  Serial.println(temperatureSensorValue);

  String body = getBody();

  String path = "POST / HTTP/1.1\r\n";
  path += getHeaders(body.length());
  path += body;

  //Serial.println("Path = " + path);

  wizfi250.connect(SERVER_IP,SERVER_PORT,LOCAL_PORT);
  delay(10);

  wizfi250.send(path.c_str());
  delay(1000);

  wizfi250.clear();
  delay(900000);
}

String getHeaders(int contentLength) {
  String headers = "";
  headers += "Content-Type: application/json\r\n";
  headers += "Content-Length: ";
  headers += contentLength;
  headers += "\r\n";
  headers += "Host: ";
  headers += SERVER_IP;
  headers += ":";
  headers += SERVER_PORT;
  headers += "\r\n\r\n";

  //Serial.println("Headers = " + headers);

  return headers;
}

String getBody() {
  int moistureSensorValue = analogRead(moistureSensorPin);
  int lightSensorValue = TSL2561.readVisibleLux();
  int temperatureSensorValue = (int) dht.readTemperature(); // float
  int humiditySensorValue = (int) dht.readHumidity(); // float

  String body = "{";
  body += "\"moisture\":";
  body += moistureSensorValue;
  body += ",";
  body += "\"light\":";
  body += lightSensorValue;
  body += ",";
  body += "\"temperature\":";
  body += temperatureSensorValue;
  body += ",";
  body += "\"humidity\":";
  body += humiditySensorValue;
  body += "}\r\n\r\n";

  //Serial.println("Body = " + body);

  return body;
}