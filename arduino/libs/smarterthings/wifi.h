#ifndef wifi_h
#define wifi_h
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h>
#define DEFAULT_AP_NAME "SmarterThings"

class Wifi
{
  public:
    static void begin();
    static void begin(String);
    static String getSsid();
    static String getSsid(String);
  private:
    Wifi();
};
#endif
