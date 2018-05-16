# BalloonTutorial

Simple tutorial library.

![license](https://img.shields.io/github/license/mashape/apistatus.svg)



# Including in your project
```build.gradle
dependencies {
	implementation 'jp.studio.edamame:balloontutorial:0.2.1'
}
```

# How to use
```kotlin

TutorialBuilder.init(this, target = binding.buttonTutorialCircle)
        .holeType(TutorialBuilder.HoleType.CIRCLE)
        .radiusOfDp(50f)
        .descriptionByString("Circle type")
        .onClickedOutSide {
            // nothing to do
        }
        .onClickedTarget {
            Toast.makeText(this, "tapped circle button", Toast.LENGTH_SHORT).show()
        }
        .buildAndLayout()
        
TutorialBuilder.init(this, binding.buttonTutorialSquare)
        .holeType(TutorialBuilder.HoleType.SQUARE)
        .descriptionByString("Square type")
        .balloonColor(Color.BLACK)
        .textColor(Color.WHITE)
        .buildAndLayout()

```
