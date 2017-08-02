package com.peng.com.peng.model;

/**
 * Created by 高信朋 on 2017/7/29.
 * ImageView的模型类，存放ImageView的左上角距离左边界和上边界的距离和图片的宽和高
 */

public class ImgModel {
    private float maginLeft = 0;
    private float maginTop = 0;
    private float width = 0;
    private float height = 0;

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {

        return weight;
    }

    private float weight = 0;

    /**
     * 设置图片距离左边界的距离
     *
     * @param maginLeft 距离左边界的距离
     */
    public void setMaginLeft(float maginLeft) {
        this.maginLeft = maginLeft;
    }

    /**
     * 设置图片距离上边界的距离
     *
     * @param maginTop 距离上边界的距离
     */
    public void setMaginTop(float maginTop) {
        this.maginTop = maginTop;
    }

    /**
     * 设置图片的宽度
     *
     * @param width 图片的宽度
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * 设置图片的高度
     *
     * @param height 图片的高度
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * 获取图片距离左边界的距离
     *
     * @return 图片距离左边界的距离
     */
    public float getMaginLeft() {
        return maginLeft;
    }

    /**
     * 获取图片距离上边界的距离
     *
     * @return 图片距离上边界的距离
     */
    public float getMaginTop() {
        return maginTop;
    }

    /**
     * 获取图片的宽度
     *
     * @return 图片的宽度
     */
    public float getWidth() {
        return width;
    }

    /**
     * 获取图片的高度
     *
     * @return 图片的高度
     */
    public float getHeight() {
        return height;
    }


}
