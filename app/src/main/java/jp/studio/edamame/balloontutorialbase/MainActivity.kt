package jp.studio.edamame.balloontutorialbase

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import jp.studio.edamame.balloontutorial.TutorialBuilder
import jp.studio.edamame.balloontutorialbase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.buttonTutorialCircle.setOnClickListener {
            TutorialBuilder.init(this, target = binding.buttonTutorialCircle)
                    .holeType(TutorialBuilder.HoleType.CIRCLE)
                    .radiusOfDp(50f)
                    .durationForBaseAnimation(300)
                    .durationForHoleAnimation(300)
                    .descriptionByString("Circle type")
                    .onClickedOutSide {
                        // nothing to do
                    }
                    .onClickedTarget {
                        Toast.makeText(this, "tapped circle button", Toast.LENGTH_SHORT).show()
                    }
                    .buildAndLayout()
        }

        binding.buttonTutorialSquare.setOnClickListener {
            TutorialBuilder.init(this, binding.buttonTutorialSquare)
                    .holeType(TutorialBuilder.HoleType.SQUARE)
                    .descriptionByString("Square type")
                    .balloonColor(Color.BLACK)
                    .textColor(Color.WHITE)
                    .durationForBaseAnimation(150)
                    .durationForBaseAnimation(300)
                    .buildAndLayout()
        }
    }
}
