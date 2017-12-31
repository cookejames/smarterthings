#ifndef ssdp_h
#define ssdp_h
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <ESP8266SSDP.h>

class Ssdp
{
  public:
    Ssdp(ESP8266WebServer*, const char*, const char*, const char*);
  private:
    ESP8266WebServer *http;
};
#endif
