package jp.studio.edamame.balloontutorial

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout


internal abstract class TutorialBaseView internal constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    protected lateinit var target: View

    /**
     * For value animation
     * change hole radius by animation updated
     */
    protected var radius: Float = 0f
    var maxRadius: Float = 0f

    protected val holePaint = Paint().apply { isAntiAlias = true }
    private val basePaint = Paint().apply { isAntiAlias = true }

    var targetCenterX = 0f
    var targetCenterY = 0f

    val targetRect: RectF = RectF()

    protected var density = 0f

    var baseColor = Color.parseColor("#99000000")

    private var latestMotionEvent: MotionEvent? = null
    private var onClickTarget: (() -> Unit)? = null
    private var onClickOutSide: (() -> Unit)? = null

    /**
     * require call
     */
    fun initialize(context: Context, target: View) {
        bringToFront()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)

        this.target = target

        this.holePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        this.density = context.resources.displayMetrics.density
    }

    fun onClickTarget(onclick: () -> Unit) {
        this.onClickTarget = onclick
    }

    fun onClickOutSide(onclick: () -> Unit) {
        this.onClickOutSide = onclick
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        basePaint.color = this.baseColor
        canvas?.let {
            it.drawRect(0F, 0F, it.width.toFloat(), it.height.toFloat(), basePaint)
        }
    }

    fun startDigging(duration: Long, onAnimationEnd: () -> Unit) {
        val anim = ValueAnimator.ofFloat(0f, maxRadius)
        anim.duration = duration

        anim.addUpdateListener { animator ->
            this.radius = animator.animatedValue as Float
            this.invalidate()
        }
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEnd.invoke()
            }

        })

        anim.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // タッチされた座標を取得
        latestMotionEvent = event

        return super.onTouchEvent(event)
    }

    protected fun setupOnClickListener(targetRect: RectF) {
        this.setOnClickListener {

            val onClickTarget = this.onClickTarget
            val motionEvent = latestMotionEvent

            if (onClickTarget != null) {
                if (motionEvent != null
                        && targetRect.left < motionEvent.x
                        && motionEvent.x < targetRect.right
                        && targetRect.top < motionEvent.y
                        && motionEvent.y < targetRect.bottom) {
                    onClickTarget.invoke()
                } else {
                    onClickOutSide?.invoke()
                }

            } else {
                onClickOutSide?.invoke()
            }
        }
    }
}