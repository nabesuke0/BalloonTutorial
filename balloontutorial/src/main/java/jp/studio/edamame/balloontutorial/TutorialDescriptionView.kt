package jp.studio.edamame.balloontutorial

import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.View


class TutorialDescriptionView private constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var description: String
    private var density: Float = 0f

    private lateinit var holeType: TutorialBuilder.HoleType

    private val paint = Paint().apply { isAntiAlias = true }

    private var balloonColor: Int = 0
    private var textColor: Int = 0

    // Initialize dp to pixel by density
    private var textSize: Float = 16f
    private var textVerticalMargin: Float = 18f
    private var textHorizontalMargin: Float = 22f
    private var verticalMargin: Float = 7f

    private var triangleCoordinate = TriangleCoordinate().apply {
        height = 17.5f
        width = 25f
        radius = 3f
        margin = 15f
        verticalMargin = 5f
    }

    private var endMargin: Float = 10f
    private var balloonRadius: Float = 5f

    private var shadowRadius = 4f
    private val balloonPath = Path()
    private lateinit var targetRect: RectF

    companion object {
        fun init(
                context: Context,
                description: String,
                targetRect: RectF,
                holeType: TutorialBuilder.HoleType,
                @ColorInt textColor: Int,
                @ColorInt balloonColor: Int
        ) =
                TutorialDescriptionView(context).apply {
                    bringToFront()
                    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    setWillNotDraw(false)

                    this.description = description
                    this.targetRect = targetRect
                    this.holeType = holeType

                    this.density = context.resources.displayMetrics.density

                    // dp to px
                    this.textSize *= this.density
                    this.textVerticalMargin *= this.density
                    this.textHorizontalMargin *= this.density

                    this.triangleCoordinate.updateDimensionByDensity(this.density)

                    this.endMargin *= this.density
                    this.balloonRadius *= this.density

                    this.shadowRadius *= this.density

                    this.verticalMargin *= this.density // for square

                    this.textColor = textColor
                    this.balloonColor = balloonColor
                }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cv = canvas?.let { it } ?: return

        paint.textSize = textSize
        val textWidth = paint.measureText(description)
        val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top

        // 縦の座標を求める
        val textY: Float
        val balloonTop: Float
        val balloonBottom: Float

        val isBalloonTop: Boolean

        if ((targetRect.bottom + textHeight + (textVerticalMargin * 2) + triangleCoordinate.height + triangleCoordinate.verticalMargin + endMargin) < cv.height) {
            // description is bottom of target
            triangleCoordinate.topY = targetRect.bottom + triangleCoordinate.verticalMargin
            if (holeType == TutorialBuilder.HoleType.SQUARE) triangleCoordinate.topY += this.verticalMargin

            balloonTop = triangleCoordinate.topY + triangleCoordinate.height
            balloonBottom = balloonTop + textHeight + (textVerticalMargin * 2)
            textY = balloonTop + textVerticalMargin + (textHeight - paint.fontMetrics.descent)
            isBalloonTop = false

        } else {
            // description is top of target
            triangleCoordinate.topY = targetRect.top - triangleCoordinate.verticalMargin
            if (holeType == TutorialBuilder.HoleType.SQUARE) triangleCoordinate.topY -= this.verticalMargin

            balloonBottom = triangleCoordinate.topY - triangleCoordinate.height
            balloonTop = balloonBottom - textHeight - (textVerticalMargin * 2)
            textY = balloonBottom - textVerticalMargin - paint.fontMetrics.bottom
            isBalloonTop = true
        }

        var balloonLeft: Float
        var balloonRight: Float

        if (holeType == TutorialBuilder.HoleType.CIRCLE) {
            if ((cv.width / 2) < targetRect.centerX()) {
                // balloon is right side

                // 吹き出しがcanvasのwidthから出るか判定する
                val tempBalloonRight = cv.width - (targetRect.centerX() + balloonRadius + (triangleCoordinate.width / 2) + endMargin + triangleCoordinate.margin)
                if (tempBalloonRight < 0) { // 吹き出しがはみ出る
                    val overWidth = Math.abs(tempBalloonRight)
                    if (overWidth <= (endMargin + triangleCoordinate.margin)) {
                        triangleCoordinate.margin -= overWidth
                        if (triangleCoordinate.margin < 0) {
                            endMargin += triangleCoordinate.margin
                            triangleCoordinate.margin = 0f
                        }
                        balloonRight = cv.width - (endMargin + triangleCoordinate.margin)
                        triangleCoordinate.topX = targetRect.centerX()
                    } else {
                        balloonRight = cv.width.toFloat()
                        triangleCoordinate.topX = balloonRight - ((triangleCoordinate.width / 2) + balloonRadius)
                    }
                    balloonLeft = balloonRight - (textWidth + textHorizontalMargin * 2)


                } else { // はみ出ないので、描画位置を計算する
                    // とりあえず右側に合わせる
                    balloonRight = cv.width - endMargin // default
                    balloonLeft = balloonRight - (textWidth + textHorizontalMargin * 2)

                    triangleCoordinate.topX = targetRect.centerX()

                    // バルーンの範囲内にターゲットの中心があるか？
                    when {
                        (balloonLeft + balloonRadius + (triangleCoordinate.width / 2) + triangleCoordinate.margin) > targetRect.centerX() -> {
                            // 範囲の外左側
                            val diff = (balloonLeft + balloonRadius + (triangleCoordinate.width / 2) + triangleCoordinate.margin) - targetRect.centerX()
                            balloonLeft -= diff
                            balloonRight -= diff

                        }
                        targetRect.centerX() > (balloonRight - (balloonRadius + (triangleCoordinate.width / 2))) -> {
                            // 範囲の外右側
                            // 右側に合わせてるのではみ出ることはないはず
                            Log.e("TutorialBaseView", "target outside")
                        }
                        else -> {
                            // 内側。初期値のままでいい
                        }
                    }
                }

            } else {
                // balloon is left side

                // 吹き出しがcanvasのwidthから出るか判定する
                val tempBalloonLeft = targetRect.centerX() - (balloonRadius + (triangleCoordinate.width / 2) + endMargin + triangleCoordinate.margin)

                if (tempBalloonLeft < 0) { // 吹き出しがはみ出る
                    val overWidth = Math.abs(tempBalloonLeft)
                    if (overWidth <= (endMargin + triangleCoordinate.margin)) {
                        triangleCoordinate.margin -= overWidth
                        if (triangleCoordinate.margin < 0) {
                            endMargin += triangleCoordinate.margin
                            triangleCoordinate.margin = 0f
                        }
                        balloonLeft = (endMargin + triangleCoordinate.margin)
                        triangleCoordinate.topX = targetRect.centerX()
                    } else {
                        balloonLeft = 0f
                        triangleCoordinate.topX = balloonLeft + ((triangleCoordinate.width / 2) + balloonRadius)
                    }
                    balloonRight = balloonLeft + (textWidth + textHorizontalMargin * 2)

                } else {
                    balloonLeft = targetRect.centerX() - (endMargin + (triangleCoordinate.width / 2) + balloonRadius + triangleCoordinate.margin) // default
                    balloonRight = balloonLeft + (textWidth + textHorizontalMargin * 2)

                    triangleCoordinate.topX = targetRect.centerX()

                    val overRightWidth = (balloonRight + endMargin) - cv.width
                    if (overRightWidth > 0) {
                        balloonLeft -= overRightWidth
                        balloonRight -= overRightWidth
                    }
                }
            }
        } else {
            // SQUAREの場合は左側に吹き出しの位置を合わせる
            balloonLeft = Math.max(endMargin, targetRect.left)
            triangleCoordinate.topX = balloonLeft + (triangleCoordinate.width / 2) + triangleCoordinate.margin
            balloonRight = balloonLeft + (textWidth + textHorizontalMargin * 2)

            val overRightWidth = (balloonRight + endMargin) - cv.width
            if (overRightWidth > 0) {
                balloonLeft -= overRightWidth
                balloonRight -= overRightWidth
            }
        }

        triangleCoordinate.cornerLeft = triangleCoordinate.topX - (triangleCoordinate.width / 2)
        triangleCoordinate.cornerRight = triangleCoordinate.topX + (triangleCoordinate.width / 2)

        paint.color = balloonColor
        paint.setShadowLayer(shadowRadius, 1f * density, 1f * density, Color.parseColor("#4d000000"))

        // balloon path
        balloonPath.apply {
            val radiusY: Float

            val radiusLeftX = triangleCoordinate.topX - triangleCoordinate.radius
            val radiusRightX = triangleCoordinate.topX + triangleCoordinate.radius

            if (isBalloonTop) {
                radiusY = triangleCoordinate.topY - triangleCoordinate.radius

                moveTo(balloonLeft, balloonTop + balloonRadius)

                // 左上の弧
                cubicTo(balloonLeft, balloonTop + balloonRadius,
                        balloonLeft, balloonTop,
                        balloonLeft + balloonRadius, balloonTop)

                lineTo(balloonRight - balloonRadius, balloonTop)

                // 右上の弧
                cubicTo(balloonRight - balloonRadius, balloonTop,
                        balloonRight, balloonTop,
                        balloonRight, balloonTop + balloonRadius)

                lineTo(balloonRight, balloonBottom - balloonRadius)

                // 右下の弧
                cubicTo(balloonRight, balloonBottom - balloonRadius,
                        balloonRight, balloonBottom,
                        balloonRight - balloonRadius, balloonBottom)

                lineTo(triangleCoordinate.cornerRight, balloonBottom)

                // 吹き出し
                lineTo(radiusRightX, radiusY)
                cubicTo(radiusRightX, radiusY,
                        triangleCoordinate.topX, triangleCoordinate.topY,
                        radiusLeftX, radiusY)
                lineTo(triangleCoordinate.cornerLeft, balloonBottom)

                lineTo(balloonLeft + balloonRadius, balloonBottom)

                // 左下の弧
                cubicTo(balloonLeft + balloonRadius, balloonBottom,
                        balloonLeft, balloonBottom,
                        balloonLeft, balloonBottom - balloonRadius)

            } else {
                radiusY = triangleCoordinate.topY + triangleCoordinate.radius

                moveTo(balloonLeft, balloonTop + balloonRadius)

                // 左上の弧
                cubicTo(balloonLeft, balloonTop + balloonRadius,
                        balloonLeft, balloonTop,
                        balloonLeft + balloonRadius, balloonTop)

                lineTo(triangleCoordinate.cornerLeft, balloonTop)

                // 吹き出し
                lineTo(radiusLeftX, radiusY)
                cubicTo(radiusLeftX, radiusY,
                        triangleCoordinate.topX, triangleCoordinate.topY,
                        radiusRightX, radiusY)
                lineTo(triangleCoordinate.cornerRight, balloonTop)

                lineTo(balloonRight - balloonRadius, balloonTop)

                // 右上の弧
                cubicTo(balloonRight - balloonRadius, balloonTop,
                        balloonRight, balloonTop,
                        balloonRight, balloonTop + balloonRadius)

                lineTo(balloonRight, balloonBottom - balloonRadius)

                // 右下の弧
                cubicTo(balloonRight, balloonBottom - balloonRadius,
                        balloonRight, balloonBottom,
                        balloonRight - balloonRadius, balloonBottom)

                lineTo(balloonLeft + balloonRadius, balloonBottom)

                // 左下の弧
                cubicTo(balloonLeft + balloonRadius, balloonBottom,
                        balloonLeft, balloonBottom,
                        balloonLeft, balloonBottom - balloonRadius)
            }
        }
        cv.drawPath(balloonPath, paint)

        paint.clearShadowLayer()

        paint.color = textColor
        cv.drawText(description, balloonLeft + textHorizontalMargin, textY, paint)
    }

    class TriangleCoordinate {
        var height = 0f
        var width = 0f

        var radius = 0f
        var margin = 0f
        var verticalMargin = 0f

        var topY = 0f
        var topX = 0f
        var cornerLeft = 0f
        var cornerRight = 0f

        fun updateDimensionByDensity(density: Float) {
            height *= density
            width *= density
            radius *= density
            margin *= density
            verticalMargin *= density
        }
    }
}