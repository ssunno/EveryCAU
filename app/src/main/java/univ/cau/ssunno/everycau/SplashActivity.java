package univ.cau.ssunno.everycau;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import univ.cau.ssunno.everycau.utils.database.DatabaseHelper;
import univ.cau.ssunno.everycau.utils.network.CafeteriaManager;

/*
*  앱 실행시 시작되는 첫 화면
* */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DatabaseHelper.getInstance(getApplicationContext());
        CafeteriaManager cafeteriaManager = new CafeteriaManager();
        // TODO : 어플 시작할 때 마다 식단표 요청 -> 하루에 한 번만 요청하는 것으로 변경해야 함
        // DatabaseHelper 에서 새 테이블 생성 -> 오늘 날짜 저장하고 업데이트 하면 업데이트한 날짜로 변경하면 대충 되겠지?
        // DB는 DatabaseHelper 클래스에서 사용법 보고 따라하면 대충 됩니다
        cafeteriaManager.updateCafeteria();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(1000); } catch (Exception e) {e.printStackTrace();}
                // start activity
                startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
                finish();
            }
        }).start();
    }
}
