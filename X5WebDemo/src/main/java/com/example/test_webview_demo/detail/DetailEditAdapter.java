package com.example.test_webview_demo.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.test_webview_demo.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
class DetailEditAdapter extends RecyclerView.Adapter<DetailEditAdapter.ImgHolder> {
  private List<MutilType> mData;
  private Context context;

  public List<MutilType> getmData() {
    return mData;
  }

  public DetailEditAdapter(List<MutilType> data) {
    this.mData = (List)(data == null ? new ArrayList<>(): data);
  }

  public void setNewData(List<MutilType> data) {
    this.mData = (List)(data == null ? new ArrayList<>(): data);
    this.notifyDataSetChanged();
  }

  public void addData(List<MutilType> data) {
    this.mData.addAll(data);
    //this.notifyItemRangeInserted(this.mData.size() - data.size(), data.size());
    this.notifyDataSetChanged();
  }

  public void addData(MutilType data) {
    this.mData.add(data);
    this.notifyDataSetChanged();
  }

  public void remove(int position) {
    this.mData.remove(position);
    //this.notifyItemRemoved(position);
    this.notifyDataSetChanged();
  }

  @NonNull @Override public ImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    context = parent.getContext();
    return new ImgHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_detail_edit,parent,false));
  }

  @Override public void onBindViewHolder(@NonNull ImgHolder holder, int position) {
    holder.bind(position);
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public class ImgHolder extends RecyclerView.ViewHolder {
    ImageView iv;
    TextView tv;
    public ImgHolder(View itemView) {
      super(itemView);
      iv = itemView.findViewById(R.id.iv_rv_item_detail_edit);
      tv = itemView.findViewById(R.id.tv_rv_item_detail_edit_delete);
    }

    public void bind(final int position) {
      MutilType mutilType = mData.get(position);
      if (mutilType.getType() == MutilType.T_ADD){
        tv.setVisibility(View.GONE);
        Glide.with(context)
            .load(R.drawable.addimg_1x)
            .into(iv);
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            //List<MutilType> list = new ArrayList<>();
            //list.add(new MutilType(MutilType.T_NORMAL,new DeeImgEntity("https://cbu01.alicdn.com/img/ibank/2018/076/681/8714186670_1472189757.200x200xz.jpg")));
            //list.add(new MutilType(MutilType.T_NORMAL,new DeeImgEntity("https://cbu01.alicdn.com/img/ibank/2018/076/681/8714186670_1472189757.200x200xz.jpg")));
            //list.add(new MutilType(MutilType.T_NORMAL,new DeeImgEntity("https://cbu01.alicdn.com/img/ibank/2018/076/681/8714186670_1472189757.200x200xz.jpg")));
            //addData(list);
            if (onEditClickListener != null){
              onEditClickListener.onAdd();
            }
          }
        });
      }else {
        tv.setVisibility(View.GONE);
        Glide.with(context)
            .load(mutilType.getDeeImgEntity().getImgUrl())
            .into(iv);
        tv.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (onEditClickListener != null){
              onEditClickListener.onRemove(position);
            }
          }
        });
      }
    }
  }

  private OnEditClickListener onEditClickListener;

  public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
    this.onEditClickListener = onEditClickListener;
  }

  public interface OnEditClickListener{
    void onRemove(int position);
    void onAdd();
  }
}
