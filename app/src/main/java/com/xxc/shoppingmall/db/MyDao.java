package com.xxc.shoppingmall.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.model.ShopCatListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by scy on 2017/3/3.
 */

public class MyDao {
    private static MyDao instence = null;
    private final String tableName = "shopcatlist";
    private DatabaseHelper helper;
    public MyDao(Context ctx){
            helper = new DatabaseHelper(ctx,"hongji.db");
        SQLiteDatabase db1 = helper.getWritableDatabase();//必要条件
    }
    public static synchronized MyDao getInstence(Context ctx){
        if (instence == null) {
            instence = new MyDao(ctx);
        }
        return instence;
    }

    /**
     * 插入数据到数据库
     * @param
     */
    public void setInsert(ProductListBean.DataBean dataBean,Context ctx){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("shopid",dataBean.getId());
        values.put("description",dataBean.getDescription());
        values.put("imgUrl",dataBean.getImgUrl());
        values.put("name",dataBean.getName());
        values.put("price",dataBean.getPrice());
        values.put("productCode",dataBean.getProductCode());
        values.put("status",dataBean.getStatus());
        values.put("num",1);
        db.insert(tableName,null,values);
        Toast.makeText(ctx, "成功加入购物车", Toast.LENGTH_SHORT).show();
        db.close();
    }

    /**
     * 删除数据
     */
    public void deleteData(int shopid){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(tableName,"shopid=?",new String[]{""+shopid});
    }

    /**
     * 修改数据
     */
    public void upData(int id,int num,Context ctx){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("num",num);
        db.update(tableName,values, "shopid=?",new String[]{""+id});

        db.close();
    }
     /**
     * 修改数据
     */
    public void upDataCheck(int id,boolean checked,Context ctx){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("checked",checked);
        db.update(tableName,values, "shopid=?",new String[]{""+id});

        db.close();
    }


    /**
     * 查询全部数据
     * @return
     */
    public List<ShopCatListBean> quaryAllData(){
        List<ShopCatListBean> names = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor user = db.query(tableName, null, null, null, null, null, null);//查询全部
//        Cursor user = db.query(tableName, new String[]{"id", "name"}, "id=?", new String[]{"1"}, null, null, null, null);
        while (user.moveToNext()){
            ShopCatListBean catListBean = new ShopCatListBean();
            int shopid = user.getInt(user.getColumnIndex("shopid"));
            String description = user.getString(user.getColumnIndex("description"));
            String imgUrl = user.getString(user.getColumnIndex("imgUrl"));
            String name = user.getString(user.getColumnIndex("name"));
            double price = user.getDouble(user.getColumnIndex("price"));
            String productCode = user.getString(user.getColumnIndex("productCode"));
            int status = user.getInt(user.getColumnIndex("status"));
            int num = user.getInt(user.getColumnIndex("num"));
            catListBean.setShopid(shopid);
            catListBean.setDescription(description);
            catListBean.setImgUrl(imgUrl);
            catListBean.setName(name);
            catListBean.setPrice(price);
            catListBean.setProductCode(productCode);
            catListBean.setStatus(status);
            catListBean.setNum(num);
            names.add(catListBean);
        }
        return names;
    }

    /**
     * 查询单个
     * @param dataBean
     */
    public void queryDate(ProductListBean.DataBean dataBean,int num,Context ctx){
        int queryNum=0;
        List<ShopCatListBean> names = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor user = db.query(tableName, null, "shopid=?", new String[]{dataBean.getId()+""}, null, null, null, null);
        while (user.moveToNext()){
            queryNum += user.getInt(user.getColumnIndex("num"));

        }
        if (queryNum == 0) {
            setInsert(dataBean,ctx);
        }else{
            upData(dataBean.getId(),num+queryNum,ctx);
            ToastUtils.showToast(ctx,"成功加入购物车");
        }
    }

}
