#ifndef wifi_h
#define wifi_h
#include <Arduino.h>
#define DEFAULT_AP_NAME "SmarterThings"

class Wifi
{
  public:
    static void setup();
    static void setup(String);
    static String getSsid();
    static String getSsid(String);
  private:
    Wifi();
};
#endif
