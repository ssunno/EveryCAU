package univ.cau.ssunno.everycau;

import android.support.v4.app.Fragment;

/*
*  MainTabActivity 에 탑재되는 abstract Fragment.
*  공통 구현 기능은 여기에 구현
*
* */
public abstract class BaseTabFragment extends Fragment {
    protected String fragmentName = "BaseTab";
    // TODO : newInstance 구현 ?
    // TODO : 내부 뷰 업데이트 하는 메소드 구현

    public String getFragmentName(){
        return fragmentName;
    }
}
