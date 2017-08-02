package com.peng.pisitionAndSize;

import com.peng.com.peng.model.ImgModel;

import java.util.Queue;

/**
 * Created by 高信朋 on 2017/7/29.
 * 此类用来根据图片数计算各图片位置和尺寸
 */

public class PositionAndSizeAdapter {
    /**
     * 图片总数
     */
    private int imgNum = 0;
    /**
     * 屏幕的宽度和高度
     */
    private int widthScreen;
    private int heightScreen;

    //    在最前面展示的图片的宽度占屏幕宽度的比重
    private double FRONT_IMG_WIDTH_WEIGHT = 3.0 / 5.0;
    //    在最前面展示的图片的高度占宽度的比重是5.0/8.0
    private double FRONT_IMG_HEIGHT_WEIGHT = FRONT_IMG_WIDTH_WEIGHT * 5.0 / 8.0;
    //    在最前面展示的图片的宽度(px)
    private double FRONT_IMG_WIDTH = 0;
    //    在最前面展示的图片的高度占宽度(px)
    private double FRONT_IMG_HEIGHT = 0;
    //    在最前面展示的图片的缩放比例为1.0
    private float BEGIN_SCALE_WEIGHT = 1.0f;
    //    在最后排展示的图片的缩放比例,在初始化参数时，若显示为5行及以下，则初始化为0.5f，否则初始化为0.3f
    private float END_SCALE_WEIGHT;
    //    imgNum张图片摆放在屏幕里共占用的行数
    private int imgRows = 0;
    //    imgRows行的图片每行之间的缩放比例差
    private float STEP_SCALE_WEIGHT;
    //    imgRows行的图片每行之间的角度差（此角度为两行图片左上角和椭圆中心的连线形成的夹角）
    private double STEP_ANGLE = 0;


    //    椭圆的a，单位是px
    private double A_CIRCLE;
    //    椭圆的b，单位是px，值是a的一半
    private double B_CIRCLE;


    /**
     * 根据图片总数构造PositionAndSizeAdapter类的对象
     *
     * @param imgNum       图片总数
     * @param widthScreen  屏幕的宽度
     * @param heightScreen 屏幕的高度
     */
    public PositionAndSizeAdapter(int imgNum, int widthScreen, int heightScreen) {
        this.imgNum = imgNum;
        this.widthScreen = widthScreen;
        this.heightScreen = heightScreen;
        init();
    }

    private void init() {
        FRONT_IMG_WIDTH = FRONT_IMG_WIDTH_WEIGHT * widthScreen;
        FRONT_IMG_HEIGHT = FRONT_IMG_WIDTH * 5.0 / 8.0;
        imgRows = imgNum / 2 + 1;
        if (imgRows <= 5) {
            END_SCALE_WEIGHT = 0.5f;
        } else {
            END_SCALE_WEIGHT = 0.3f;
        }
        STEP_SCALE_WEIGHT = (BEGIN_SCALE_WEIGHT - END_SCALE_WEIGHT) / (imgRows - 1);
        if (imgNum % 2 == 0) {
            STEP_ANGLE = Math.PI  / (imgRows - 1.5);
        } else {
            STEP_ANGLE = Math.PI  / (imgRows - 0.5);
        }
        A_CIRCLE = FRONT_IMG_WIDTH / 2.0;
        B_CIRCLE = A_CIRCLE / 3.0;
    }

    public ImgModel[] getImgModels() {
        double angle = 0;

        ImgModel[] models = new ImgModel[imgNum];
        for (int i = 0; i < imgRows; i++) {
            models[i] = new ImgModel();
            angle = STEP_ANGLE * i;
            if (angle != Math.PI && angle != 0) {
//                ----------------------------此部分代码是为了防止maginLeft的值小于0，部分图片溢出屏幕-------------------
                int maginLeft = (int) (1.0 / 5.0 * widthScreen - Math.sqrt((A_CIRCLE * A_CIRCLE * B_CIRCLE * B_CIRCLE) / (B_CIRCLE * B_CIRCLE + A_CIRCLE * A_CIRCLE / Math.tan(angle) / Math.tan(angle))));
                if (maginLeft<=0){
                    maginLeft = 2;
                }
//                -------------------------------结束--------------------------------------------------------
                models[i].setMaginLeft(maginLeft);
                if (angle > Math.PI / 2) {
                    models[i].setMaginTop((int) (1.0 / 2.0 * heightScreen - B_CIRCLE - Math.sqrt((A_CIRCLE * A_CIRCLE * B_CIRCLE * B_CIRCLE) / (Math.tan(angle) * Math.tan(angle) * B_CIRCLE * B_CIRCLE + A_CIRCLE * A_CIRCLE))));
                } else {
                    models[i].setMaginTop((int) (1.0 / 2.0 * heightScreen - Math.sqrt((A_CIRCLE * A_CIRCLE * B_CIRCLE * B_CIRCLE) / (Math.tan(angle) * Math.tan(angle) * B_CIRCLE * B_CIRCLE + A_CIRCLE * A_CIRCLE))));
                }
                models[i].setWeight(BEGIN_SCALE_WEIGHT - STEP_SCALE_WEIGHT * i);
                models[i].setWidth((int) (FRONT_IMG_WIDTH * models[i].getWeight()));
                models[i].setHeight((int) (FRONT_IMG_HEIGHT * models[i].getWeight()));

//                根据 models[i] 的属性值确定 models[imgNum - i] 的属性值，因为两者关于屏幕竖直方向的中心轴对称
                models[imgNum - i] = new ImgModel();
                models[imgNum - i].setMaginLeft((int) (widthScreen - models[i].getMaginLeft() - models[i].getWidth()));
                models[imgNum - i].setMaginTop(models[i].getMaginTop());
                models[imgNum - i].setWeight(models[i].getWeight());
                models[imgNum - i].setWidth(models[i].getWidth());
                models[imgNum - i].setHeight(models[i].getHeight());
            } else if (angle == Math.PI) {
                models[i].setMaginLeft((int) ((widthScreen - FRONT_IMG_WIDTH) / 2));
                models[i].setMaginTop((int) (1 / 2 * heightScreen - B_CIRCLE * 2));
                models[i].setWeight(END_SCALE_WEIGHT);
                models[i].setWidth((int) (FRONT_IMG_WIDTH * END_SCALE_WEIGHT));
                models[i].setHeight((int) (FRONT_IMG_HEIGHT * END_SCALE_WEIGHT));
            } else if (angle == 0) {
                models[i].setMaginLeft((int) (1.0 / 5.0 * widthScreen));
                models[i].setMaginTop((int) (1.0 / 2.0 * heightScreen));
                models[i].setWeight(BEGIN_SCALE_WEIGHT);
                models[i].setWidth((int) FRONT_IMG_WIDTH);
                models[i].setHeight((int) FRONT_IMG_HEIGHT);
            }

        }


        return models;
    }
}
