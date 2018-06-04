package com.example.test_webview_demo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.io.File;
import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

  @BindView(R.id.tv_user_collect) TextView tvUserCollect;
  @BindView(R.id.user_collect_toolbar) Toolbar userCollectToolbar;
  @BindView(R.id.rv_user_collect) RecyclerView rvUserCollect;
  @BindView(R.id.rl_share_container) RelativeLayout rlShareContainer;
  @BindView(R.id.et_share_title) EditText editText;

  private WindowManager mWindowManager;
  private WindowManager.LayoutParams mLayoutParams;
  private BottomViewHolder viewHolder;
  private UserCollectAdapter mAdapter;
  private GridLayoutManager layoutManager;
  private boolean isEditMode = false;
  int count = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    mLayoutParams = new WindowManager.LayoutParams();
    mLayoutParams.format = PixelFormat.TRANSPARENT;
    mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
    mLayoutParams.token = getWindow().getDecorView().getWindowToken();
    mLayoutParams.gravity = Gravity.BOTTOM;
    mLayoutParams.flags |=
        WindowManager.LayoutParams.FLAG_SPLIT_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    mLayoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    mLayoutParams.width = -1;
    mLayoutParams.height = -2;
    setContentView(R.layout.activity_share);
    ButterKnife.bind(this);

    viewHolder = new BottomViewHolder(
        LayoutInflater.from(this).inflate(R.layout.extra_user_collect_bottom_bar, null, false));
    viewHolder.tvCollectBottomTitle.setText("分享图片");

    setBottomEnable(false);
    initToolBar();
    initView();
    initShareFollow();
  }

  private void initShareFollow() {
    viewHolder.tvShare.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Toast.makeText(ShareActivity.this, "share", Toast.LENGTH_SHORT).show();
      }
    });
    viewHolder.tvFollow.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //Toast.makeText(UserCollectActivity.this,"follow",Toast.LENGTH_SHORT).show();
        ArrayList<UserCollectItemEntity> mCheckedData =
            (ArrayList<UserCollectItemEntity>) mAdapter.getAllChecked();
        if (mCheckedData != null && mCheckedData.size() > 0) {
          //ArrayList<Uri> imgUris = new ArrayList<>();
          //for (UserCollectItemEntity entity : mCheckedData) {
          //  imgUris.add(entity.getPath());
          //}
          //originalShareImg(ShareActivity.this,imgUris);
          ArrayList<File> imgUris = new ArrayList<>();
          for (UserCollectItemEntity entity : mCheckedData) {
            File file = new File(entity.getPath().toString());
            imgUris.add(file);
          }
          originalShareImage(ShareActivity.this, imgUris);
        }
      }
    });
  }

  public void originalShareImg(Context context, ArrayList<Uri> imageUris) {
    Intent share_intent = new Intent();
    share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
    share_intent.setType("image/png");//设置分享内容的类型
    share_intent.addFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
    context.startActivity(Intent.createChooser(share_intent, "Share"));
  }

  public void originalShareImage(Context context, ArrayList<File> files) {
    Intent share_intent = new Intent();
    ArrayList<Uri> imageUris = new ArrayList<Uri>();
    Uri uri;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      for (File f : files) {
        Uri imageContentUri = getImageContentUri(context, f);
        imageUris.add(imageContentUri);
      }
    } else {
      for (File f : files) {
        imageUris.add(Uri.fromFile(f));
      }
    }
    share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
    share_intent.setType("image/*");//设置分享内容的类型
    share_intent.addFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    share_intent.putExtra("Kdescription", editText.getText().toString().trim());
    share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
    context.startActivity(Intent.createChooser(share_intent, "Share"));
  }

  public Uri getImageContentUri(Context context, File imageFile) {
    String filePath = imageFile.getAbsolutePath();
    Cursor cursor = context.getContentResolver()
        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
            new String[] { filePath }, null);
    if (cursor != null && cursor.moveToFirst()) {
      int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
      Uri baseUri = Uri.parse("content://media/external/images/media");
      return Uri.withAppendedPath(baseUri, "" + id);
    } else {
      if (imageFile.exists()) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, filePath);
        return context.getContentResolver()
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      } else {
        return null;
      }
    }
  }

  private void initView() {
    layoutManager = new GridLayoutManager(this, 4);
    rvUserCollect.setHasFixedSize(true);
    rvUserCollect.setLayoutManager(layoutManager);
    rvUserCollect.addItemDecoration(new GridDivider(ShareActivity.this));

    mAdapter = new UserCollectAdapter(null, ShareActivity.this);

    mAdapter.registerObserver(new UserCollectAdapter.UserCollectObserver() {
      @Override public void onNoData() {
        //super.onNoData();
        //rvNoCollect.setVisibility(View.VISIBLE);
        //stateLayout.showEmpty();
      }

      @Override public void onChecked(boolean isChecked) {
        //super.onChecked(isChecked);
        count += isChecked ? 1 : -1;
        if (count <= 0) {
          count = 0;
          setBottomEnable(false);
        } else {
          setBottomEnable(true);
        }
      }

      @Override public void onEditChanged(boolean inEdit) {
        //super.onEditChanged(inEdit);
        if (inEdit) {
          //Toast.makeText(UserCollectActivity.this,"edit",Toast.LENGTH_SHORT).show();
          showEditMode();
        } else {
          //Toast.makeText(UserCollectActivity.this,"no edit",Toast.LENGTH_SHORT).show();
          hideEditMode();
        }
      }

      @Override public void onRestore() {
        //super.onRestore();
        count = 0;
        //bottomView.setVisibility(View.INVISIBLE);
        setBottomEnable(false);
      }
    });

    rvUserCollect.setAdapter(mAdapter);
    rvUserCollect.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        hideSoftInput();
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }
    });

    final float density = getResources().getDisplayMetrics().density;
    rlShareContainer.post(new Runnable() {
                            @Override public void run() {
                              //bottomView.setTranslationY(55 * density);
                              //mWindowManager.addView(bottomView, mLayoutParams);
                              viewHolder.getRoot().setTranslationY(55 * density);
                              mWindowManager.addView(viewHolder.getRoot(), mLayoutParams);
                            }
                          }

    );

    refreshData();
  }

  private void hideSoftInput() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(rlShareContainer.getWindowToken(), 0);
    }
  }

  private void refreshData() {
    ArrayList<String> shareImgs = getIntent().getStringArrayListExtra("share_imgs");
    ArrayList<UserCollectItemEntity> entities = new ArrayList<>();
    for (String img : shareImgs) {
      entities.add(new UserCollectItemEntity(img));
    }
    mAdapter.setNewData(entities);

    String share_title = getIntent().getStringExtra("share_title");
    editText.setText(share_title);
    editText.setSelection(editText.getText().length());
  }

  private void showEditMode() {
    viewHolder.getRoot().animate().translationY(0).start();
    //bottomView.animate().translationY(0).start();
  }

  private void hideEditMode() {
    viewHolder.getRoot().animate().translationY(viewHolder.getRoot().getHeight()).start();
    //bottomView.animate().translationY(bottomView.getHeight()).start();
  }

  private void initToolBar() {
    userCollectToolbar.setTitle("");
    setSupportActionBar(userCollectToolbar);
    userCollectToolbar.setNavigationIcon(R.mipmap.activity_back);
    userCollectToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
  }

  private void setBottomEnable(boolean enable) {
    viewHolder.tvFollow.setEnabled(enable);
    viewHolder.tvShare.setEnabled(enable);
    if (enable) {
      viewHolder.rlUserCollectBottpm.setAlpha(0.8f);
    } else {
      viewHolder.rlUserCollectBottpm.setAlpha(0.5f);
    }
  }

  private void changeEditMode() {
    isEditMode = !isEditMode;
    mAdapter.setEditMode(isEditMode);
    if (isEditMode) {
      tvUserCollect.setText("取消");
      //showEditMode();
    } else {
      tvUserCollect.setText("选择图片");
      //hideEditMode();
    }
  }

  @Override protected void onDestroy() {
    mWindowManager.removeViewImmediate(viewHolder.getRoot());
    //mWindowManager.removeViewImmediate(bottomView);
    super.onDestroy();
  }

  @Override public void onBackPressed() {
    if (mAdapter.isEditMode()) {
      mAdapter.setEditMode(false);
    }
    super.onBackPressed();
  }

  @OnClick({ R.id.tv_user_collect, R.id.user_collect_toolbar })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_user_collect:
        changeEditMode();
        break;
      case R.id.user_collect_toolbar:
        break;
    }
  }

  static class BottomViewHolder {
    @BindView(R.id.tv_user_collect_bottom_follow) TextView tvUserCollectBottomFollow;
    @BindView(R.id.tv_collect_bottom_title) TextView tvCollectBottomTitle;
    @BindView(R.id.tv_follow) RelativeLayout tvFollow;
    @BindView(R.id.tv_share) TextView tvShare;
    @BindView(R.id.rl_user_collect_bottpm) RelativeLayout rlUserCollectBottpm;
    private View itemView;

    BottomViewHolder(View view) {
      this.itemView = view;
      ButterKnife.bind(this, view);
    }

    public View getRoot() {
      //this.getRoot();
      return itemView.getRootView();
    }
  }
}
