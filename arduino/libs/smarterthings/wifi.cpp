#include "wifi.h"

Wifi::Wifi() {}

void Wifi::begin() {
  Wifi::begin((String)DEFAULT_AP_NAME);
}

void Wifi::begin(String apName) {
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
