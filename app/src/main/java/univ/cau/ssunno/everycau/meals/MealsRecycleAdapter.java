package univ.cau.ssunno.everycau.meals;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import univ.cau.ssunno.everycau.R;
import univ.cau.ssunno.everycau.utils.database.Constant;
import univ.cau.ssunno.everycau.utils.network.CafeteriaInfo;
import univ.cau.ssunno.everycau.utils.network.CafeteriaManager;

public class MealsRecycleAdapter extends RecyclerView.Adapter<MealsRecycleAdapter.MealsViewHolder> {
    private ArrayList<CafeteriaInfo> cafeteriaInfos;
    private CafeteriaManager cafeteriaManager;
    public MealsRecycleAdapter() {
        this.cafeteriaManager = new CafeteriaManager();
        this.cafeteriaInfos = new ArrayList<>();
        // TODO : getMeals 메서드 파라미터를 변수로 변경
        for ( CafeteriaInfo ci : cafeteriaManager.getMeals(Constant.getCurrentDate(), Constant.getCurrentTime()))
            this.cafeteriaInfos.add(ci);
    }

    @Override
    public int getItemCount() {
        return cafeteriaInfos.size();
    }

    @Override
    public void onBindViewHolder(MealsViewHolder holder, int position) {
        CafeteriaInfo ci = cafeteriaInfos.get(position);
        holder.cafeteriaName.setText(ci.getCafeteriaName());
        holder.dishStyle.setText(ci.getDishStyle());
        holder.calorie.setText(ci.getCalorie());
        holder.price.setText(ci.getPrice());
        while ( !holder.dishContainer.isEmpty() ) holder.dishContainer.remove(0);
        for (String dish : ci.getDishList()) holder.dishContainer.add(dish);
        setListViewHeightBasedOnItems(holder.dishList);
        holder.dishAdapter.notifyDataSetChanged();
    }

    @Override
    public MealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_card, parent, false);
        return new MealsViewHolder(view);
    }

    public static class MealsViewHolder extends RecyclerView.ViewHolder {
        public ImageView cafeteriaLogo;
        public TextView cafeteriaName, dishStyle, calorie, price;
        public ListView dishList;
        public ArrayAdapter<String> dishAdapter;
        public ArrayList<String> dishContainer;
        public MealsViewHolder(View itemView) {
            super(itemView);
            // cafeteriaLogo   = (ImageView)itemView.findViewById(R.id.meals_card_logo);
            cafeteriaName   = (TextView)itemView.findViewById(R.id.meals_card_title);
            dishStyle       = (TextView)itemView.findViewById(R.id.meals_card_dish_style);
            calorie         = (TextView)itemView.findViewById(R.id.meals_card_calorie);
            price           = (TextView)itemView.findViewById(R.id.meals_card_price);
            dishList        = (ListView)itemView.findViewById(R.id.meals_card_dish_list);
            dishContainer   = new ArrayList<>();
            dishAdapter     = new ArrayAdapter<>(itemView.getContext(), R.layout.simple_list_item, dishContainer);
            dishList.setAdapter(dishAdapter);
            dishList.setDivider(null);
        }
    }

    public void setListViewHeightBasedOnItems(ListView listView) {
        // Get list adpter of listview;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)  return;
        int numberOfItems = listAdapter.getCount();
        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            TextView item = (TextView)listAdapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
            if (item.getText().length() > 13)
                totalItemsHeight += (item.getMeasuredHeight() - item.getPaddingTop() - item.getPaddingBottom());
        }
        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() *  (numberOfItems - 1);
        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}