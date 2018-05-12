package jp.studio.edamame.balloontutorial

import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

class ViewOnLayoutListener private constructor(val view: View) {
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
                Log.e("ViewOnLayoutListener", "OnGlobalLayoutListener")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
                } else {
                    view.viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener)
                }

                view.post {
                    Log.e("ViewOnLayoutListener", "view.post")
                    callback.invoke()
                }
            }

            view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }
}