package com.example.mooreli.game2048;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GameView mGameView;
    private TextView mTvCurrentScore, mTvHighScore;
    private TextView mTvReplay;
    private GameOverLinearLayout mLlGameOver;
    private TextView mGameOverPlayAgain;

    private SharedPreferences mSp;
    private final String spName = "2048Score";
    private final String SCORE = "highScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGameView = (GameView) findViewById(R.id.Main_gameView);
        mTvCurrentScore = (TextView) findViewById(R.id.tvCurrentScore);
        mTvHighScore = (TextView) findViewById(R.id.tvHighScore);
        mTvReplay = (TextView) findViewById(R.id.tvReplay);
        mLlGameOver = (GameOverLinearLayout) findViewById(R.id.llGameOver);
        mGameOverPlayAgain = (TextView) findViewById(R.id.tvGameOverPlayAgain);

        //存储分数
        mSp = getSharedPreferences(spName, MODE_PRIVATE);
        mTvHighScore.setText(getScore()+"");

        mGameView.setOnGameOverListener(new GameView.OnGameOverListener() {
            @Override
            public void gameScore(boolean isGameOver, int score) {
                mTvCurrentScore.setText(score + "");
                judgeScore(score);
                if (isGameOver) {
                    mLlGameOver.setVisibility(View.VISIBLE);
                }
            }
        });
        mTvReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.reset();
                mTvCurrentScore.setText("0");
            }
        });
        mGameOverPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlGameOver.setVisibility(View.GONE);
                mGameView.reset();
                mTvCurrentScore.setText("0");
            }
        });

    }

    private void judgeScore(int currentScore){
        int highScore = getScore();
        if(currentScore <= highScore){
            return;
        }else{
            mTvHighScore.setText(currentScore+"");
            saveScore(currentScore);
        }
    }

    private void saveScore(int value) {
        mSp.edit().putInt(SCORE, value).apply();
    }

    private int getScore() {
        return mSp.getInt(SCORE, 0);
    }
}
