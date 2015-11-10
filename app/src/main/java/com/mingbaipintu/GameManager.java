package com.mingbaipintu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.mingbaipintu.activity.ChooseImageActivity;
import com.mingbaipintu.activity.MainActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by DanDan on 2015/11/9.
 */
public class GameManager {
    private static GameManager mGameManager=null;
    private UIManager mUIManager;
    private LocalBroadcastManager mLocalBroadcastManager=null;
    private int mCurrentMaxDiff;
    private boolean mIsCustom;
    private int mLevel;
    private int mDiff;
    public  Bitmap[] mBitmapChips = new Bitmap[100];
    private MainActivity mMainActivity;
    private Bitmap mCurrentImage;
    private int mWidthPixel;
    private int mHeightPixel;
    private UpdateTitleTimer mTimer;
    private boolean mIsGaming;

    private GameManager() {
        mUIManager=UIManager.getInstance();
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(MyApplication.getContextObject());
        mIsCustom=false;
        mLevel=1;
    }

    public static GameManager getInstance() {
        if (mGameManager == null) {
            mGameManager = new GameManager();
        }
        return mGameManager;
    }
    public void setScreenPixel(int widthPixel,int heightPixel)
    {
        this.mWidthPixel=widthPixel;
        this.mHeightPixel=heightPixel;
    }
    public int getmWidthPixel()
    {
        return mWidthPixel;
    }
    public int getmHeightPixel()
    {
        return mHeightPixel;
    }
    public void setMainActivityContext(MainActivity activity)
    {
        mMainActivity=activity;
    }
    public Activity getMainActivity()
    {
        return mMainActivity;
    }
    public void setmIsCustom(boolean isCustom)
    {
        mIsCustom=isCustom;
    }
    public boolean IsCustom()
    {
        return mIsCustom;
    }
    public void setmDiff(int diff)
    {
        this.mDiff=diff;
    }
    public int getmCurrentMaxDiff() {
        return mCurrentMaxDiff;
    }

    public void setmLevel(int level) {
        if(level>26)
            level=26;
        this.mLevel = level;
        caculatemDiff();
    }
    public int getmLevel()
    {
        return mLevel;
    }
    public LocalBroadcastManager getmLocalBroadcastManager() {
        return mLocalBroadcastManager;
    }
    public void gameBegin()
    {
        mIsGaming=true;
        mUIManager.showGamingView();
        mUIManager.setTitleText("00:00");
        UpdateTitleTimer.getInstance().startTimer(0);
    }
    public void gameReady()
    {
        UpdateTitleTimer.getInstance().concelTimer();
        if(mIsCustom) {
            mUIManager.setTitleText("");
        }else
        {
            mUIManager.setTitleText("第" + mLevel + "关(共25关)");
        }
        mUIManager.showGameView(mCurrentImage);
    }

    public int getmDiff() {
        return mDiff;
    }
    public void gameWin()
    {
        if(mIsGaming) {
            mIsGaming=false;
            int time = UpdateTitleTimer.getInstance().concelTimer();
            if (mIsCustom) {
                mUIManager.setGameWinMarkText(time, "");
                mUIManager.setGameWinAgainButtonVisibility(true);
                mUIManager.setGameWinNextButtonText("换一张");
            } else {
                if (mLevel == 25) {
                    mUIManager.setGameWinNextButtonText("换一张");
                    mIsCustom=true;
                }
                else {
                    mUIManager.setGameWinNextButtonText("下一关");
                }
                mUIManager.setGameWinAgainButtonVisibility(false);
                mUIManager.setGameWinMarkText(time, "第" + mLevel + "关");
                mLevel++;
                int oldDiff = mDiff;
                caculatemDiff();
                if (oldDiff != mDiff) {
                    mUIManager.initGamingView(mDiff);
                }
            }
            mUIManager.showGameWinView(mCurrentImage);
        }
    }
    private void caculatemDiff() {
        if(mLevel<26) {
            switch ((mLevel - 1) / 5) {
                case 0:
                    setmDiff(3);
                    mCurrentMaxDiff = 5;
                    break;
                case 1:
                    setmDiff(5);
                    mCurrentMaxDiff = 5;
                    break;
                case 2:
                    setmDiff(7);
                    mCurrentMaxDiff = 7;
                    break;
                case 3:
                    setmDiff(9);
                    mCurrentMaxDiff = 9;
                    break;
                case 4:
                    setmDiff(10);
                    mCurrentMaxDiff = 10;
                    break;
            }
        }else
        {
            setmDiff(10);
            mCurrentMaxDiff = 10;
        }
    }
    public  void splitBitmap() {
        int width = mCurrentImage.getWidth();
        int height = mCurrentImage.getHeight();
        int w = width / mDiff;
        int h = height / mDiff;

        for (int i = 0; i < mDiff; ++i) {
            for (int j = 0; j < mDiff; j++) {
                mBitmapChips[i * mDiff + j] = Bitmap.createBitmap(mCurrentImage, j * w, i * h, w, h);
            }
        }
    }
    public Bitmap getBitmapChip(int index)
    {
        return mBitmapChips[index];
    }

    public void setCurrentImageFromResource(int index)
    {
        Resources res = MyApplication.getContextObject().getResources();
        int resourceId = ChooseImageActivity.mImagesId[index];
        Bitmap choosedImage = BitmapFactory.decodeResource(res, resourceId);
        if(choosedImage!=null) {
            choosedImage = Util.normalizeImage(choosedImage, mWidthPixel, mHeightPixel - mUIManager.getmTitleOffY());
            mCurrentImage = Util.cropBitmap(choosedImage, mWidthPixel, mHeightPixel - mUIManager.getmTitleOffY());
        }else
        {
            Toast.makeText(mMainActivity, "图片没找到！", Toast.LENGTH_LONG).show();
            return;
        }
    }
    public void setCurrentImageFromUri(Uri uri)
    {
        InputStream is = null;
        try {
            is = MyApplication.getContextObject().getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap choosedImage = BitmapFactory.decodeStream(is);
        if(choosedImage!=null) {
            choosedImage = Util.normalizeImage(choosedImage, mWidthPixel, mHeightPixel - mUIManager.getmTitleOffY());
            mCurrentImage = Util.cropBitmap(choosedImage, mWidthPixel, mHeightPixel - mUIManager.getmTitleOffY());
        }else
        {
            Toast.makeText(mMainActivity, "图片没找到！", Toast.LENGTH_LONG).show();
            return;
        }
    }
    public void startChooseActivity() {
        Intent chooseImageIntent = new Intent(mMainActivity, ChooseImageActivity.class);
        mMainActivity.startActivityForResult(chooseImageIntent, MainActivity.CHOOSE_IMAGE);
    }
}

