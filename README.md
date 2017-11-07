# Smarter Things

## Introduction
Smarter Things came about through my desire to get simple custom devices based on the ESP8266 micro controller working with Smartthings.

My initial investigations lead me to [ST Anything](https://github.com/DanielOgorchock/ST_Anything/) which was a library initially written for the now discontinued Arduino Thingshield by Samsung. ST Anything has been expanded to support a range of networked Arduino compatible devices such as the ESP8266 with a nice abstraction layer and a load of examples. For me though it had two main problems 1) it relies on static IP addresses for your Smartthigs hub and Arduino. I hate managing static IPs when there are alternatives like SSDP available 2) ST Anything creates an empty "parent" device then a "child" device for each supported sensor type so if you want a simple temperature and humidity sensor you end up with three devices in Smartthings. Not a big problem but I hate the untidiness when there really should only be one device.

Cue my discovery of the excellent [Sonoff Connect](https://community.smartthings.com/t/release-sonoff-sonoff-th-s20-dual-4ch-pow-touch-device-handler-smartapp-5-10-smart-switches/45957) by Eric Maycock (@erocm123) which allows you to flash the ESP8266 based Sonoff mains relays with a custom firmware. Erics work appears to be based on the recommendations by Smartthings to use SSDP to discover networked devices and has been completely stable for me over many months. Sonoff Connect uses the [ESP Easy](https://github.com/letscontrolit/ESPEasy) firmware to provide a REST API, SSDP, Wifi setup and OTA to Sonoff devices unfortunately ESP Easy doesn't have support for WS2812b led strips which was my first use case so I decided to write my own firmware to replicate ESP Easys functions.

## Where it goes from there

My v1 attempt initially only supports controlling WS2812b LED strips with a devices handler based on a mashup of Sonoff Connect and ST Anythings RGB light DH but it wouldn't be hard to make other device handlers and arduino firmwares. I have split all the functionality out into simple classes.

## Installation instructions

### Arduino

Connect up your WS2812b strip to your ESP8266 using pin D4 for data. You can customise the pin in the `leds.h` file if required. The number of leds in your strip can be set in `main.cpp` it is set to 100 if you have more than this you will need to increase the number.

I've used PlatformIO for development so it should be as simple as using PlatformIOs upload function which should install the required dependencies.

Your ESP should boot up and create a wifi hotspot named SmarterThings followed by a number. Connect to this and you should be prompted for your Wifi details.

### Smartthings

I'm assuming that you have added a custom device handler or smart app to Smartthings before.

  1. Install and publish the smarterthings.groovy smart app
  2. Install and publish the smarterledstrip.groovy device handler
  3. In the Smartthings app add the Smarter Things Smart app
  4. Use the Smarter Things app to discover and add your device
