package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.Position;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/21.
 * 实体店界面
 */
public class OfflineMallActivity extends AbstractPermissionActivity {
    @BindView(R.id.offline_mall_map)
    ImageView mOfflineMallMap;
    @BindView(R.id.offline_mall_location_map)
    MapView mOfflineMallLocationMap;

    private AMap mAMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EasyPermissions

        mOfflineMallLocationMap.onCreate(savedInstanceState);

        //初始化地图控制器对象
        mAMap = mOfflineMallLocationMap.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //初始化定位蓝点样式类myLocationStyle.myLocationType
        // (MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        LoginResult user = ShoppingMallApp.getInstance().getUser();
        initPositions(user.getData().getUserId());
    }

    private void initPositions(String userId) {
        Call<Position> call = mHttpApi.getPositions(userId);
        Callback<Position> callback = new EasyCallBack<Position>() {
            @Override
            public void onSuccess(Call<Position> call, Position model) {
                if (null != model && model.getMsg().isSuccess()) {
                    for (int i = 0; i < model.getData().size(); i++) {
                        Position.DataBean dataBean = model.getData().get(i);
                        if (0 != dataBean.getLatitude() && 0 != dataBean.getLongitude()) {
                            LatLng latLng = new LatLng(dataBean.getLatitude(), dataBean
                                    .getLongitude());
                            MarkerOptions markerOption = new MarkerOptions();
                            markerOption.position(latLng);
                            markerOption.title(dataBean.getNickName());
                            markerOption.draggable(true);//设置Marker可拖动
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable
                                            .map_position)));
                            markerOption.visible(true);
                            mAMap.addMarker(markerOption);
                        }
                    }
                } else {
                    ToastUtils.showToast(OfflineMallActivity.this, model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_offline_mall;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mOfflineMallLocationMap.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOfflineMallLocationMap.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOfflineMallLocationMap.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mOfflineMallLocationMap.onSaveInstanceState(outState);
    }

    @OnClick(R.id.offline_mall_map)
    public void onViewClicked() {
        Intent intent = new Intent();
        //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(NetConstant.OFFICIAL_URL);
        intent.setData(content_url);
        startActivity(intent);
    }

}
