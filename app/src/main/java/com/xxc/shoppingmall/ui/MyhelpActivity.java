package com.xxc.shoppingmall.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class MyhelpActivity extends AbstractPermissionActivity {
    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.helpview)
    View helpview;

    @BindView(R.id.help2)
    TextView help2;
    @BindView(R.id.helpview2)
    View helpview2;

    @BindView(R.id.help3)
    TextView help3;
    @BindView(R.id.helpview3)
    View helpview3;

    @BindView(R.id.helpcontent)
    TextView helpcontent;
    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.helplayout;
    }

    @Override
    public void initData() {
      help.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              init();
              help.setTextColor(0xffff4040);
              helpview.setVisibility(View.VISIBLE);
              helpcontent.setText("1.选择【首页】-> 点击【数字货币】按钮，然后点击右上角提币按钮，输入GSC数量和转出地址，确认无误后点击立即提币即可。");
          }
      });

        help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                help2.setTextColor(0xffff4040);
                helpview2.setVisibility(View.VISIBLE);
                helpcontent.setText("1.升级为初级微股东，赠送价值2000元GSC（以实时价格计算）。\n" +
                        "2.升级为中级微股东，赠送价值10000元GSC（以实时价格计算）。\n" +
                        "3.升级为高级微股东，赠送价值20000元GSC（以实时价格计算）。");
            }
        });

        help3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                help3.setTextColor(0xffff4040);
                helpview3.setVisibility(View.VISIBLE);
                helpcontent.setText("以天为单位，1‰余额定返。\n" +
                        "推荐一名初级微股东余额定返速度增加0.1‰；\n" +
                        "推荐一名中级微股东余额定返速度增加0.5‰；\n" +
                        "推荐一名高级微股东余额定返速度增加1‰；\n");
            }
        });
    }

    @Override
    public void addListeners() {

    }
    private void init(){
        help.setTextColor(0xff333333);
        help2.setTextColor(0xff333333);
        help3.setTextColor(0xff333333);
        helpview.setVisibility(View.GONE);
        helpview2.setVisibility(View.GONE);
        helpview3.setVisibility(View.GONE);

    }
}
