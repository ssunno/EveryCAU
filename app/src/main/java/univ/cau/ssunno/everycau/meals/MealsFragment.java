package univ.cau.ssunno.everycau.meals;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import univ.cau.ssunno.everycau.BaseTabFragment;
import univ.cau.ssunno.everycau.R;

public class MealsFragment extends BaseTabFragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MealsRecycleAdapter mealsRecycleAdapter;
    public MealsFragment() {
        fragmentName = "Meals";
    }

    public static MealsFragment newInstance() {
        return new MealsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mealsRecycleAdapter = new MealsRecycleAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meals, container, false);
        // Fragment 내의 recycler view에 layout manager 와 adapter 연결
        recyclerView = (RecyclerView)rootView.findViewById(R.id.meals_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mealsRecycleAdapter);
        return rootView;
    }
}
