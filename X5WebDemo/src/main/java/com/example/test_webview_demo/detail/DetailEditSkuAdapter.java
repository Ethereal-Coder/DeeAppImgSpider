package com.example.test_webview_demo.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.test_webview_demo.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙应恒 on 2018/6/7.
 * Description:
 */
class DetailEditSkuAdapter extends RecyclerView.Adapter<DetailEditSkuAdapter.SkuHolder> {
  private List<Sku> mData;
  private Context context;

  public DetailEditSkuAdapter(List<Sku> data) {
    this.mData = (List) (data == null ? new ArrayList<>() : data);
  }

  public void setNewData(List<Sku> data) {
    this.mData = (List) (data == null ? new ArrayList<>() : data);
    this.notifyDataSetChanged();
  }

  private void removeImg(int position) {
    mData.get(position).setUrl("");
    this.notifyDataSetChanged();
  }

  @NonNull @Override public SkuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    context = parent.getContext();
    return new SkuHolder(
        LayoutInflater.from(context).inflate(R.layout.rv_item_detail_edit_sku_list, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull SkuHolder holder, int position) {
    holder.bind(position);
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public void changeData(int tempSkuPosition, String img_select_sku) {
    mData.get(tempSkuPosition).setUrl(img_select_sku);
    this.notifyDataSetChanged();
  }

  public class SkuHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_one) TextView tvOne;
    @BindView(R.id.tv_two) TextView tvTwo;
    @BindView(R.id.iv_rv_item_detail_edit) ImageView imageView;
    @BindView(R.id.tv_rv_item_detail_edit_delete) TextView tv_delete;

    public SkuHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final int position) {
      Sku sku = mData.get(position);
      tvOne.setText(sku.getName1() == null ? "" : sku.getName1());
      tvTwo.setText(sku.getName2() == null ? "" : sku.getName2());
      if (sku.getUrl() == null || sku.getUrl().equals("")) {
        Glide.with(context).load(R.drawable.addimg_1x).into(imageView);
        tv_delete.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (onEditClickListener != null) {
              onEditClickListener.onAdd(position);
            }
          }
        });
      } else {
        Glide.with(context).load(sku.getUrl()).into(imageView);
        tv_delete.setVisibility(View.VISIBLE);
        tv_delete.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            removeImg(position);
          }
        });
      }
    }
  }

  private OnEditClickListener onEditClickListener;

  public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
    this.onEditClickListener = onEditClickListener;
  }

  public interface OnEditClickListener {
    void onAdd(int position);
  }
}
