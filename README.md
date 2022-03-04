# Horse Buff
![Banner_Huge](https://user-images.githubusercontent.com/37855219/141597249-7a6a7d25-8da8-43ac-a10f-bd12d3e1577f.png)
[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png

## About
Removes stupid things about horses.
- Fix rubberbanding on stairs/slabs "Horse moved wrongly!" ([MC-100830](https://bugs.mojang.com/browse/MC-100830))
- Better Breeding Logic
- Saddled Horses don't wander
- Remove break speed debuff
- Allow nether portal travel without dismounting

## New Breeding Logic

Stays within vanilla min/max horse stats

1. Take average of the parents' traits
2. Random amount added/subtracted from said average
   - +/- 10% of range between max and min
3. clamp to vanilla min/max stat
