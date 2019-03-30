package spider.task1;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    private long timeleft = 15000;
    private long startTime=0L,timeinmillisec=0L,updatetime=0L;
    private CountDownTimer countDownTimer;
    private boolean counterrunning,Swatchrunning,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv1=(TextView)findViewById(R.id.tv1);
        final TextView tv2=(TextView)findViewById(R.id.tv2);
        LinearLayout layout=(LinearLayout)findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counterrunning)
                {
                    if(Swatchrunning)
                    {
                        if(pass)
                        {
                            tv1.setText("15:000");
                            tv2.setText("00:00:000");
                            timeleft = 15000;
                            startTime=0L;
                            updatetime=0L;
                            timeinmillisec=0L;
                            counterrunning=false;
                            Swatchrunning=false;
                            pass=false;
                        }
                        else
                            StopRunner();

                    }
                    else
                    {
                        countDownTimer.cancel();
                        StartRunner();
                    }
                }
                else
                    StartTimer();
            }
        });
    }

    final Handler handler=new Handler();
    Runnable updateTimeThread=new Runnable() {
        @Override
        public void run() {
            TextView tv2=(TextView)findViewById(R.id.tv2);
            timeinmillisec=SystemClock.uptimeMillis()-startTime;
            int secs=(int)(timeinmillisec/1000);
            int mins=secs/60;
            secs%=60;
            int milliseconds=(int)(timeinmillisec%1000);
            tv2.setText(String.format("%02d:%02d:%03d",mins,secs,milliseconds));
            handler.postDelayed(this,0);
        }
    };




    public void StartTimer(){
        final TextView tv1=(TextView)findViewById(R.id.tv1);
        countDownTimer = new CountDownTimer(timeleft, 1) {
            @Override
            public void onTick(long l) {
                timeleft = l;
                UpdateTime();
            }

            @Override
            public void onFinish() {
                tv1.setText("00:000");
                StartRunner();
            }
        }.start();
        counterrunning=true;
    }

    private void UpdateTime(){
        int seconds=(int)(timeleft/1000);
        long millisec=timeleft-seconds*1000;
        String sec,sec1;
        final MediaPlayer mp = MediaPlayer.create(this,R.raw.beep);
         TextView tv1 = (TextView) findViewById(R.id.tv1);
        if(seconds<10){
            String sec2="0"+seconds;
            if(millisec<100&&millisec>9) {
                sec="0"+millisec;
                tv1.setText(sec2+":"+sec);
            }
            else if(millisec<=9)
            {
                sec1="00"+millisec;
                tv1.setText(sec2+":"+sec1);
            }
            else
                tv1.setText(sec2+":"+millisec);

            if ("03".equals(sec2))
                mp.start();
        }
        else{
            if(millisec<100&&millisec>9) {
                sec="0"+millisec;
                tv1.setText(seconds+":"+sec);
            }
            else if(millisec<=9)
            {
                sec1="00"+millisec;
                tv1.setText(seconds+":"+sec1);
            }
            else
                tv1.setText(seconds+":"+millisec);
        }

    }
    public void StartRunner(){
        startTime=SystemClock.uptimeMillis();
        handler.postDelayed(updateTimeThread,0);
        Swatchrunning=true;
    }

    public void StopRunner(){
        handler.removeCallbacks(updateTimeThread);
        pass=true;
    }

}

