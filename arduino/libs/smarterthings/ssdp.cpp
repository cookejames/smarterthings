#include "ssdp.h"

Ssdp::Ssdp(ESP8266WebServer *httpInstance, const char* name, const char* modelName, const char* modelNumber) {
  http = httpInstance;

  Serial.println("Starting SSDP Service");
  http->on("/description.xml", HTTP_GET, [&](){
    SSDP.schema(http->client());
  });
  http->begin();

  SSDP.setSchemaURL("description.xml");
  SSDP.setHTTPPort(80);
  SSDP.setSerialNumber(ESP.getChipId());
  SSDP.setDeviceType("urn:schemas-upnp-org:device:SmarterThings:1");
  SSDP.setName(name);
  SSDP.setModelName(modelName);
  SSDP.setModelNumber(modelNumber);
  SSDP.begin();
}
