package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.UserTransationEntity;
import com.xxc.shoppingmall.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XXC on 2017/11/26.
 * 积分明细界面数据适配器
 */
public class UserTransationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String REJECT_FORMAT = "被驳回(%s)";
    private static final String DATA_ERROR_FORMAT = "数据异常(状态码:%d)";

    public static final int TYPE_HEADER = 0X11;
    private List<UserTransationEntity> mEntities = new ArrayList<>();
    private Context mContext;
    private int flowType;

    public UserTransationAdapter(Context context, int flowType) {
        mContext = context;
        this.flowType = flowType;
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
        UserTransationEntity entity = mEntities.get(position);
        ((UserTransationHolder) holder).createTime.setText(TimeUtils.getFormatDay(Long.parseLong
                (entity.getCreateTime())));
        ((UserTransationHolder) holder).flowType.setText(getFlowType(entity));
        float tranNum = Float.parseFloat(entity.getTranNum());
        ((UserTransationHolder) holder).tranNum.setText(tranNum < 0 ? entity.getTranNum() :
                "+" + entity.getTranNum());
        String statusComments;
        if (flowType == 2 || flowType == 4) {

            if (-1 == entity.getSoldStatus()) {
                statusComments = String.format(REJECT_FORMAT, entity.getComments());
            } else if (0 == entity.getSoldStatus()) {
                statusComments = "未处理";
            } else if (1 == entity.getSoldStatus()) {
                statusComments = "已处理";
            } else {
                statusComments = String.format(DATA_ERROR_FORMAT, entity.getSoldStatus());
            }

        } else if (flowType == 1) {
            if (-1 == entity.getSoldStatus()) {
                statusComments = String.format(REJECT_FORMAT, entity.getComments());
            } else if (0 == entity.getSoldStatus()) {
                statusComments = "未处理";
            } else if (1 == entity.getSoldStatus()) {
                statusComments = "已到账";
            } else {
                statusComments = String.format(DATA_ERROR_FORMAT, entity.getSoldStatus());
            }
        } else {
            statusComments = "";
        }
        ((UserTransationHolder) holder).statusComments.setText(statusComments);
    }

    /**
     * 1 兑现 2充值  3购物
     *
     * @param entity 实体对象
     * @return
     */
    private String getFlowType(UserTransationEntity entity) {
        String type = entity.getFlowType();
        String typeContent = "";
        switch (flowType) {
            case 1:
                switch (type) {
                    case "1":
                        typeContent = "兑现";
                        break;
                    case "2":
                        typeContent = "充值";
                        break;
                    case "3":
                        typeContent = "购物";
                        break;
                    case "4":
                        typeContent = "奖励";
                        break;
                }
                break;
            case 2:
                switch (type) {
                    case "1":
                        typeContent = "卖出";
                        break;
                    case "2":
                        if (1 == entity.getIncomeType()) {
                            typeContent = "静态获得";
                        } else if (2 == entity.getIncomeType()) {
                            typeContent = "分享奖励";
                        } else {
                            typeContent = "数据异常";
                        }
                        break;
                }
                break;
            case 3:
                switch (type) {
                    case "1":
                        typeContent = "换卡";
                        break;
                    case "2":
                        typeContent = "兑现";
                        break;
                    case "3":
                        typeContent = "奖励";
                        break;
                }
                break;
            case 4:
                typeContent = "奖励";
                break;
        }
        return typeContent;
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

    public List<UserTransationEntity> getEntities() {
        return mEntities;
    }

    public void swapData(List<UserTransationEntity> entities) {
        if (null != entities) {
            this.mEntities = entities;
            notifyDataSetChanged();
        }
    }

    public void swapMoreData(List<UserTransationEntity> entities) {
        if (null != entities && entities.size() > 0) {
            this.mEntities.addAll(entities);
            notifyDataSetChanged();
        }
    }
}
