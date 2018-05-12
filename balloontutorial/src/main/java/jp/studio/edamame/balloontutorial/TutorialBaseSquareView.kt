package jp.studio.edamame.balloontutorial

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

internal class TutorialBaseSquareView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TutorialBaseView(context, attrs, defStyleAttr) {

    private var holeMargin = 7f
    private var holeRadius = 5f
    private val holeRect = RectF()
    private val drawRect = RectF()

    private var endMargin = 10f

    companion object {
        @JvmStatic
        fun init(context: Context, target: View) =
                TutorialBaseSquareView(context).apply {
                    this.initialize(context, target)

                    this.holeMargin *= density
                    this.endMargin *= density

                    val location: IntArray = intArrayOf(0, 0)
                    target.getLocationInWindow(location)
                    targetCenterX = location[0] + (target.width / 2.0f)
                    targetCenterY = location[1] + (target.height / 2.0f)

                    this.targetRect.left = location[0].toFloat()
                    this.targetRect.top = location[1].toFloat()
                    this.targetRect.right = location[0].toFloat() + target.width
                    this.targetRect.bottom = location[1].toFloat() + target.height

                    holeRect.apply {
                        left = targetRect.left - holeMargin
                        top = targetRect.top - holeMargin
                        right = targetRect.right + holeMargin
                        bottom = targetRect.bottom + holeMargin
                    }

                    this.holeRadius = this.holeRadius * density

                    // height
                    val holeHeight = target.height + this.holeMargin

                    // width
                    val holeWidth = target.width + this.holeMargin

                    maxRadius = Math.max(holeHeight, holeWidth)

                    this.setupOnClickListener(this.targetRect)
                }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cv = canvas?.let { it } ?: return

        // left
        var left = holeRect.centerX() - radius
        if (left < holeRect.left) {
            left = holeRect.left
        }
        if (left - endMargin < 0) {
            left = 0 + endMargin
        }

        // top
        var top = targetCenterY - radius
        if (top < holeRect.top) {
            top = holeRect.top
        }
        if (top - endMargin < 0) {
            top = 0 + endMargin
        }

        // right
        var right = targetCenterX + radius
        if (holeRect.right < right) {
            right = holeRect.right
        }
        if (cv.width < right + endMargin) {
            right = canvas.width - endMargin
        }

        // bottom
        var bottom = targetCenterY + radius
        if (holeRect.bottom < bottom) {
            bottom = holeRect.bottom
        }
        if (cv.height < holeRect.bottom) {
            bottom = canvas.height - endMargin
        }

        drawRect.apply {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }
        cv.drawRoundRect(drawRect, this.holeRadius, this.holeRadius, holePaint)
    }
}