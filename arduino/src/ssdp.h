#ifndef ssdp_h
#define ssdp_h
#define NAME "Smarter Things Led Strip"
#define MODEL_NAME "SmarterThingsLedStrip"
#define MODEL_NUMBER "stlsv1"

#include <ESP8266WebServer.h>

class Ssdp
{
  public:
    Ssdp(ESP8266WebServer*);
  private:
    ESP8266WebServer *http;
};
#endif
