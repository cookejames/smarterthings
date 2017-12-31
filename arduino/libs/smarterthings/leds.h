#ifndef leds_h
#define leds_h
#include <FastLED.h>
#define PIN_LED D4

class Leds
{
  public:
    Leds(int);
    void fill(const struct CRGB &);
    void fill(CRGB::HTMLColorCode);
    void fill(int, int, int);
    void off();
    CRGB getCurrentColour();
    boolean isOn() {return status;};
  private:
    CRGB *leds;
    int numLeds;
    CRGB currentColour;
    boolean status = false;
};
#endif
