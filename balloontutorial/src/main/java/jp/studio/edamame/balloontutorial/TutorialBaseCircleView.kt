package jp.studio.edamame.balloontutorial

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class TutorialBaseCircleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TutorialBaseView(context, attrs, defStyleAttr) {

    companion object {
        @JvmStatic
        fun init(context: Context, target: View, radiusDp: Float) =
                TutorialBaseCircleView(context).apply {
                    this.initialize(context, target)

                    // dp to px
                    this.maxRadius = radiusDp * this.density

                    val location: IntArray = intArrayOf(0, 0)
                    target.getLocationInWindow(location)
                    targetCenterX = location[0] + (target.width / 2.0f)
                    targetCenterY = location[1] + (target.height / 2.0f)

                    this.targetRect.left = targetCenterX - maxRadius
                    this.targetRect.top = targetCenterY - maxRadius
                    this.targetRect.right = targetCenterX + maxRadius
                    this.targetRect.bottom = targetCenterY + maxRadius

                    this.setupOnClickListener(this.targetRect)
                }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(targetCenterX, targetCenterY, radius, holePaint)
    }
}