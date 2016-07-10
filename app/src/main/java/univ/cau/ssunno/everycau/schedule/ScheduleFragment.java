package univ.cau.ssunno.everycau.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import univ.cau.ssunno.everycau.BaseTabFragment;
import univ.cau.ssunno.everycau.R;

/*
* 스케줄 관리 프래그먼트
* 주간 시간표 기능
*
* TODO : 내부 뷰 업데이트 메소드 오버라이드
*
* */
public class ScheduleFragment extends BaseTabFragment {

    public ScheduleFragment() {
        this.fragmentName = "Schedule";
    }

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}
