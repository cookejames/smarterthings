#include <ssdp.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <ESP8266SSDP.h>

#define NAME "Smart Led Controller"
#define MODEL_NAME "SmartLed"
#define MODEL_NUMBER "smartledv1"

Ssdp::Ssdp(ESP8266WebServer *httpInstance) {
  http = httpInstance;

  Serial.println("Starting SSDP Service");
  http->on("/description.xml", HTTP_GET, [&](){
    SSDP.schema(http->client());
  });
  http->begin();

  SSDP.setSchemaURL("description.xml");
  SSDP.setHTTPPort(80);
  SSDP.setSerialNumber(ESP.getChipId());
  SSDP.setDeviceType("urn:schemas-upnp-org:device:SmartLed:1");
  SSDP.setName(NAME);
  SSDP.setModelName(MODEL_NAME);
  SSDP.setModelNumber(MODEL_NUMBER);
  SSDP.begin();
}
