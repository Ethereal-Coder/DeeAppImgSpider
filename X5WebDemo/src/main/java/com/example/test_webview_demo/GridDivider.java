package com.example.test_webview_demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by EtherealPatrick on 2016/10/27.
 * 分割线很多地方都重复,以后组件化再迭代
 */

public class GridDivider extends RecyclerView.ItemDecoration {

  private final int[] ATTRS = new int[] { android.R.attr.listDivider };
  private Drawable mDivider;

  public GridDivider(Context context) {
    final TypedArray a = context.obtainStyledAttributes(ATTRS);
    mDivider = a.getDrawable(0);
    a.recycle();
  }

  private int getSpanCount(RecyclerView parent) {
    int spanCount = -1;
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
    }
    return spanCount;
  }

  private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      if ((pos + 1) % 4 == 0) {
        return true;
      }
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
      if (orientation == StaggeredGridLayoutManager.VERTICAL) {
        if ((pos + 1) % spanCount == 0) {
          return true;
        }
      } else {
        childCount = childCount - childCount % spanCount;
        if (pos >= childCount) {
          return true;
        }
      }
    }
    return false;
  }

  @Override public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
    int spanCount = getSpanCount(parent);
    int childCount = parent.getAdapter().getItemCount();
    if (isLastColum(parent, itemPosition, spanCount, childCount)) {
      outRect.set(mDivider.getIntrinsicWidth() / 2, mDivider.getIntrinsicHeight(),
          mDivider.getIntrinsicWidth(), 0);
    } else {
      outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(),
          mDivider.getIntrinsicWidth() / 2, 0);
    }
  }
}
