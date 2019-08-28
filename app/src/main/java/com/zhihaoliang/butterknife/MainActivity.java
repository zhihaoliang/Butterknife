package com.zhihaoliang.butterknife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhihaoliang.butterknife.base.BaseActivity;
import com.zhihaoliang.butterknife.core.ButterKnife;
import com.zhihaoliang.annoation.BindView;
import com.zhihaoliang.annoation.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.txtMain)
    TextView txtMain;
    @BindView(R.id.butOne)
    Button butOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        txtMain.setText(this.getClass().getName());
        txtTitleAll.setText(this.getClass().getName());
    }


    @OnClick({R.id.butOne, R.id.butTwo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.butOne:
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.butTwo:
                Toast.makeText(this, this.getClass().getName(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
