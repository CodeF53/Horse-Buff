# Horse Buff
[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png

## About

Removes stupid things about horses.
- Reduce rubberbanding on stairs/slabs "Horse moved wrongly!" ([MC-100830](https://bugs.mojang.com/browse/MC-100830))
- Better Breeding
- Saddled Horses don't wander

Todo
- Remove break speed debuff

## New Breeding Logic

Stays within vanilla min/max horse stats

1. Take average of the parents' traits
2. Random amount added/subtracted from said average
  - +/- 10% of range between max and min
3. clamp to vanilla min/max stat
