# BalloonTutorial

Simple tutorial library.

![license](https://img.shields.io/github/license/mashape/apistatus.svg)

![sample_image](https://user-images.githubusercontent.com/1941369/40153004-a9dc0b0e-59c2-11e8-8967-524855a7469f.png)

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
```

## property
- text color
- balloon color
- duration of animation
```kotlin
TutorialBuilder.init(this, binding.buttonTutorialSquare)
        .holeType(TutorialBuilder.HoleType.SQUARE)
        .descriptionByString("Square type")
        .balloonColor(Color.BLACK)
	.durationForBaseAnimation(300)
        .durationForHoleAnimation(300)
        .textColor(Color.WHITE)
        .buildAndLayout()
```
