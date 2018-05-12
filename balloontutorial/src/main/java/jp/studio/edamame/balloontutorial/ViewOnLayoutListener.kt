package jp.studio.edamame.balloontutorial

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

internal class ViewOnLayoutListener private constructor(val view: View) {
    private lateinit var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    companion object {
        fun target(view: View): ViewOnLayoutListener {
            return ViewOnLayoutListener(view)
        }
    }

    fun listen(callback: () -> Unit) {
        if (view.width != 0 || view.height != 0) {
            callback.invoke()
        } else {
            onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                Log.d("ViewOnLayoutListener", "OnGlobalLayoutListener")

                view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)

                view.post {
                    Log.e("ViewOnLayoutListener", "view.post")
                    callback.invoke()
                }
            }

            view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }
}