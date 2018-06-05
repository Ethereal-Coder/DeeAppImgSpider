package com.example.test_webview_demo.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.test_webview_demo.R;
import java.util.ArrayList;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
class ImgSelectAdapter extends RecyclerView.Adapter<ImgSelectAdapter.ImgHolder> {
  private ArrayList<DeeImgEntity> mData;
  private Context context;
  private int type;

  public ImgSelectAdapter(ArrayList<DeeImgEntity> data,int type) {
    this.mData = (ArrayList)(data == null ? new ArrayList<>(): data);
    this.type = type;
  }

  //public void setNewData(List<DeeImgEntity> data) {
  //  this.mData = (List)(data == null ? new ArrayList<>(): data);
  //  this.notifyDataSetChanged();
  //}

  public ArrayList<DeeImgEntity> getData() {
    return mData;
  }

  @NonNull @Override public ImgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    context = parent.getContext();
    return new ImgHolder(
        LayoutInflater.from(context).inflate(R.layout.rv_item_img_select, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull ImgHolder holder, int position) {
    holder.bind(position);
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public class ImgHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_rv_item_img_select) ImageView ivRvItemImgSelect;
    @BindView(R.id.tv_rv_item_ie) TextView tvRvItemIe;
    @BindView(R.id.fl_rv_item_ie) RelativeLayout flRvItemIe;
    @BindView(R.id.tv_rv_item_img_select_main) TextView tvRvItemImgSelectMain;
    @BindView(R.id.tv_rv_item_img_select_scale) TextView tvRvItemImgSelectScale;
    @BindView(R.id.tv_rv_item_img_select_banner) TextView tvRvItemImgSelectBanner;
    @BindView(R.id.tv_rv_item_img_select_detail) TextView tvRvItemImgSelectDetail;
    public ImgHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final int position) {
      final DeeImgEntity deeImgEntity = mData.get(position);
      Glide.with(context)
          .load(mData.get(position).getImgUrl())
          .into(ivRvItemImgSelect);
      switch (type){
        case ImgType.MAIN:
          if (mData.get(position).isMain()){
            tvRvItemIe.setBackgroundResource(R.mipmap.user_collect_select);
          }
          break;
        case ImgType.SCALE:
          if (mData.get(position).isScale()){
            tvRvItemIe.setBackgroundResource(R.mipmap.user_collect_select);
          }
          break;
        case ImgType.BANNER:
          if (mData.get(position).isBanner()){
            tvRvItemIe.setBackgroundResource(R.mipmap.user_collect_select);
          }
          break;
        case ImgType.DETAIL:
          if (mData.get(position).isDetail()){
            tvRvItemIe.setBackgroundResource(R.mipmap.user_collect_select);
          }
          break;
      }
      if (deeImgEntity.isMain())tvRvItemImgSelectMain.setVisibility(View.VISIBLE);
      if (deeImgEntity.isScale())tvRvItemImgSelectScale.setVisibility(View.VISIBLE);
      if (deeImgEntity.isBanner())tvRvItemImgSelectBanner.setVisibility(View.VISIBLE);
      if (deeImgEntity.isDetail())tvRvItemImgSelectDetail.setVisibility(View.VISIBLE);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          switch (type){
            case ImgType.MAIN:
              mData.get(position).setMain(!mData.get(position).isMain());
              tvRvItemIe.setBackgroundResource(
                  mData.get(position).isMain() ? R.mipmap.user_collect_select
                      : R.mipmap.user_collect_unselect);
              break;
            case ImgType.SCALE:
              mData.get(position).setScale(!mData.get(position).isScale());
              tvRvItemIe.setBackgroundResource(
                  mData.get(position).isScale() ? R.mipmap.user_collect_select
                      : R.mipmap.user_collect_unselect);
              break;
            case ImgType.BANNER:
              mData.get(position).setBanner(!mData.get(position).isBanner());
              tvRvItemIe.setBackgroundResource(
                  mData.get(position).isBanner() ? R.mipmap.user_collect_select
                      : R.mipmap.user_collect_unselect);
              break;
            case ImgType.DETAIL:
              mData.get(position).setDetail(!mData.get(position).isDetail());
              tvRvItemIe.setBackgroundResource(
                  mData.get(position).isDetail() ? R.mipmap.user_collect_select
                      : R.mipmap.user_collect_unselect);
              break;
          }
        }
      });
    }
  }
}
