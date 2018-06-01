package com.mingbaipintu.customLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mingbaipintu.GameManager;
import com.mingbaipintu.UIManager;

/**
 * Created by CCX on 2018/05/28.
 */
/**
 * 继承自ImageView，作为分裂后的子图的类，并可对子图在屏幕上的位置进行管理
 */
public class FlowImage extends ImageView {
    private FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private int titleOffY;
    private int ScreenWidthPixel;
    private int ScreenHeightPixel;//屏幕宽度及高度

    public FlowImage(Context context) {
        super(context);
        GameManager gameManager =GameManager.getInstance();
        ScreenWidthPixel = gameManager.getmWidthPixel();
        ScreenHeightPixel = gameManager.getmHeightPixel();
        this.titleOffY= UIManager.getInstance().getmTitleOffY();//获得标题栏的高度
    }

    public void setImage(Bitmap bm) {
        setImageBitmap(bm);
    }

    public void setLocation(int x, int y) {
        if (x < 0)
            x = 0;
        if (x > ScreenWidthPixel - getWidth())
            x = ScreenWidthPixel - getWidth();
        if (y < titleOffY)
            y = titleOffY;
        if (y > ScreenHeightPixel - getHeight()-1)
            y = ScreenHeightPixel - getHeight()-1;
        mParams.leftMargin = x;
        mParams.topMargin = y - titleOffY;
        setLayoutParams(mParams);
    }
}