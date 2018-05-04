package com.xxc.shoppingmall.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.SignBottomSlBena;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class SignInDateAdapter  <M extends SignBottomSlBena> extends BaseQuickAdapter<M, SignInDateAdapter
        .SignInDateViewHolder> {
    public SignInDateAdapter(@Nullable List<M> data) {
        super(R.layout.item_signidate, data);
    }
    @Override
    protected void convert(SignInDateAdapter.SignInDateViewHolder helper, M item) {
            helper.setText(R.id.tv_money,item.getIntegrationNum()+"");
        if (item.getStatus() == 1) {//已签到
            helper.setText(R.id.tv_type,"已签到");
            helper.setBackgroundRes(R.id.rl_typebg,R.mipmap.icon_signxiaoguo);
            helper.setGone(R.id.ll_sign,true);
        }else{
            helper.setText(R.id.tv_type,"未签到");
            helper.setGone(R.id.ll_sign,false);
            helper.setBackgroundRes(R.id.rl_typebg,R.mipmap.icon_signwei);
        }
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        helper.setText(R.id.tv_date,item.getSignedLastTime().substring(5));
    }

    public static class SignInDateViewHolder extends BaseViewHolder{

        public SignInDateViewHolder (View view){super(view);}
    }
}
