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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.test_webview_demo.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 孙应恒 on 2018/6/6.
 * Description:
 */
class ScanDetailAdapter extends RecyclerView.Adapter<ScanDetailAdapter.BannerHolder> {
  List<DeeImgEntity> mData;
  private Context context;

  public ScanDetailAdapter(List<DeeImgEntity> data) {
    this.mData = (List) (data == null ? new ArrayList<>() : data);
  }

  public void remove(int position) {
    this.mData.remove(position);
    this.notifyItemRemoved(position);
    this.notifyItemRangeChanged(position, mData.size());
    //this.notifyDataSetChanged();
  }

  public void switchItem(int oldIndex,int newindex){
    Collections.swap(mData,oldIndex,newindex);
    this.notifyItemMoved(oldIndex,newindex);
    this.notifyItemChanged(oldIndex);
    this.notifyItemChanged(newindex);

  }

  public List<DeeImgEntity> getmData() {
    return mData;
  }

  @NonNull @Override
  public ScanDetailAdapter.BannerHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    context = parent.getContext();
    return new BannerHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_scan_detail,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull ScanDetailAdapter.BannerHolder holder, int position) {
    holder.bind(position);
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public class BannerHolder extends RecyclerView.ViewHolder {
    ImageView bannerView;
    TextView tv;
    TextView up;
    TextView down;
    public BannerHolder(View itemView) {
      super(itemView);
      //bannerView = (AutoScaleHeightImageView) itemView;
      //      //ViewGroup.LayoutParams params =
      //      //    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
      //      //        ViewGroup.LayoutParams.MATCH_PARENT);
      //      //bannerView.setLayoutParams(params);
      //      //bannerView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      bannerView = itemView.findViewById(R.id.iv_Rv_item_scan_detail);
      tv = itemView.findViewById(R.id.tv_rv_item_img_scan_delete);
      down = itemView.findViewById(R.id.tv_rv_item_move_down);
      up = itemView.findViewById(R.id.tv_rv_item_move_up);
    }

    public void bind(final int position) {
      if (mData == null || mData.isEmpty()) {
        return;
      }

      if (position == 0){
        up.setVisibility(View.GONE);
      }else {
        up.setVisibility(View.VISIBLE);
      }

      if (position == mData.size() - 1){
        down.setVisibility(View.GONE);
      }else {
        down.setVisibility(View.VISIBLE);
      }

      String res = mData.get(position).getImgUrl();
      Glide.with(context)
          .load(res)
          .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
          .into(bannerView);
      tv.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          remove(position);
        }
      });

      up.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          switchItem(position,position-1);
        }
      });

      down.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          switchItem(position,position+1);
        }
      });

    }
  }
}
