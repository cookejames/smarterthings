#ifndef ota_h
#define ota_h
#include <ESP8266mDNS.h>
#include <ArduinoOTA.h>

class Ota
{
  public:
    static void setup();
    static void loop();
  private:
    Ota();
};
#endif
