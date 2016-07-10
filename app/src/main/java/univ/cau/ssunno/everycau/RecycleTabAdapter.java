package univ.cau.ssunno.everycau;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecycleTabAdapter extends RecyclerView.Adapter<RecycleTabAdapter.RecycleTabViewHolder> {

    @Override
    public void onBindViewHolder(RecycleTabViewHolder holder, int position) {
        // TODO : 내부 뷰에 실제로 값을 넣어주는 메소드.
    }

    @Override
    public RecycleTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // ViewHolder class
    public static class RecycleTabViewHolder extends RecyclerView.ViewHolder {
        public RecycleTabViewHolder(View itemView) {
            super(itemView);
            // TODO : 카드 내부 뷰 id 가져오기
        }
    }
}
