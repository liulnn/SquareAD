package xyz.flove.square;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity implements OnClickListener {

    public TextView mPlayer;
    public TextView mStepCount;
    public GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameView = (GameView) this.findViewById(R.id.gameView);
        mPlayer = (TextView) this.findViewById(R.id.gamePlayer);
        mStepCount = (TextView) this.findViewById(R.id.stepCount);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
