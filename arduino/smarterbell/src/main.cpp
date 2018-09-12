#include <Arduino.h>
#include "wifi.h"
#include "ssdp.h"
#include "ota.h"
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <ArduinoJson.h>
#define BUTTON_PIN D5
#define RELAY_PIN D2

ESP8266WebServer server(80);
Ssdp ssdp(&server, "Smarter Things Doorbell", "SmarterThingsDoorbell", "stdbv1");
int buttonState = 1;
bool shouldAlarm = true;

void sendSuccess() {
  server.send(200);
}

void sendSuccess(String message) {
  server.send(200, "text/plain", message);
}

void sendSuccess(JsonObject *message) {
  String jsonString;
  message->printTo(jsonString);
  Serial.printf("Sending: %s\n", jsonString.c_str());
  server.send(200, "application/json", jsonString);
}

void sendError(String message) {
  Serial.printf("Error: %s", message.c_str());
  server.send(500, "text/plain", message);
}

void sendStatus() {
  String status = shouldAlarm == true ? "on" : "off";

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject &root = jsonBuffer.createObject();
  root["status"] = status;

  sendSuccess(&root);
}

void handleOn() {
  Serial.println("Turning on");

  shouldAlarm = true;

  sendStatus();
}

void handleOff() {
  Serial.println("Turning off");

  shouldAlarm = false;

  sendStatus();
}

void handleRestart() {
  Serial.println("Restarting");
  sendSuccess();
  ESP.restart();
}

void handleStatus() {
  Serial.println("Status requested");
  sendStatus();
}

void setupHandlers() {
  server.on("/on", handleOn);
  server.on("/off", handleOff);
  server.on("/restart", handleRestart);
  server.on("/status", handleStatus);
}

void handleButtonInterrupt() {
  int currentState = digitalRead(BUTTON_PIN);
  if (currentState != buttonState) {
    Serial.printf("Button state is now: %u\n", currentState);
    buttonState = currentState;

    if (shouldAlarm) {
      if (buttonState == 0) {
        Serial.println("Relay high");
        digitalWrite(RELAY_PIN, LOW);
      } else {
        Serial.println("Relay low");
        digitalWrite(RELAY_PIN, HIGH);
      }
    }
  }
}

void setup() {
  Serial.begin(115200);
  // Setup the interrupt and button before anything else. We want this to
  // work even if there is no Wifi

  // Setup input button
  pinMode(BUTTON_PIN, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(BUTTON_PIN), handleButtonInterrupt, CHANGE);

  // Setup relay
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, HIGH);

  Wifi::begin();
  Ota::begin();

  String hostname = Wifi::getSsid() + ".local";
  if (MDNS.begin(hostname.c_str())) {
    Serial.printf("MDNS responder started from %s\n", hostname.c_str());
  }

  setupHandlers();
  server.begin();
}

void loop() {
    server.handleClient();
    Ota::loop();
}
