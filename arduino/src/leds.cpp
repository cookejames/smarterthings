#include "leds.h"
#define TEMPERATURE Tungsten40W
#define CORRECTION Typical8mmPixel
// Dim a color by 25% (64/256ths)
#define FADE_PERCENTAGE 256

Leds::Leds(int number) {
  numLeds = number;
  leds = (CRGB *)malloc(sizeof(CRGB) * numLeds);
  CRGBSet ledSet(leds, numLeds);
  FastLED.addLeds<NEOPIXEL, PIN_LED>(leds, numLeds);
  FastLED.setTemperature(TEMPERATURE);
  FastLED.setCorrection(CORRECTION);
  Leds::off();
}

void Leds::fill(const struct CRGB &color) {
  currentColour = color;
  fill_solid(leds, numLeds, color);
  FastLED.show();
  status = true;
}

void Leds::fill(CRGB::HTMLColorCode colour) {
  Leds::fill(CRGB(colour));
}

void Leds::fill(int R, int G, int B) {
  Leds::fill(CRGB(R, G, B));
}

void Leds::off() {
  Leds::fill(CRGB::Black);
  status = false;
}

CRGB Leds::getCurrentColour() {
  return currentColour;
}
