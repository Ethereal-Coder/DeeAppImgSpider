package com.example.test_webview_demo.detail;

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
 * Created by 孙应恒 on 2018/6/7.
 * Description:
 */
class SkuEditAdapter extends RecyclerView.Adapter<SkuEditAdapter.SkuHoder> {
  private List<MutilSku> mData;
  private Context context;

  public ArrayList<Sku> getData() {
    ArrayList<Sku> list = new ArrayList<>();
    for (int i = 0; i < mData.size(); i++) {
      if (mData.get(i).getType() == MutilSku.T_NORMAL){
        list.add(mData.get(i).getSku());
      }
    }
    return list;
  }

  public SkuEditAdapter(List<MutilSku> data) {
    this.mData = (List)(data == null ? new ArrayList<>(): data);
  }

  public void addData(MutilSku data) {
    this.mData.add(0,data);
    this.notifyDataSetChanged();
  }

  public void remove(int position) {
    this.mData.remove(position);
    //this.notifyItemRemoved(position);
    this.notifyDataSetChanged();
  }

  @NonNull @Override public SkuHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    context = parent.getContext();
    return new SkuHoder(
        LayoutInflater.from(context).inflate(R.layout.rv_item_sku_edit,parent,false));
  }

  @Override public void onBindViewHolder(@NonNull SkuHoder holder, int position) {
    holder.bind(position);
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public class SkuHoder extends RecyclerView.ViewHolder {
    TextView tv_name;
    TextView tv_delete;
    public SkuHoder(View itemView) {
      super(itemView);
      tv_name = itemView.findViewById(R.id.tv_rv_item_sku_edit);
      tv_delete = itemView.findViewById(R.id.tv_rv_item_sku_edit_delete);
    }

    public void bind(final int position) {
      MutilSku mutilSku = mData.get(position);
      if (mutilSku.getType() == MutilSku.T_ADD){
        tv_delete.setVisibility(View.GONE);
        tv_name.setText("添加");
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (onEditClickListener != null){
              onEditClickListener.onAdd();
            }
          }
        });
      }else {
        tv_delete.setVisibility(View.VISIBLE);
        tv_name.setText(mutilSku.getSku().getName1());
        tv_delete.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (onEditClickListener != null){
              onEditClickListener.onRemove(position);
            }
          }
        });
      }
    }
  }

  private DetailEditAdapter.OnEditClickListener onEditClickListener;

  public void setOnEditClickListener(DetailEditAdapter.OnEditClickListener onEditClickListener) {
    this.onEditClickListener = onEditClickListener;
  }

  public interface OnEditClickListener{
    void onRemove(int position);
    void onAdd();
  }
}
