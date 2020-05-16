## The Arch of afengine future follow as:
## the design blue print of albertgame-afengine.
## module design of afengine>
### root package: albertgame.afengine
### core package:
* core.app
* core.graphics
* core.message
* core.util

### inner implements package of engine function:
* in.core.graphics
* in.parts.input
* in.parts.input.ui
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
* afgame-builder(the game studio of afengine)

#### fixed time: 2020/5/16 20:42
#### writer: Albert Flex
