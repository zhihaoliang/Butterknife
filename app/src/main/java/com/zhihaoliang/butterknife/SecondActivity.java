package com.zhihaoliang.butterknife;

import android.os.Bundle;
import android.widget.TextView;

import com.zhihaoliang.butterknife.base.BaseActivity;
import com.zhihaoliang.butterknife.core.ButterKnife;
import com.zhihaoliang.annoation.BindView;
import com.zhihaoliang.annoation.OnClick;

/**
 * 创建日期：2019-08-28
 * 描述:
 * 作者:支豪亮
 */
public class SecondActivity extends BaseActivity {
    @BindView(R.id.txtSecond)
    TextView txtSecond;
    @BindView(R.id.txtSecond)
    TextView txtSecond1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ButterKnife.bind(this);

        txtSecond.setText(this.getClass().getName());
        txtTitleAll.setText(this.getClass().getName());
        txtSecond1.setText(this.getClass().getName()+"1");
    }


    @OnClick({R.id.butThree,R.id.butThree})
    public void onClick() {
        finish();
    }
}
