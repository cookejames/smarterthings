#ifndef ssdp_h
#define ssdp_h
#include <ESP8266WebServer.h>

class Ssdp
{
  public:
    Ssdp(ESP8266WebServer*);
  private:
    ESP8266WebServer *http;
};
#endif
