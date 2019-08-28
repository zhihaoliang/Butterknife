package com.zhihaoliang.butterknife.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhihaoliang.butterknife.R;
import com.zhihaoliang.annoation.BindView;

/**
 * 创建日期：2019-08-28
 * 描述:
 * 作者:支豪亮
 */
public class BaseActivity extends AppCompatActivity {
    @BindView(R.id.txtTitleAll)
   public TextView txtTitleAll;

}
