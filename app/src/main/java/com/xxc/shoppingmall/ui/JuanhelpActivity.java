package com.xxc.shoppingmall.ui;

import android.view.View;
import android.widget.TextView;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class JuanhelpActivity extends AbstractPermissionActivity {
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
        help3.setText("扣减规则");
        helpcontent.setText("可以转给用于兑换河南泓济智能科技股份有限公司在香港国际知识产权交易中心上市的商标原始股。");
      help.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              init();
              help.setTextColor(0xffff4040);
              helpview.setVisibility(View.VISIBLE);
              helpcontent.setText("可以转给用于兑换河南泓济智能科技股份有限公司在香港国际知识产权交易中心上市的商标原始股。");
          }
      });

        help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                help2.setTextColor(0xffff4040);
                helpview2.setVisibility(View.VISIBLE);
                helpcontent.setText("积分提现时，15%的积分按照10：1的比例转换成商标原始股。比如：提现1000积分，自动转换成15股商标原始股。");
            }
        });

        help3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                help3.setTextColor(0xffff4040);
                helpview3.setVisibility(View.VISIBLE);
                helpcontent.setText("");
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
