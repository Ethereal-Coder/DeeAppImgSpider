package com.example.test_webview_demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test_webview_demo.net.VisitorNets;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;

/**
 * Created by EtherealPatrick on 2017/1/19.
 */

public class UserCollectAdapter
    extends RecyclerView.Adapter<UserCollectAdapter.UserCollectViewHolder> {

  private Context context;
  private List<UserCollectItemEntity> mData;
  private ShareActivity shareActivity;
  private List<UserCollectItemEntity> mCheckedData = new ArrayList<>();
  private boolean mEditMode;
  private String url;

  public UserCollectAdapter(List<String> data, ShareActivity activity) {
    this.mData = (List) (data == null ? new ArrayList<>() : data);
    this.shareActivity = activity;
  }

  //重写onCreateViewHolder()方法,返回一个自定义的ViewHolder
  @Override public UserCollectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    context = parent.getContext();
    View view = LayoutInflater.from(context).inflate(R.layout.rv_user_collect_item, parent, false);
    UserCollectViewHolder holder = new UserCollectViewHolder(view);
    return holder;
  }

  //填充onCreateViewHolder()方法返回的holder控件
  @Override public void onBindViewHolder(final UserCollectViewHolder holder, final int position) {
    UserCollectItemEntity entity = mData.get(position);
    String xxx = entity.getImgUrl();
    final String yyy = xxx.substring(xxx.lastIndexOf("/")+1);
    Glide.with(context)
        .load(entity.getImgUrl())
        .apply(new RequestOptions().centerCrop())
        .into(holder.ivRvUserCollectItem);
    //io.reactivex.Observable.create(new ObservableOnSubscribe<byte[]>() {
    //  @Override public void subscribe(final ObservableEmitter<byte[]> emitter) throws Exception {
    //    Glide.with(context)
    //        .as(byte[].class)
    //        .load(url)
    //        .into(new SimpleTarget<byte[]>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
    //          @Override public void onResourceReady(final byte[] resource,
    //              Transition<? super byte[]> transition) {
    //            if (resource.length > 0) {
    //              emitter.onNext(resource);
    //              emitter.onComplete();
    //            } else {
    //              emitter.onError(new Throwable());
    //            }
    //          }
    //        });
    //  }
    //})
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(Schedulers.io())
    //    .flatMap(new Function<byte[], ObservableSource<String>>() {
    //      @Override public ObservableSource<String> apply(byte[] bytes) throws Exception {
    //        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    //          String filePath = null;
    //          String fileAb = null;
    //          FileOutputStream fos = null;
    //          try {
    //            filePath =
    //                Environment.getExternalStorageDirectory().getCanonicalPath() + "/DeeSpiderImg";
    //            File imgDir = new File(filePath);
    //            if (!imgDir.exists()) {
    //              imgDir.mkdirs();
    //            }
    //            fileAb = filePath + "/" + yyy;
    //            fos = new FileOutputStream(fileAb);
    //            fos.write(bytes);
    //            final String finalFileAb = fileAb;
    //            return io.reactivex.Observable.create(new ObservableOnSubscribe<String>() {
    //              @Override public void subscribe(ObservableEmitter<String> emitter)
    //                  throws Exception {
    //                emitter.onNext(finalFileAb);
    //                emitter.onComplete();
    //              }
    //            });
    //            //Toast.makeText(context, "图片已保存到" + filePath, Toast.LENGTH_SHORT).show();
    //          } catch (final IOException e) {
    //
    //            e.printStackTrace();
    //            return io.reactivex.Observable.create(new ObservableOnSubscribe<String>() {
    //              @Override public void subscribe(ObservableEmitter<String> emitter)
    //                  throws Exception {
    //                emitter.onError(e);
    //              }
    //            });
    //          } finally {
    //            try {
    //              if (fos != null) {
    //                fos.close();
    //              }
    //            } catch (IOException e) {
    //              e.printStackTrace();
    //            }
    //          }
    //        } else {
    //          Toast.makeText(context, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
    //          return io.reactivex.Observable.create(new ObservableOnSubscribe<String>() {
    //            @Override public void subscribe(ObservableEmitter<String> emitter)
    //                throws Exception {
    //              emitter.onError(new Throwable());
    //            }
    //          });
    //        }
    //      }
    //    })
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Observer<String>() {
    //      @Override public void onSubscribe(Disposable d) {
    //
    //      }
    //
    //      @Override public void onNext(String s) {
    //
    //      }
    //
    //      @Override public void onError(Throwable e) {
    //
    //      }
    //
    //      @Override public void onComplete() {
    //
    //      }
    //    });
    io.reactivex.Observable.just(entity.getImgUrl())
        .subscribeOn(Schedulers.io())
        .flatMap(new Function<String, ObservableSource<ResponseBody>>() {
          @Override public ObservableSource<ResponseBody> apply(String s) throws Exception {
            return VisitorNets.getInstance().downloadImg(s);
          }
        })
        .map(new Function<ResponseBody, String>() {
          @Override public String apply(ResponseBody responseBody) throws Exception {
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "DeeSpiderImg";
            String imgname = yyy;
            if (ImgSaveUtil.saveImageToGallery(context, responseBody, storePath,
                imgname)) {
              return storePath + "/" + imgname;
            }
            return "";
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(String s) {
            if (s.length()>0){
              holder.tvUserCollectItemPrice.setText(s);
              long length = new File(s).length();
              //if (length <10000){
              //  removeItemSmall(position);
              //}
              holder.tvSize.setText(length/1024+"KB");

            }else {
              holder.tvUserCollectItemPrice.setText("error");
              holder.tvSize.setText("0kb");
            }
          }

          @Override public void onError(Throwable e) {
            holder.tvUserCollectItemPrice.setText("error");
            holder.tvSize.setText("0kb");
          }

          @Override public void onComplete() {

          }
        });
    //holder.tvUserCollectItemPrice.setText(yyy);
    holder.bind(mData.get(position), mEditMode);

    if (mOnItemClickListener != null) {
      holder.llRvUserCollectItem.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mOnItemClickListener.onClick(holder, position);
        }
      });
    }
  }

  private void removeItemSmall(int position) {
    mData.remove(position);
    if (mData.size() < 1) {
      mObservable.notifyNoData();
    }
    notifyItemRemoved(position);
    mObservable.notifyItemRestore();
  }

  public void savaBitmap(String imgName, byte[] bytes) {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      String filePath = null;
      FileOutputStream fos = null;
      try {
        filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/DeeSpiderImg";
        File imgDir = new File(filePath);
        if (!imgDir.exists()) {
          imgDir.mkdirs();
        }
        imgName = filePath + "/" + imgName;
        fos = new FileOutputStream(imgName);
        fos.write(bytes);
        //Toast.makeText(context, "图片已保存到" + filePath, Toast.LENGTH_SHORT).show();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (fos != null) {
            fos.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      Toast.makeText(context, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public int getItemCount() {
    return mData == null ? 0 : mData.size();
  }

  /**
   * 设置是否在可编辑状态下
   */
  public void setEditMode(boolean editMode) {
    mEditMode = editMode;
    if (!editMode) {
      if (mCheckedData.size() > 0) {
        for (UserCollectItemEntity data : mCheckedData) {
          data.setChecked(false);
        }
        mCheckedData.clear();
      }
      mObservable.notifyItemRestore();
    }
    notifyDataSetChanged();

    //edit模式在外编辑,作为以后需要添加长按或拖拽edit模式扩展
    mObservable.notifyItemEditModeChanged(editMode);
  }

  public boolean isEditMode() {
    return mEditMode;
  }

  class UserCollectViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_rv_user_collect_item) ImageView ivRvUserCollectItem;
    @BindView(R.id.tv_rv_user_collect_item) TextView tvRvUserCollectItem;
    @BindView(R.id.tv_user_collect_item_price) TextView tvUserCollectItemPrice;
    @BindView(R.id.ll_rv_user_collect_item) RelativeLayout llRvUserCollectItem;
    @BindView(R.id.tv_user_collect_item_size)TextView tvSize;
    public UserCollectViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bind(UserCollectItemEntity itemEntity, boolean inEditMode) {
      if (inEditMode) {
        Drawable drawable = ContextCompat.getDrawable(itemView.getContext(),
            itemEntity.isChecked() ? R.mipmap.user_collect_select : R.mipmap.user_collect_unselect);
        tvRvUserCollectItem.setVisibility(View.VISIBLE);
        tvRvUserCollectItem.setBackgroundDrawable(drawable);
      } else {
        tvRvUserCollectItem.setVisibility(View.GONE);
      }
    }
  }

  public List<UserCollectItemEntity> getAllChecked() {
    return mCheckedData;
  }

  public void setNewData(List<UserCollectItemEntity> data) {
    this.mData = (List) (data == null ? new ArrayList() : data);
    if (mData.size() < 1) {
      mObservable.notifyNoData();
    }
    this.notifyDataSetChanged();
  }

  public void addData(List<UserCollectItemEntity> data) {
    this.mData.addAll(data);
    this.notifyItemRangeInserted(this.mData.size() - data.size(), data.size());
    this.notifyDataSetChanged();
  }

  public void clearData() {
    this.mData.clear();
    this.notifyDataSetChanged();
  }

  /**
   * 点击监听
   */
  public interface OnItemClickListener {
    void onClick(UserCollectViewHolder holder, int position);

    void onLongClick(UserCollectViewHolder holder, int position);

    void onChildClick(UserCollectViewHolder holder, int position);
  }

  private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
    @Override public void onClick(final UserCollectViewHolder viewHolder, int position) {
      if (mEditMode) {
        final UserCollectItemEntity mockEntity = mData.get(position);
        if (mockEntity.isChecked()) {
          mockEntity.setChecked(false);
          mCheckedData.remove(mockEntity);
        } else {
          //if (mCheckedData.size() > 2) {
          //  Toast.makeText(context, "最多选择三个商品", Toast.LENGTH_SHORT).show();
          //  return;
          //}
          if (mCheckedData.size()>8){
            Toast.makeText(context,"最多选择9张图片",Toast.LENGTH_SHORT).show();
            return;
          }
          mockEntity.setChecked(true);
          mockEntity.setPath(Uri.parse(viewHolder.tvUserCollectItemPrice.getText().toString()));
          mCheckedData.add(mockEntity);
        }
        //mockEntity.setChecked(!mockEntity.isChecked());
        //mCheckedData.add(mockEntity);
        mObservable.notifyItemCheckChanged(mockEntity.isChecked());

        final UserCollectViewHolder binding = viewHolder;
        //if (position != -1) {
        //  notifyItemChanged(position);
        //}
        binding.tvRvUserCollectItem.setScaleX(0f);
        binding.tvRvUserCollectItem.setScaleY(0f);
        binding.tvRvUserCollectItem.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setListener(new AnimatorListenerAdapter() {
              @Override public void onAnimationCancel(Animator animation) {
                binding.tvRvUserCollectItem.setScaleX(1f);
                binding.tvRvUserCollectItem.setScaleY(1f);
                binding.tvRvUserCollectItem.animate().setListener(null);
              }

              @Override public void onAnimationEnd(Animator animation) {
                binding.tvRvUserCollectItem.setScaleX(1f);
                binding.tvRvUserCollectItem.setScaleY(1f);
                binding.tvRvUserCollectItem.animate().setListener(null);
              }

              @Override public void onAnimationStart(Animator animation) {
                binding.tvRvUserCollectItem.setBackgroundResource(
                    mockEntity.isChecked() ? R.mipmap.user_collect_select
                        : R.mipmap.user_collect_unselect);
              }
            });
      } else {
        UserCollectItemEntity mockEntity = mData.get(position);
        mockEntity.setPath(Uri.parse(viewHolder.tvUserCollectItemPrice.getText().toString()));
        Intent intent = new Intent(context,ScanImgActivity.class);
        intent.putExtra("scan_img",mockEntity.getPath().toString());
        intent.putExtra("scan_img_position",position);
        ViewCompat.setTransitionName(viewHolder.ivRvUserCollectItem, "smart_ware_img"+position);
        ActivityOptionsCompat options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(shareActivity,
                new Pair<View, String>(viewHolder.ivRvUserCollectItem, "smart_ware_img"+position));
        ActivityCompat.startActivity(context, intent, options.toBundle());
      }
    }

    @Override public void onLongClick(UserCollectViewHolder viewHolder, int position) {

    }

    @Override public void onChildClick(UserCollectViewHolder viewHolder, int position) {

    }
  };

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.mOnItemClickListener = onItemClickListener;
  }

  /**
   * check/edit 监听
   */
  private UserCollectObservable mObservable = new UserCollectObservable();

  public void registerObserver(UserCollectObserver observer) {
    mObservable.registerObserver(observer);
  }

  public static abstract class UserCollectObserver {
    public void onChecked(boolean isChecked) {

    }

    public void onEditChanged(boolean inEdit) {

    }

    public void onRestore() {

    }

    public void onNoData() {

    }
  }

  static class UserCollectObservable extends Observable<UserCollectObserver> {

    public boolean isRegister(UserCollectObserver observer) {
      return mObservers.contains(observer);
    }

    public void notifyItemCheckChanged(boolean isChecked) {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        mObservers.get(i).onChecked(isChecked);
      }
    }

    public void notifyItemEditModeChanged(boolean editMode) {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        mObservers.get(i).onEditChanged(editMode);
      }
    }

    public void notifyItemRestore() {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        mObservers.get(i).onRestore();
      }
    }

    public void notifyNoData() {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        mObservers.get(i).onNoData();
      }
    }
  }
}
