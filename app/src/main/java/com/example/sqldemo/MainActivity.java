package com.example.sqldemo;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sqldemo.sql.SQLiteDatabaseDAO;
import com.example.sqldemo.sql.bean.Country;
import com.example.sqldemo.sql.bean.Person;

public class MainActivity extends AppCompatActivity {
    private EditText et_id, et_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_id = findViewById(R.id.tv_id);
        et_name = findViewById(R.id.tv_name);
        checkPermissions();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private void checkPermissions() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length == 2) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }
            }
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                //SQLiteDatabaseDAO.getInstance(this).insertPerson("111", "夏侯渊", "1");
                break;
            case R.id.btn_del:
                //SQLiteDatabaseDAO.getInstance(this).deletePersonById("111");
                SQLiteDatabaseDAO.getInstance(this).deleteCountryById("3");
                break;
            case R.id.btn_update:
                SQLiteDatabaseDAO.getInstance(this).updatePersonById("111");
                break;
            case R.id.btn_search:
//                Country country = SQLiteDatabaseDAO.getInstance(this).selectCountryById("2");
//                if (null != country) {
//                    Log.e(getClass().getName(), country.toString());
//                } else {
//                    Log.e(getClass().getName(), "未查到数据！");
//                }
//                Person person = SQLiteDatabaseDAO.getInstance(this).selectPersonById("1");
//                if (null != person) {
//                    Log.e(getClass().getName(), person.toString());
//                } else {
//                    Log.e(getClass().getName(), "未查到数据！");
//                }
                SQLiteDatabaseDAO.getInstance(this).selectPersonByCountryId("2");
                break;
            case R.id.btn_search_all:
                SQLiteDatabaseDAO.getInstance(this).selectAllCountry();
                SQLiteDatabaseDAO.getInstance(this).selectAllPerson();
                break;
        }
    }
}
