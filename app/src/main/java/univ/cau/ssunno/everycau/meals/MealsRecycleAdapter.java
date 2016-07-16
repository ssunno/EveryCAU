package univ.cau.ssunno.everycau.meals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import univ.cau.ssunno.everycau.R;
import univ.cau.ssunno.everycau.utils.network.CafeteriaInfo;
import univ.cau.ssunno.everycau.utils.network.CafeteriaManager;

public class MealsRecycleAdapter extends RecyclerView.Adapter<MealsRecycleAdapter.MealsViewHolder> {
    private ArrayList<CafeteriaInfo> cafeteriaInfos;
    private CafeteriaManager cafeteriaManager;
    public MealsRecycleAdapter() {
        this.cafeteriaManager = new CafeteriaManager();
        this.cafeteriaInfos = new ArrayList<>();
        // TODO : getMeals 메서드 파라미터를 변수로 변경
        for ( CafeteriaInfo ci : cafeteriaManager.getMeals("20160718"))
            this.cafeteriaInfos.add(ci);
    }

    @Override
    public int getItemCount() {
        return cafeteriaInfos.size();
    }

    @Override
    public void onBindViewHolder(MealsViewHolder holder, int position) {
        holder.name.setText(this.cafeteriaInfos.get(position).getName());
        holder.time.setText(this.cafeteriaInfos.get(position).getServiceTime());
        holder.dishAdapter.setMenuInfos(this.cafeteriaInfos.get(position).getMenus());
        holder.dishAdapter.notifyDataSetChanged();
    }

    @Override
    public MealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_card, parent, false);
        return new MealsViewHolder(view);
    }

    public static class MealsViewHolder extends RecyclerView.ViewHolder {
        public ImageView logo;
        public TextView name, time;
        public RecyclerView restDishRecycler;
        public StaggeredGridLayoutManager layoutManager;
        public DishAdapter dishAdapter;
        public MealsViewHolder(View itemView) {
            super(itemView);
            logo = (ImageView)itemView.findViewById(R.id.meals_card_logo);
            name = (TextView)itemView.findViewById(R.id.meals_card_title);
            time = (TextView)itemView.findViewById(R.id.meals_card_time);
            restDishRecycler = (RecyclerView)itemView.findViewById(R.id.meals_card_meallist);
            layoutManager = new StaggeredGridLayoutManager(2, 1);
            restDishRecycler.setLayoutManager(layoutManager);
            dishAdapter = new DishAdapter();
            restDishRecycler.setAdapter(dishAdapter);
        }
    }
}