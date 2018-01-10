package com.example.admin.ai_speech;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.john.waveview.WaveView;

public class StartActivity extends Activity {
    String array[] = {"boo","boy","agnes","tangled","boy1"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.circle_menu);
        //WaveView waveView = (WaveView)findViewById(R.id.wave);
        //waveView.setProgress(50);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        final ImageView imgViewPointer = (ImageView) findViewById(R.id.imgViewPointer);
        imgViewPointer.startAnimation(blink);

        circleMenu.startAnimation(fade_in);
        circleMenu.setMainMenu(Color.parseColor("#D75C34"),R.drawable.ic_human_greeting,R.drawable.ic_close)
                .addSubMenu(Color.parseColor("#FFFAFA"),R.drawable.boo1)
                .addSubMenu(Color.parseColor("#FFC0CB"),R.drawable.boy11)
                .addSubMenu(Color.parseColor("#00BFFF"),R.drawable.agnes1)
                .addSubMenu(Color.parseColor("#FF7F50"),R.drawable.tangled1)
                .addSubMenu(Color.parseColor("#FFFF66"),R.drawable.boy1)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(final int i) {
                        //Toast.makeText(getApplicationContext(),array[i],Toast.LENGTH_LONG).show();
                        imgViewPointer.clearAnimation();
                        imgViewPointer.setVisibility(View.INVISIBLE);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // Actions to do after 10 seconds
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("name",array[i]);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
                            }
                        }, 1000);

                    }
                });
    }


}
