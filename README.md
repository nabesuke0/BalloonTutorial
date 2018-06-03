# BalloonTutorial

Simple tutorial library.

![license](https://img.shields.io/github/license/mashape/apistatus.svg)

![sample_tutorial](https://user-images.githubusercontent.com/1941369/40185507-b8807396-5a2d-11e8-8770-2478da5ddddc.gif)

# Including in your project
```build.gradle
dependencies {
	implementation 'jp.studio.edamame:balloontutorial:0.2.2'
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

### ビルダーを初期化

```kotlin
TutorialBuilder.init(activituy, target = targetView)
```

 `.init(activity, targetView)` で初期化します。

### 対象ハイライトする形を指定する

```kotlin
.holeType(type: TutorialBuilder.HoleType)
```

 - 四角形

```kotlin
.holeType(TutorialBuilder.HoleType.SQUARE)
```

 - 円形

```kotlin
.holeType(TutorialBuilder.HoleType.CIRCLE)
.radiusOfDp(50f) // 円形の場合は半径を指定する(dp)
```
        
### 吹き出しを設定

```kotlin
.descriptionByString("説明が入ります") // 吹き出しの文字
.textColor(@ColorInt color: Int) // 文字色
.balloonColor(@ColorInt color: Int) // 吹き出しの色
```

### タップの動作を指定

```kotlin
.onClickedOutSide { } // ターゲットの外側をタップされたとき
.onClickedTarget { } // ターゲットをタップされたとき
```

### アニメーションの時間を指定
 - msecで指定

```kotlin
.durationForBaseAnimation(150) // 背景の表示
.durationForHoleAnimation(300) // ハイライトの表示
.durationForBalloonTextAnimation(200) // 吹き出し
```


### ビルダーを初期化

```kotlin
TutorialBuilder.init(activituy, target = targetView)
```

 `.init(activity, targetView)` で初期化します。

### 対象ハイライトする形を指定する

```kotlin
.holeType(type: TutorialBuilder.HoleType)
```

		* 四角形

```kotlin
.holeType(TutorialBuilder.HoleType.SQUARE)
```

		* 円形

```kotlin
.holeType(TutorialBuilder.HoleType.CIRCLE)
.radiusOfDp(50f) // 円形の場合は半径を指定する(dp)
```
        
### 吹き出しを設定

```kotlin
.descriptionByString("説明が入ります") // 吹き出しの文字
.textColor(@ColorInt color: Int) // 文字色
.balloonColor(@ColorInt color: Int) // 吹き出しの色
```

### タップの動作を指定

```kotlin
.onClickedOutSide { } // ターゲットの外側をタップされたとき
.onClickedTarget { } // ターゲットをタップされたとき
```

### アニメーションの時間を指定
- msecで指定

```kotlin
.durationForBaseAnimation(150) // 背景の表示
.durationForHoleAnimation(300) // ハイライトの表示
.durationForBalloonTextAnimation(200) // 吹き出し
```


# ライセンス
```
MIT License

Copyright (c) 2018 Kazuya Watanabe

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
