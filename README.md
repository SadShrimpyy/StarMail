# Maintained version (supports 1.8 to 1.21)
The original version of the plugin, therefore the source code, can be found [here](https://gitlab.com/sword7/starmail).
The first commit (15867618) of this repo, contain all and only the source code from the [original repo](https://gitlab.com/sword7/starmail) with project id: 15867618 by the commit id: 18a686f29152649fc0afa0577b4e20b11e002fcf.

*This is simply a version where the original StarMail plugin, created by **sword7**, is retained.*

## Support
If you encounter any kind of bug, contact the dev through Discord (sadshrimpy), or  create a [GitHub Issue](https://github.com/SadShrimpyy/StarMail/issues).

# Welcome
Star Mail is a Spigot plugin that adds mailboxes, letters, and packages to Minecraft. It pays close attention to details to deliver an immersive mailing experience.

Star Mail is supported by all major versions: **from 1.8 to 1.20**!

# Why Star Mail?
Star Mail offers an intuitive and complete mailing system. Players can send letters and packages to each other by using Star Mail's mailbox items. Star Mail protects against a large amount of inactive mail data by adding a configurable expiration duration for mail and package data tracking.
Additionally, Star Mail contains a simple API that allows for many creative implementations and integrations by other Spigot plugins.

# Changes compared to the original
Same as commits, but in a list. In other words every implementation can be found here.
```
[08/23/24 #1] Add support for Minecraft versions 1.19.x, 1.20.x and 1.21.x.

[08/22/24 #4] Improved legacy compatibility through maven modules: spigot-legacy (<1.9), spigot-modern (>1.8.8), spigot-core and commons
              In the original version the code were all in one module causing dependency-priority problems
[08/22/24 #3] Updated plugin.yml and release version
[08/22/24 #2] Introduced dynmap dependency
[08/22/24 #1] Introduced spigot dependency for NMS
 
[08/20/24 #2] Introduced JSR305 dependency to pom.xml to allow the usage of 'javax.annotation.*'
[08/20/24 #1] Fetched source code from origin 'https://gitlab.com/sword7/starmail'
```
