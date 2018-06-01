package com.mingbaipintu.customLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mingbaipintu.GameManager;
import com.mingbaipintu.ListViewAdapter;
import com.mingbaipintu.MyApplication;
import com.mingbaipintu.R;
import com.mingbaipintu.UIManager;
import com.mingbaipintu.UpdateTitleTimer;
import com.mingbaipintu.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCX on 2018/05/28.
 */
/**
 * 继承自ListView，用于定义Setting菜单各个子项的点击功能及功能实现的接口
 */
public class SettingList extends ListView {

    public static final String CUSTOM = "选图";
    public static final String FINGHT = "闯关";//
    private static final String[] DIFFICULTY = {"(3x3)", "(4x4)", "(5x5)", "(7x7)", "(9x9)"};


    public SettingList(Context context) {
        super(context);
        init();
    }

    public SettingList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setVisibility(INVISIBLE);
        setAlpha(0.8f);
        setListContent(false);
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setVisibility(INVISIBLE);
               final  GameManager gameManager = GameManager.getInstance();
                int diff=0;
                switch (position) {
                    case 0:
                        gameManager.startChooseActivity();
                        break;
                    case 1:
                        if (gameManager.getmLevel() > 15) {
                          /* Toast.makeText(gameManager.getMainActivity(), "已通关", Toast.LENGTH_LONG).show();
                            return;*/
                      final Activity mainActivity= gameManager.getMainActivity();
                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                            builder.setMessage("已通关，是否重新闯关?");
                            builder.setTitle("提醒");
                            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    gameManager.setmLevel(1);
                                    gameManager.setmIsCustom(false);
                                    UpdateTitleTimer.getInstance().concelTimer();
                                    gameManager.setCurrentImageFromResource(gameManager.getmLevel());
                                    gameManager.caculatemDiff();
                                    UIManager.getInstance().initGamingView(gameManager.getmDiff());
                                    gameManager.splitBitmap();
                                    gameManager.gameReady();
                                }
                            });
                            builder.create().show();
                            return;
                        }//共15关
                        else {
                            gameManager.setmIsCustom(false);
                            UpdateTitleTimer.getInstance().concelTimer();
                            gameManager.setCurrentImageFromResource(gameManager.getmLevel());
                            gameManager.caculatemDiff();
                            UIManager.getInstance().initGamingView(gameManager.getmDiff());
                            gameManager.splitBitmap();
                            gameManager.gameReady();
                        }
                        break;
                    case 2:
                        diff=3;
                        break;
                    case 3:
                        diff=4;
                        break;
                    case 4:
                        diff=5;
                        break;
                    case 5:
                        diff=7;
                        break;
                    case 6:
                        diff=9;
                        break;
                }
                if(diff!=0) {
                   // if (diff > 9) {
                    //    Toast.makeText(gameManager.getMainActivity(), "你是人才(^_^)", Toast.LENGTH_SHORT).show();
                    //}
                    gameManager.setmDiff(diff);
                    UpdateTitleTimer.getInstance().concelTimer();
                    UIManager.getInstance().initGamingView(diff);
                    gameManager.splitBitmap();
                    gameManager.gameReady();
                }
            }
        });
    }

    public void setListContent(boolean isCustom) {
        List<String> dataList;
        if (isCustom) {
            dataList = new ArrayList<>();
            dataList.add(CUSTOM);
            dataList.add(FINGHT);
            dataList.add(DIFFICULTY[0]);
            dataList.add(DIFFICULTY[1]);
            dataList.add(DIFFICULTY[2]);
            dataList.add(DIFFICULTY[3]);
            dataList.add(DIFFICULTY[4]);
        } else {
            dataList = new ArrayList<>();
            dataList.add(CUSTOM);
        }
        ListViewAdapter adapter = new ListViewAdapter(MyApplication.getContextObject(), R.layout.option_item, dataList);
        setAdapter(adapter);
    }
}
