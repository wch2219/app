package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.ExtrEntity;
import com.xxc.shoppingmall.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XXC on 2017/11/26.
 * 积分明细界面数据适配器
 */
public class ExtrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String REJECT_FORMAT = "被驳回(%s)";
    private static final String DATA_ERROR_FORMAT = "数据异常(状态码:%d)";

    public static final int TYPE_HEADER = 0X11;
    private List<ExtrEntity> mEntities = new ArrayList<>();
    private Context mContext;


    public ExtrAdapter(Context context) {
        mContext = context;

    }

    @Override
    public int getItemCount() {
        return null != mEntities && mEntities.size() > 0 ? mEntities.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_HEADER) {
//            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout
//                    .item_transation_header, parent, false);
//            return new UserTransationHeaderHolder(item);
//        } else {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_user_transation, parent, false);
        return new UserTransationHolder(item);
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExtrEntity entity = mEntities.get(position);

        ((UserTransationHolder) holder).statusComments.setText(TimeUtils.getFormatTime(entity.getCreateTime()));
        ((UserTransationHolder) holder).tranNum.setText( entity.getAmount()+"");
        ((UserTransationHolder) holder).createTime.setText(entity.getMonyAddren());
        if (1 ==entity.getFlowType()) {
            ((UserTransationHolder) holder).flowType.setText("转入");
        }else{
            ((UserTransationHolder) holder).flowType.setText("转出");
        }
    }

    public class UserTransationHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.flowType)
        TextView flowType;
        @BindView(R.id.createTime)
        TextView createTime;
        @BindView(R.id.tranNum)
        TextView tranNum;
        @BindView(R.id.status_comments)
        TextView statusComments;

        public UserTransationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class UserTransationHeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.transation_month)
        TextView transation_month;

        public UserTransationHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<ExtrEntity> getEntities() {
        return mEntities;
    }

    public void swapData(List<ExtrEntity> entities) {
        if (null != entities) {
            this.mEntities = entities;
            notifyDataSetChanged();
        }
    }
    public void swapMoreData(List<ExtrEntity> entities) {
        if (null != entities && entities.size() > 0) {
            this.mEntities.addAll(entities);
            notifyDataSetChanged();
        }
    }
}
