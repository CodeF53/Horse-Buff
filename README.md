# Horse Buff
![Banner_Huge](https://user-images.githubusercontent.com/37855219/141597249-7a6a7d25-8da8-43ac-a10f-bd12d3e1577f.png)
[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png

## About
Improve horses by removing stupid stuff and adding useful stuff.

### Server Changes:
<details><summary>
Allow travel through portals without dismounting <a href="https://bugs.mojang.com/browse/MC-67">(MC-67)</a>*
</summary>
<sub><sup>*only fixes the bug for players riding vehicles as fully fixing the bug would screw with technical minecraft</sup></sub>

![image](https://i.imgur.com/cvph0Ye.gif)

![image](https://i.imgur.com/JeJNEgO.gif)

</details>
<details><summary> Fix rubberbanding on stairs/slabs "Horse moved wrongly!" <a href="https://bugs.mojang.com/browse/MC-100830">(MC-100830)</a></summary>

![image](https://i.imgur.com/bXu7lt4.gif)

</details>
<details><summary> Better Breeding Logic *</summary>

1. Take average of the parents' traits
2. Random amount added/subtracted from said average
      - +/- 10% of range between max and min
3. clamp to vanilla min/max stat

(removed in 1.19.4 because they fixed it)
</details>
<details><summary> Saddled Horses don't wander</summary>

![image](https://i.imgur.com/E0ZGzMU.gif)

</details>
<details><summary> Remove break speed debuff</summary>

Allows players on horseback to mine at full speed.\
Minecraft dramatically lowers break speed while a player is not on the ground.

![image](https://i.imgur.com/DKqrGOD.gif)

</details>
<details><summary> Improve "Step Height" capability </summary>

Increases horse step-height from 1 block to 1.1 blocks.\
This makes riding horses around path blocks way less frustrating.

![image](https://i.imgur.com/nvssVeA.gif)

</details>

### Client Changes:
<details><summary> Allow swimming while Mounted </summary>

Normally Horses will sink in water when you are riding them.\
This makes you float on the surface as if they weren't mounted.\
The default config turns this ability off for skeleton and zombie horses, and camels.\
Have you ever seen a camel swim? I think not!

</details>

<details><summary> Prevent "Bucking" </summary>

Normally horses will randomly "Buck", stopping dead in their tracks while you are riding them.\
This prevents them from doing that.

</details>

<details><summary> Fade Horse as you look down</summary>

![image](https://i.imgur.com/kwtmFVd.gif)

</details>

<details><summary> Horse Head Pitch offset</summary>

![image](https://user-images.githubusercontent.com/37855219/163890939-87f3b255-176a-49df-ad4d-ec5b41a3b54a.png)

</details>

<details><summary> Horses named `jeb_` become rainbow</summary>

![image](https://i.imgur.com/QTk8w33.gif)

</details>
<details><summary> Hotkey to open survival inventory</summary>

![image](https://i.imgur.com/8oOswAR.png)
* Pre 1.20.2
  * Horse Buff pulls from your Minecraft keybinds, so if you change your inventory/sprint key, that will change what you need to press to access your survival inventory
* Post 1.20.2
  * A keybind for opening your inventory while on a horse has been added. ALT + Inv (ALT + E) is now the default, but you can change it to whatever you like

</details>
