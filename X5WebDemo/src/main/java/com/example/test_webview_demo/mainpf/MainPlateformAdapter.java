package com.example.test_webview_demo.mainpf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.test_webview_demo.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
public class MainPlateformAdapter extends RecyclerView.Adapter<MainPlateformAdapter.PlateHolder> {
  List<MainPfEntity> mData;
  private Context context;

  public MainPlateformAdapter(List<MainPfEntity> data) {
    this.mData = (List)(data == null ? new ArrayList<>(): data);
  }

  public void addData(@NonNull List<MainPfEntity> data){
    mData.addAll(data);
    notifyDataSetChanged();
  }

  public List<MainPfEntity> getmData() {
    return mData;
  }

  @NonNull @Override public PlateHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    context = parent.getContext();
    return new PlateHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_main_plate,parent,false));
  }

  @Override public void onBindViewHolder(@NonNull PlateHolder holder, int position) {
    holder.bind(position);
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public class PlateHolder extends RecyclerView.ViewHolder {
    TextView tv;
    public PlateHolder(View itemView) {
      super(itemView);
      tv= itemView.findViewById(R.id.tv_rv_item_main_plate);
    }

    public void bind(final int position) {
      final MainPfEntity mainPfEntity = mData.get(position);
      tv.setText(mainPfEntity.getName());
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          //context.startActivity(new Intent(context, BrowserActivity.class).putExtra("plate_url",mainPfEntity.getUrl()));
       if (selectListener != null){
         selectListener.onSelect(position);
       }
        }
      });
    }
  }

  private OnSelectListener selectListener;

  public void setSelectListener(OnSelectListener onSelectListener) {
    this.selectListener = onSelectListener;
  }

  public interface OnSelectListener{
    void onSelect(int position);
  }
}
