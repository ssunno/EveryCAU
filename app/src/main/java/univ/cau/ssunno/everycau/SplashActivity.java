package univ.cau.ssunno.everycau;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/*
*  앱 실행시 시작되는 첫 화면
* */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // TODO : 네트워크 동기화 쓰레드 시작
        // TODO : MainTabActivity 호출
        startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
        // TODO : SplashActivity 종료
        finish();
    }
}
