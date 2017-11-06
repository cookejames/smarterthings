#include <wifi.h>
#include <ESP8266WiFi.h>          //https://github.com/esp8266/Arduino

//needed for library
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h>         //https://github.com/tzapu/WiFiManager

#define DEFAULT_AP_NAME "SmartLed"

Wifi::Wifi() {}

void Wifi::setup() {
  Wifi::setup((String)DEFAULT_AP_NAME);
}

void Wifi::setup(String apName) {
  WiFiManager wifiManager;
  wifiManager.autoConnect(Wifi::getSsid().c_str());
}

String Wifi::getSsid() {
  return Wifi::getSsid((String)DEFAULT_AP_NAME);
}

String Wifi::getSsid(String apName) {
  String ssid = apName + String(ESP.getChipId());
  return ssid;
}
