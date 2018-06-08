package com.example.test_webview_demo.detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.test_webview_demo.R;
import java.util.ArrayList;
import java.util.List;

public class SkuEditActivity extends AppCompatActivity {

  @BindView(R.id.rbn_single) RadioButton rbnSingle;
  @BindView(R.id.rbn_one) RadioButton rbnOne;
  @BindView(R.id.rbn_two) RadioButton rbnTwo;
  @BindView(R.id.acp_first) AppCompatSpinner acpFirst;
  @BindView(R.id.rv_acp_first) RecyclerView rvAcpFirst;
  @BindView(R.id.ll_spinner_first) LinearLayout llSpinnerFirst;
  @BindView(R.id.acp_second) AppCompatSpinner acpSecond;
  @BindView(R.id.rv_acp_second) RecyclerView rvAcpSecond;
  @BindView(R.id.ll_spinner_second) LinearLayout llSpinnerSecond;
  @BindView(R.id.tv_cancel) TextView tvCancel;
  @BindView(R.id.tv_commit) TextView tvCommit;
  @BindView(R.id.ll_bottom_operate) LinearLayout llBottomOperate;
  private int checkedId;
  private SkuEditAdapter adapter_one;
  private SkuEditAdapter adapter_two;
  private int addFlag;
  private String lan_second;
  private String lan_first;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sku_edit);
    ButterKnife.bind(this);
    initCheckListener();
    initSpinner();
    initRecycler();
  }

  private void initRecycler() {
    initOne();
    initTwo();
  }

  private void initTwo() {
    GridLayoutManager manager_two = new GridLayoutManager(this, 3);
    List<MutilSku> list = new ArrayList<>();
    list.add(new MutilSku(MutilSku.T_ADD, null));
    adapter_two = new SkuEditAdapter(list);
    adapter_two.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        adapter_two.remove(position);
      }

      @Override public void onAdd() {
        if (lan_second == null)return;
        addFlag = 1;
        showNameDialog();
      }
    });
    rvAcpSecond.setLayoutManager(manager_two);
    rvAcpSecond.setAdapter(adapter_two);
  }

  private void initOne() {
    GridLayoutManager manager_one = new GridLayoutManager(this, 3);
    List<MutilSku> list = new ArrayList<>();
    list.add(new MutilSku(MutilSku.T_ADD, null));
    adapter_one = new SkuEditAdapter(list);
    adapter_one.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        adapter_one.remove(position);
      }

      @Override public void onAdd() {
        if (lan_first == null)return;
        addFlag = 0;
        showNameDialog();
      }
    });
    rvAcpFirst.setLayoutManager(manager_one);
    rvAcpFirst.setAdapter(adapter_one);
  }

  private void initSpinner() {
    acpFirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] languages = getResources().getStringArray(R.array.week);
        lan_first = languages[position];
        Toast.makeText(SkuEditActivity.this, "你点击的是:"+ lan_first, Toast.LENGTH_SHORT).show();
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    acpSecond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] languages = getResources().getStringArray(R.array.week);
        lan_second = languages[position];
        Toast.makeText(SkuEditActivity.this, "你点击的是:"+ lan_second, Toast.LENGTH_SHORT).show();
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void initCheckListener() {
    rbnSingle.setChecked(true);
    checkedId = rbnSingle.getId();
  }

  @OnClick({ R.id.rbn_single, R.id.rbn_one, R.id.rbn_two, R.id.tv_cancel, R.id.tv_commit })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.rbn_single:
      case R.id.rbn_one:
      case R.id.rbn_two:
        onBtnClick(view.getId());
        break;
      case R.id.tv_cancel:
        finish();
        break;
      case R.id.tv_commit:
        getEditSku();
        break;
    }
  }

  private void getEditSku() {
    ArrayList<Sku> skus1 = adapter_one.getData();
    List<Sku> skus2 = adapter_two.getData();
    if (checkedId == R.id.rbn_single){
      finish();
    }else if (checkedId == R.id.rbn_two){
      ArrayList<Sku> skus = new ArrayList<>();
      for (int i = 0; i < skus1.size(); i++) {
        Sku skux1 = skus1.get(i);
        for (int j = 0; j < skus2.size(); j++) {
          Sku skux2 = skus2.get(j);
          skus.add(new Sku(skux1.getName1(),skux2.getName1(),""));
        }
      }
      Intent intent = new Intent();
      intent.putParcelableArrayListExtra("edit_skus",skus);
      setResult(RESULT_OK,intent);
      finish();
    }else if (checkedId == R.id.rbn_one){
      Intent intent = new Intent();
      intent.putParcelableArrayListExtra("edit_skus",skus1);
      setResult(RESULT_OK,intent);
      finish();
    }


  }

  private void onBtnClick(int id) {
    if (checkedId == id) {
      return;
    } else {
      RadioButton radioButton = (RadioButton) findViewById(checkedId);
      radioButton.setChecked(false);
      checkedId = id;
      RadioButton rb = (RadioButton) findViewById(checkedId);
      rb.setChecked(true);
    }

    switch (checkedId){
      case R.id.rbn_single:
        llSpinnerFirst.setVisibility(View.GONE);
        llSpinnerSecond.setVisibility(View.GONE);
        break;
      case R.id.rbn_one:
        llSpinnerFirst.setVisibility(View.VISIBLE);
        llSpinnerSecond.setVisibility(View.GONE);
        break;
      case R.id.rbn_two:
        llSpinnerFirst.setVisibility(View.VISIBLE);
        llSpinnerSecond.setVisibility(View.VISIBLE);
        break;
    }

  }

  private void showNameDialog() {
    final Dialog dialog = new Dialog(SkuEditActivity.this, R.style.MyDialog);
    dialog.setContentView(R.layout.dialog_name_info);
    //dialog.setCanceledOnTouchOutside(true);
    final AppCompatEditText editText = (AppCompatEditText) dialog.findViewById(R.id.et_dialog_name);
    editText.setText("");
    editText.setSelection(editText.getText().length());
    dialog.findViewById(R.id.tv_dialog_name_cancel).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
      }
    });
    dialog.findViewById(R.id.tv_dialog_name_confirm).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!editText.getText().toString().trim().isEmpty()) {
          onNameChanged(editText.getText().toString().trim());
          dialog.dismiss();
        } else {
          Toast.makeText(getApplicationContext(), "请输入正确的用户名", Toast.LENGTH_SHORT).show();
        }
      }
    });

    dialog.show();
  }

  private void onNameChanged(String trim) {
    if (addFlag == 0){
      adapter_one.addData(new MutilSku(MutilSku.T_NORMAL,new Sku(trim)));
    }else {
      adapter_two.addData(new MutilSku(MutilSku.T_NORMAL,new Sku(trim)));
    }
  }
}
