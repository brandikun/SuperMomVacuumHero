package com.brandonhimes.supermomvacuumhero;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private TextView scoreTextView;
    private ConstraintLayout game;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        game = new ConstraintLayout(this);
        Button jumpButton = new Button(this);
        scoreTextView = new TextView(this);
        gameView = new GameView(this);

        game.addView(gameView);
        game.addView(jumpButton);
        game.addView(scoreTextView);
        setContentView(game);

        //make sure views that were added programmatically have Ids
        gameView.setId(View.generateViewId());
        jumpButton.setId(View.generateViewId());
        scoreTextView.setId(View.generateViewId());
        //use constraints to locate elements correctly in UI
        ConstraintSet set = new ConstraintSet();
        set.connect(jumpButton.getId(), ConstraintSet.END, gameView.getId(), ConstraintSet.END);
        set.connect(jumpButton.getId(), ConstraintSet.BOTTOM, gameView.getId(), ConstraintSet.BOTTOM);
        set.connect(scoreTextView.getId(), ConstraintSet.START, gameView.getId(), ConstraintSet.START);
        set.connect(scoreTextView.getId(), ConstraintSet.BOTTOM, gameView.getId(), ConstraintSet.BOTTOM);
        set.applyTo(game);

        ViewGroup.LayoutParams params = jumpButton.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        ((ConstraintLayout.LayoutParams)params).setMargins(0,0,0,50);
        ((ConstraintLayout.LayoutParams)params).setMarginEnd(50);
        jumpButton.setLayoutParams(params);
        jumpButton.setText(R.string.jump);

        ViewGroup.LayoutParams params1 = scoreTextView.getLayoutParams();
        params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params1.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        ((ConstraintLayout.LayoutParams)params1).setMargins(0,0,0,80);
        ((ConstraintLayout.LayoutParams)params1).setMarginStart(50);
        scoreTextView.setLayoutParams(params1);
        scoreTextView.setText(R.string.score);

        jumpButton.setOnClickListener(v -> {
            if(!gameView.getPlayer().getJumpFlag()) {
                gameView.performJump();
            }
        });
    }

    //use activity lifecycle to start and pause game
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!gameView.getPlayer().getJumpFlag()) {
            gameView.performJump();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void updateScore(int score) {
        scoreTextView.setText(String.valueOf("SCORE: " + score));
    }

    public void setGameEnd(String endMessage) {
        TextView gameOverTextView = new TextView(this);
        game.addView(gameOverTextView);
        gameOverTextView.setId(View.generateViewId());
        ConstraintSet set = new ConstraintSet();
        set.clone(game);
        set.connect(gameOverTextView.getId(), ConstraintSet.TOP, gameView.getId(), ConstraintSet.TOP);
        set.connect(gameOverTextView.getId(), ConstraintSet.START, gameView.getId(), ConstraintSet.START);
        set.connect(gameOverTextView.getId(), ConstraintSet.END, gameView.getId(), ConstraintSet.END);
        set.connect(gameOverTextView.getId(), ConstraintSet.BOTTOM, gameView.getId(), ConstraintSet.BOTTOM);
        set.applyTo(game);
        gameOverTextView.setTextSize(50);
        gameOverTextView.setText(endMessage);
        ViewGroup.LayoutParams params = gameOverTextView.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        gameOverTextView.setLayoutParams(params);
        gameOverTextView.setFocusable(true);
        gameOverTextView.setClickable(true);
        gameOverTextView.setOnClickListener(v -> {
            gameView.resume();
            gameOverTextView.setVisibility(View.GONE);
            scoreTextView.setText(String.valueOf("SCORE: " + 0));
        });
    }
}
