#ifndef ssdp_h
#define ssdp_h
#define NAME "Smart Led Controller"
#define MODEL_NAME "SmartLed"
#define MODEL_NUMBER "smartledv1"

#include <ESP8266WebServer.h>

class Ssdp
{
  public:
    Ssdp(ESP8266WebServer*);
  private:
    ESP8266WebServer *http;
};
#endif
