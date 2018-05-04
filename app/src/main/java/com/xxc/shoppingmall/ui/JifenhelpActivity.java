package com.xxc.shoppingmall.ui;

import android.view.View;
import android.widget.TextView;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class JifenhelpActivity extends AbstractPermissionActivity {
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
        helpcontent.setText("1.选择【首页】-> 点击【积分】按钮，然后选择兑换银行卡，输入兑换积分数量。最后，确认无误后点击兑换即可。后台审核通过后即回款到选择的银行账户。\n" +
                "2.积分兑现：1:1，即1积分等于1元人民币。兑现时75%提为现金，10%为税金，15%以10：1的比例转换成泓济商标原始股。如：提现1000积分则记录提现金额为750元，商标原始股增加15股。\n");
      help.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              init();
              help.setTextColor(0xffff4040);
              helpview.setVisibility(View.VISIBLE);
              helpcontent.setText("1.选择【首页】-> 点击【积分】按钮，然后选择兑换银行卡，输入兑换积分数量。最后，确认无误后点击兑换即可。后台审核通过后即回款到选择的银行账户。\n" +
                      "2.积分兑现：1:1，即1积分等于1元人民币。兑现时75%提为现金，10%为税金，15%以10：1的比例转换成泓济商标原始股。如：提现1000积分则记录提现金额为750元，商标原始股增加15股。\n");
          }
      });

        help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                help2.setTextColor(0xffff4040);
                helpview2.setVisibility(View.VISIBLE);
                helpcontent.setText("1.初级微股东享受3%新增人员积分见点奖励。\n" +
                        "2.中级微股东享受7%新增团队见点奖励，享受下面初级微股东团队4%的见点奖励。\n" +
                        "3.高级微股东享受12%新增团队见点奖励，享受下面直接中级微股东团队5%的见点奖励，和直接初级微股东团队9%的见点奖励。\n" +
                        "4.签到可以获得积分。以一个月为一个周期，每月1号至9号，每天签到一次可得0.1积分，10号签到一次可得5积分；每月11号至19号，每天签到一次可得0.1积分，20号签到一次可得10积分；每月21号至29号，每天签到一次可得0.1积分，30号签到一次可得15积分。\n" +
                        "签到没有时间限制，每天的任何时间点都可以签到。\n\n");
            }
        });

        help3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                help3.setTextColor(0xffff4040);
                helpview3.setVisibility(View.VISIBLE);
                helpcontent.setText("积分兑换审核通过后，系统自动扣除。");
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
