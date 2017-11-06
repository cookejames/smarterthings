#include <Arduino.h>
#include "leds.h"
#include "wifi.h"
#include "ssdp.h"
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <ArduinoJson.h>

#define NUM_LEDS 1
Leds leds(NUM_LEDS);
ESP8266WebServer server(80);
Ssdp ssdp(&server);

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
  String status = leds.isOn() ? "on" : "off";

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject &root = jsonBuffer.createObject();
  root["status"] = status;

  sendSuccess(&root);
}

void handleOn() {
  if (server.args() != 3) {
    sendError("Missing required arguments");
  }
  int r = atoi(server.arg("red").c_str());
  int g = atoi(server.arg("green").c_str());
  int b = atoi(server.arg("blue").c_str());
  leds.fill(r, g, b);
  Serial.printf("Set colour R: %d, G: %d, B: %d\n", r, g, b);

  sendStatus();
}

void handleOff() {
  Serial.println("Turning off");
  leds.off();

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

void setup() {
  Serial.begin(115200);
  Wifi::setup();

  if (MDNS.begin(Wifi::getSsid().c_str())) {
    Serial.println("MDNS responder started");
  }

  setupHandlers();
  server.begin();
}

void loop() {
    server.handleClient();
}
