# Horse Buff
![Banner_Huge](https://user-images.githubusercontent.com/37855219/141597249-7a6a7d25-8da8-43ac-a10f-bd12d3e1577f.png)
[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png

## About
Improve horses by removing stupid stuff and adding useful stuff.

###Server Changes:
- Allow nether portal travel without dismounting ([MC-67](https://bugs.mojang.com/browse/MC-67))*
- Fix rubberbanding on stairs/slabs "Horse moved wrongly!" ([MC-100830](https://bugs.mojang.com/browse/MC-100830))
- Better Breeding Logic
- Saddled Horses don't wander
- Remove break speed debuff\
<sub><sup>*only fixes the bug for players riding vehicles as fully fixing the bug would screw with technical minecraft</sup></sub>

###Client Changes:
- Fade Horse as you look down
- Horse Head Pitch offset
- Horses named `jeb_` become rainbow

## New Breeding Logic
1. Take average of the parents' traits
2. Random amount added/subtracted from said average
   - +/- 10% of range between max and min
3. clamp to vanilla min/max stat
