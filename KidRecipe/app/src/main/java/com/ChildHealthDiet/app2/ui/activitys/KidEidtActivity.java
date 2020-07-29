package com.ChildHealthDiet.app2.ui.activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.context.Kidinfo;
import com.ChildHealthDiet.app2.context.UserContext;
import com.ChildHealthDiet.app2.ui.base.BaseActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class KidEidtActivity extends BaseActivity {

    @BindView(R.id.edittext_nickname)
    protected EditText editTextNickname;


    @BindView(R.id.btn_pick_age)
    protected Button btnKidAge;

    @BindView(R.id.mine_edit_toolbar_id)
    Toolbar mToolbar;

    int mYear=2017,mMonth=01,mDay=01;

    @Override
    protected int getContentId() {
        return R.layout.activity_kid_eidt;
    }

    @Override
    protected void initWidget() {

        loadData();

    }

    public static void startActivity(Fragment fragment){
//        Intent intent  =new Intent(context,KidEidtActivity.class);
//        context.startactivityf(intent,10030);

//        activity.startActivityForResult(intent, 10029, options.toBundle());

//        fragment.startActivity(new Intent());
        fragment.startActivityForResult(new Intent(fragment.getContext(), KidEidtActivity.class), 10030);
    }

    private void initEvent(){

    }

    protected void loadData(){
        Kidinfo kidinfo = UserContext.getInstance().getmKidinfo(this);
        if(!kidinfo.getNickName().equals("")){
            this.editTextNickname.setText(kidinfo.getNickName());
        }
        if(!kidinfo.getBirthdate().equals("")){
            this.btnKidAge.setText(kidinfo.getBirthdate());
        }
    }

    @OnClick({R.id.btn_pick_age})
    protected void onClickAgeButton(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(KidEidtActivity.this,AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;

                        final String data =  year +"-" + (month+1<10?"0"+(month+1):month+1) + "-" + (dayOfMonth<10?"0"+dayOfMonth:dayOfMonth) + "";
//                        KidEidtActivity.this.editTextNickname.setText(data);
                        KidEidtActivity.this.btnKidAge.setText(data);
                    }
                },
                mYear, mMonth, mDay);
        if (android.os.Build.VERSION.SDK_INT>=11) {

            DatePicker dp = datePickerDialog.getDatePicker();

            dp.setMaxDate(new Date().getTime());

        }
        datePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.saveData();

    }

    private void saveData() {

        UserContext.getInstance().setmKidinfo(this,this.editTextNickname.getText().toString(),
                this.btnKidAge.getText().toString().equals("请输入出生日期")?"":this.btnKidAge.getText().toString());
    }

    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
