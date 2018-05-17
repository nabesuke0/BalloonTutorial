# BalloonTutorial

Simple tutorial library.

![license](https://img.shields.io/github/license/mashape/apistatus.svg)

![sample_tutorial](https://user-images.githubusercontent.com/1941369/40185507-b8807396-5a2d-11e8-8770-2478da5ddddc.gif)

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
- hole type
- radius(use circle type)
- text color
- balloon color
- duration of animation
```kotlin
TutorialBuilder.init(this, binding.buttonTutorialSquare)
        .holeType(TutorialBuilder.HoleType.SQUARE)
        .descriptionByString("Square type")
        .balloonColor(Color.BLACK)
        .textColor(Color.WHITE)
	.durationForBaseAnimation(300)
        .durationForHoleAnimation(300)
        .textColor(Color.WHITE)
        .buildAndLayout()
```
