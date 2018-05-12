package jp.studio.edamame.balloontutorial

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Handler
import android.support.annotation.ColorInt
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.lang.ref.WeakReference

class TutorialBuilder private constructor(activity: Activity, target: View) {
    enum class HoleType {
        CIRCLE,
        SQUARE
    }

    private var weakRefActivity: WeakReference<Activity> = WeakReference(activity)
    private var weakRefTutorialView: WeakReference<TutorialBaseView>? = null
    private var weakTargetView: WeakReference<View> = WeakReference(target)

    private var description: String? = null
    private var holeType: HoleType = HoleType.CIRCLE
    private var radiusOfDp: Float = 100.0f

    private var balloonColor = Color.parseColor("#4285f4")
    private var textColor = Color.WHITE

    private var durationForBaseAnimation = 100L
    private var durationForBalloonTextAnimation = 150L
    private var durationForHoleAnimation = 100L

    private var onClickOutSide: () -> Unit = {
        hideTutorial()
    }
    private var onClickTarget: (() -> Unit)? = null

    fun descriptionByString(text: String): TutorialBuilder {
        this.description = text
        return this
    }

    fun holeType(type: HoleType): TutorialBuilder {
        this.holeType = type
        return this
    }

    fun balloonColor(@ColorInt color: Int): TutorialBuilder {
        this.balloonColor = color
        return this
    }

    fun textColor(@ColorInt color: Int): TutorialBuilder {
        this.textColor = color
        return this
    }

    fun durationForBaseAnimation(duration: Long): TutorialBuilder {
        this.durationForBaseAnimation = duration
        return this
    }

    fun durationForBalloonTextAnimation(duration: Long): TutorialBuilder {
        this.durationForBalloonTextAnimation = duration
        return this
    }

    fun durationForHoleAnimation(duration: Long): TutorialBuilder {
        this.durationForHoleAnimation = duration
        return this
    }

    fun onClickedTarget(onclick: () -> Unit): TutorialBuilder {
        this.onClickTarget = onclick
        return this
    }

    fun onClickedOutSide(onclick: () -> Unit): TutorialBuilder {
        this.onClickOutSide = onclick
        return this
    }

    /**
     * for circle type
     */
    fun radiusOfDp(radius: Float): TutorialBuilder {
        this.radiusOfDp = radius
        return this
    }

    fun buildAndLayout(): TutorialBuilder {
        val targetView = weakTargetView.get()?.let { it } ?: return this

        if (targetView.width == 0 && targetView.height == 0) {
            ViewOnLayoutListener.target(targetView).listen {
                showTutorialView(targetView)
            }
        } else {
            Handler().post {
                showTutorialView(targetView)
            }
        }

        return this
    }

    private fun showTutorialView(target: View) {
        val activity = this.weakRefActivity.get()
        if (activity == null) {
            Log.e("TutorialBuilder", "activity is null")
            return
        }

        val tutorialView = when (holeType) {
            HoleType.CIRCLE -> {
                TutorialBaseCircleView.init(activity, target, radiusOfDp)
            }
            HoleType.SQUARE -> {
                TutorialBaseSquareView.init(activity, target)
            }
        }
        weakRefTutorialView = WeakReference(tutorialView)

        tutorialView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (activity.window.decorView as ViewGroup).addView(tutorialView)

        // このネストなんとかしたい
        startAlphaAnimation(tutorialView, durationForBaseAnimation) {
            weakRefTutorialView?.get()?.startDigging(this.durationForHoleAnimation) {
                description?.let { showBalloonText(it) }
            }
        }
    }

    private fun startAlphaAnimation(view: View, duration: Long, onAnimationEnd: () -> Unit) {
        val showAnimator = ObjectAnimator
                .ofFloat(view, "alpha", 0f, 1f)
                .setDuration(duration)

        showAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEnd.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        showAnimator.start()
    }

    private fun showBalloonText(description: String) {
        val activity = this.weakRefActivity.get()
        if (activity == null) {
            Log.e("TutorialBuilder", "activity is null")
            return
        }
        val baseView = weakRefTutorialView?.get() ?: return

        val descriptionView = TutorialDescriptionView.init(
                activity,
                description,
                baseView.targetRect,
                holeType,
                textColor,
                balloonColor
        )
        baseView.addView(descriptionView)

        startAlphaAnimation(descriptionView, durationForBalloonTextAnimation) {
            weakRefTutorialView?.get()?.let { view ->
                view.onClickOutSide(onClickOutSide)
                onClickTarget?.let {
                    view.onClickTarget {
                        it.invoke()
                        hideTutorial()
                    }
                }
            }
        }
    }

    fun hideTutorial() {
        weakRefActivity.get()?.let { activity ->
            weakRefTutorialView?.get()?.let {
                (activity.window.decorView as ViewGroup).removeViewInLayout(it)
                activity.window.decorView.invalidate()
            }
        }
    }

    companion object {
        private var builder: TutorialBuilder? = null

        @JvmStatic
        fun init(activity: Activity, target: View): TutorialBuilder {
            Handler().post {
                builder?.hideTutorial()
            }

            val newBuilder = TutorialBuilder(activity, target)
            builder = newBuilder

            return newBuilder
        }
    }

}