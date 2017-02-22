package com.example.mooreli.game2048;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GameView mGameView;
    private TextView mTvScore;
    private TextView mTvReplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGameView = (GameView) findViewById(R.id.Main_gameView);
        mTvScore = (TextView) findViewById(R.id.Main_tvScore);
        mTvReplay = (TextView) findViewById(R.id.Main_rePlay);
        mGameView.setOnGameOverListener(new GameView.OnGameOverListener() {
            @Override
            public void gameScore(boolean isGameOver, int score) {
                Log.e("MainActivity", "isOver:" + isGameOver + "   currentScore:" + score);
                mTvScore.setText(score + "");
                if (isGameOver) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("游戏结束\n分数：" + score);
                    builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mGameView.reset();
                            mTvScore.setText("0");
                        }
                    });
                    builder.create().show();
                }
            }
        });
        mTvReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.reset();
                mTvScore.setText("0");
            }
        });
    }
}
