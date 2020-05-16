## The arch of afengine future follow as:
## the modules design blue print of albertgame-afengine.
  
### root package: albertgame.afengine
  
### core package:
* core.app
* core.graphics
* core.input
* core.message
* core.util
  
### inner implements package of engine function:
* in.core.graphics
* in.core.input.ui
* in.components.action
* in.components.behavior
* in.components.render
  
### extension implements package of engine function:
* ex.parts.net
* ex.components.agent
* ex.components.render3d
* ex.components.sand
  
### the standard file format of engine:
* .boot(game boot configuration)
* .scene(scene configuration)
* .mod(components configuration)
* .actor(prefeb of action configuration)
* .ui(ui face configuration)
* .propterties(text value of game configuration)
  
### other tech of engine:
* albertgame-builder(the game studio of afengine)
  
### games powered by engine:
* albertgame-series(square,racing,treasure,avg,shoot,craftwar,soccer,antilight,darkworld,lightfly)
* island::fatality
  
#### fixed time: 2020/5/16 22:53
#### writer: Albert Flex
