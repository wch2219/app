package com.xxc.shoppingmall.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.adapter.ProductAdapter;
import com.xxc.shoppingmall.adapter.ShopScreenPopuAdapter;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.ProductDescActivity;
import com.xxc.shoppingmall.ui.base.BaseLazyFragment;
import com.xxc.shoppingmall.widget.MyPopuWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by xuxingchen on 2017/11/17.
 * 主界面---商城
 */
public class SMallFragment extends BaseLazyFragment implements ProductAdapter.OnItemClickListener {

    @BindView(R.id.shopping_mall_web_loader)
    BridgeWebView mShoppingMallWebLoader;
    //    @BindView(R.id.easyRefreshLayout)
//    EasyRefreshLayout mEasyRefreshLayout;
    @BindView(R.id.browser_error)
    ImageView mBrowserError;
    @BindView(R.id.recyclerList)
    RecyclerView recyclerList;
    @BindView(R.id.easylayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_seach)
    EditText etSeach;
    @BindView(R.id.rl_scren)
    RelativeLayout rlScren;
    Unbinder unbinder;
    private List<ProductListBean.DataBean> data = new ArrayList<>();
    private ProductAdapter adapter;
    private MyPopuWindow popu;

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_shopping_mall;
    }

    @Override
    public void initUI() {
//        mEasyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        refreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
        refreshLayout.setReboundDuration(300);//回弹动画时长（毫秒）
        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹
        refreshLayout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
        refreshLayout.setRefreshHeader(new WaterDropHeader(getActivity()));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new FalsifyFooter(getActivity()));

        GridLayoutManager mManager = new GridLayoutManager(this.getContext(), 2);
        recyclerList.setLayoutManager(mManager);
        adapter = new ProductAdapter(data, getActivity());
        adapter.setOnItemClickListener(this);
        recyclerList.setAdapter(adapter);
        page = 1;
        data.clear();
        getProductDate(page);

    }

    /**
     * 联网获取商品列表
     *
     * @param
     */
    private void getProductDate(int page) {
        Map<String, Object> params = new HashMap<>();
        //传入用户id
        params.put(ParamKey.PAGE, page);
        params.put(ParamKey.LIMIT, 10);
        Call<ProductListBean> call = mHttpApi.GetProductList(params);
        EasyCallBack<ProductListBean> callBack = new EasyCallBack<ProductListBean>() {
            @Override
            public void onSuccess(Call<ProductListBean> call, ProductListBean model) {
                data.addAll(model.getData());
                LogUtils.d(data.size() + "商品数量");
                adapter.notifyDataSetChanged();
            }
        };
        requestApi(call, callBack);
    }

    @Override
    public void initData() {

    }

    private void initpopu() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popu_screen, null);
        final ViewHolder popuHolder = new ViewHolder(view);
        popuHolder.lv_list.setAdapter(new ShopScreenPopuAdapter(getActivity()));
        popu = new MyPopuWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popu.setOutsideTouchable(true);
        popu.setFocusable(true);
        popu.setBackgroundDrawable(new BitmapDrawable());
        popu.showAsDropDown(rlScren,10,20);
        popuHolder.lv_list.setOnItemClickListener(onItemClickListener);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                if (rawY > popuHolder.lv_list.getY()|| rawX<popuHolder.lv_list.getX()) {
                    popu.dismiss();
                    popu = null;

                }
                return false;
            }
        });
    }
    /*隐藏软键盘*/
    public void inputhind(View view){

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private int page;

    @Override
    public void addListeners() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
//                page=1;
//                data.clear();
//                getProductDate(page);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                page++;
                getProductDate(page);
            }

        });

        etSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //TODO
                    ToastUtils.showToast(getActivity(),"搜索成功");
                    inputhind(etSeach);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(int postion) {
        ProductListBean.DataBean dataBean = data.get(postion);
        Intent intent = new Intent(getActivity(), ProductDescActivity.class);
        intent.putExtra("date", dataBean);
        startActivity(intent);
    }
    @OnClick(R.id.rl_scren)
    public void onViewClicked() {
      initpopu();
    }
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popu.dismiss();
        }
    };
    private class ViewHolder {
        public View rootView;
        public ListView lv_list;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.lv_list = (ListView) rootView.findViewById(R.id.lv_list);
        }

    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
}
