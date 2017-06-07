package xyz.flove.square

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import com.umeng.analytics.MobclickAgent


class MainActivity : Activity(), OnClickListener {

    var mPlayer: TextView? = null
    var mStepCount: TextView? = null
    var mStepCountLabel: TextView? = null
    var mGameView: GameView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mGameView = this.gameView
        mPlayer = this.gamePlayer
        mStepCount = this.stepCount
        mStepCountLabel = this.stepCountLabel
    }

    override fun onClick(arg0: View) {
        // TODO Auto-generated method stub

    }

    public override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    public override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }
}
