package univ.cau.ssunno.everycau;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import univ.cau.ssunno.everycau.utils.database.DatabaseHelper;

/*
*  앱 실행시 시작되는 첫 화면
* */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(1000); } catch (Exception e) {e.printStackTrace();}
                // initialize Database
                DatabaseHelper.getInstance(getApplicationContext());
                // start activity
                startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
                finish();
            }
        }).start();
    }
}
