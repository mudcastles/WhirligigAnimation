package com.peng.whirligiganimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.peng.com.peng.model.ImgModel;
import com.peng.pisitionAndSize.PositionAndSizeAdapter;

import java.util.AbstractQueue;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    /**
     * 标志是否已经移动了一次图片资源。以便将图片更换位置（LinkedList发生过一次变化）
     * 此参数会在滑动距离超过1/3屏幕宽度时修改为true，并在手势按下时修改为false
     * 此参数还可以帮助在处理手势抬起（Up）时，判断是否发生过LinkedList的元素变化
     */
    private boolean flag = false;

    /**
     * 这个值是用来保存手势在x方向上滑动了多少像素值
     */
    private float x;

    /**
     * 屏幕尺寸
     */
    private int width_screen = 0;
    private int height_screen = 0;

    /**
     * 需要展示的五张图片
     */
    private ImageView[] imgs = null;

    /**
     * 存放五张图片的源文件的LinkedList
     */
    private LinkedList<Integer> pictures = new LinkedList<>();


    /**
     * 存放五张图片的id的数组
     */
    private int[] imgIDs = {R.id.img1View, R.id.img2View, R.id.img3View, R.id.img4View, R.id.img5View};
    /**
     * 存放img属性值的类ImgModel的数组
     */
    private ImgModel[] models = null;

    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetector(this);

        /**
         *首先 获取屏幕尺寸
         */
        WindowManager windowManager = getWindowManager();
        width_screen = windowManager.getDefaultDisplay().getWidth();
        height_screen = windowManager.getDefaultDisplay().getHeight();

        imgs = new ImageView[imgIDs.length];
        /**
         * 其次 根据图片数量获取五张图片的正确显示位置
         */
        PositionAndSizeAdapter adapter = new PositionAndSizeAdapter(imgIDs.length, width_screen, height_screen);
        models = adapter.getImgModels();

        /**
         * 最后 根据得到的位置为每一张图片设置参数：位置和宽高
         */
        for (int i = 0; i < imgIDs.length; i++) {
            imgs[i] = (ImageView) findViewById(imgIDs[i]);
            imgs[i].setX(models[i].getMaginLeft());
            imgs[i].setY(models[i].getMaginTop());

            ViewGroup.LayoutParams params = imgs[i].getLayoutParams();
            params.width = (int) models[i].getWidth();
            params.height = (int) models[i].getHeight();
            imgs[i].setLayoutParams(params);
            imgs[i].setSaveEnabled(true);

//            ObjectAnimator animator1 = ObjectAnimator.ofFloat(imgs[i],"translationX",models[i].getMaginLeft(),models[i+1].getMaginLeft());
//
//            AnimatorSet set = new AnimatorSet();

//            imgs[i].setImageLevel(1);
        }

        /**
         * 为LinkedList增加五张图片的资源，然后在滑动的时候将LinkedList的头弹出，并在尾部重新加入到LinkedList中，这样就能够让图片移动在LinkedList中的位置
         */
        pictures.add(R.drawable.img1);
        pictures.add(R.drawable.img2);
        pictures.add(R.drawable.img3);
        pictures.add(R.drawable.img4);
        pictures.add(R.drawable.img5);


//        /**
//         * 这里使用属性动画模拟滑动
//         */
//        ValueAnimator animator = ValueAnimator.ofInt(1, 10);
//        animator.setDuration(10000);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            int value;
//            int begin = 1;
//
//            /**
//             * 此方法会不停被调用，也就是说，不仅仅是当10000/10=1000ms=1s时才调用一次。
//             * @param animation
//             */
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                value = (Integer) animation.getAnimatedValue();
//                if (value != begin) {
//                    begin++;
//                    int resource = 0;
//                    resource = pictures.poll();
//                    pictures.add(resource);
//
//                    for (int i = 0; i < imgIDs.length; i++) {
//                        imgs[i].setImageDrawable(getResources().getDrawable(pictures.get(i)));
////                    pictures.offer(resource);
//                    }
//                }
//            }
//        });
//        animator.start();


    }

    @Override
    public boolean onDown(MotionEvent e) {
        flag = false;
        return true;
    }


    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        x = e2.getX() - e1.getX();
        float maginLeft = 0;
        float maginTop = 0;
        float width = 0;
        float height = 0;
        if (Math.abs(x) <= 1.0 / 3.0 * width_screen && x > 0) {
            for (int i = imgIDs.length - 1; i >= 0; i--) {
                if (flag) {
//                    此部分代码是为了抵消当x>1/3*width_screen时对五个ImageView进行初始化造成的位置偏差
                    if (i != imgIDs.length - 1) {
                        maginLeft = models[i].getMaginLeft() - (width_screen-3*x)*(models[i].getMaginLeft()-models[i+1].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop()- (width_screen-3*x)*(models[i].getMaginTop()-models[i+1].getMaginTop())/ width_screen;
                        width = models[i].getWidth() - (width_screen-3*x)*(models[i].getWidth()-models[i+1].getWidth()) / width_screen;
                        height = models[i].getHeight() - (width_screen-3*x)*(models[i].getHeight()-models[i+1].getHeight()) / width_screen;
                    } else {
                        maginLeft = models[i].getMaginLeft() - (width_screen-3*x)*(models[i].getMaginLeft()-models[0].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop() - (width_screen-3*x)*(models[i].getMaginTop()-models[0].getMaginTop()) / width_screen;
                        width = models[i].getWidth() - (width_screen-3*x)*(models[i].getWidth()-models[0].getWidth()) / width_screen;
                        height = models[i].getHeight()- (width_screen-3*x)*(models[i].getHeight()-models[0].getHeight()) / width_screen;
                    }
                } else {

                    if (i != 0) {
                        maginLeft = models[i].getMaginLeft() - 3 * x * (models[i].getMaginLeft() - models[i - 1].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop() - 3 * x * (models[i].getMaginTop() - models[i - 1].getMaginTop()) / width_screen;
                        width = models[i].getWidth() - 3 * x * (models[i].getWidth() - models[i - 1].getWidth()) / width_screen;
                        height = models[i].getHeight() - 3 * x * (models[i].getHeight() - models[i - 1].getHeight()) / width_screen;
                    } else {
                        maginLeft = models[i].getMaginLeft() - 3 * x * (models[i].getMaginLeft() - models[imgIDs.length - 1].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop() - 3 * x * (models[i].getMaginTop() - models[imgIDs.length - 1].getMaginTop()) / width_screen;
                        width = models[i].getWidth() - 3 * x * (models[i].getWidth() - models[imgIDs.length - 1].getWidth()) / width_screen;
                        height = models[i].getHeight() - 3 * x * (models[i].getHeight() - models[imgIDs.length - 1].getHeight()) / width_screen;
                    }
                }
                imgs[i].setX(maginLeft);
                imgs[i].setY(maginTop);
                ViewGroup.LayoutParams params = imgs[i].getLayoutParams();
                params.width = (int) width;
                params.height = (int) height;
                imgs[i].setLayoutParams(params);
            }
        }
        if (Math.abs(x) <= 1.0 / 3.0 * width_screen && x < 0) {
            for (int i = imgIDs.length - 1; i >= 0; i--) {
                if (flag) {
//                    此部分代码是为了抵消当x>1/3*width_screen时对五个ImageView进行初始化造成的位置偏差
                    if (i != 0) {
                        maginLeft = models[i].getMaginLeft() - (width_screen + 3 * x) * (models[i].getMaginLeft() - models[i - 1].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop() - (width_screen + 3 * x) * (models[i].getMaginTop() - models[i - 1].getMaginTop()) / width_screen;
                        width = models[i].getWidth() - (width_screen + 3 * x) * (models[i].getWidth() - models[i - 1].getWidth()) / width_screen;
                        height = models[i].getHeight() - (width_screen + 3 * x) * (models[i].getHeight() - models[i - 1].getHeight()) / width_screen;
                    } else {
                        maginLeft = models[i].getMaginLeft() - (width_screen + 3 * x) * (models[0].getMaginLeft() - models[imgIDs.length - 1].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop() - (width_screen + 3 * x) * (models[0].getMaginTop() - models[imgIDs.length - 1].getMaginTop()) / width_screen;
                        width = models[i].getWidth() - (width_screen + 3 * x) * (models[0].getWidth() - models[imgIDs.length - 1].getWidth()) / width_screen;
                        height = models[i].getHeight() - (width_screen + 3 * x) * (models[0].getHeight() - models[imgIDs.length - 1].getHeight()) / width_screen;
                    }
                } else {

                    if (i != imgIDs.length - 1) {
                        maginLeft = models[i].getMaginLeft() + 3 * x * (models[i].getMaginLeft() - models[i + 1].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop() + 3 * x * (models[i].getMaginTop() - models[i + 1].getMaginTop()) / width_screen;
                        width = models[i].getWidth() + 3 * x * (models[i].getWidth() - models[i + 1].getWidth()) / width_screen;
                        height = models[i].getHeight() + 3 * x * (models[i].getHeight() - models[i + 1].getHeight()) / width_screen;
                    } else {
                        maginLeft = models[i].getMaginLeft() + 3 * x * (models[i].getMaginLeft() - models[0].getMaginLeft()) / width_screen;
                        maginTop = models[i].getMaginTop() + 3 * x * (models[i].getMaginTop() - models[0].getMaginTop()) / width_screen;
                        width = models[i].getWidth() + 3 * x * (models[i].getWidth() - models[0].getWidth()) / width_screen;
                        height = models[i].getHeight() + 3 * x * (models[i].getHeight() - models[0].getHeight()) / width_screen;
                    }
                }
                imgs[i].setX(maginLeft);
                imgs[i].setY(maginTop);
                ViewGroup.LayoutParams params = imgs[i].getLayoutParams();
                params.width = (int) width;
                params.height = (int) height;
                imgs[i].setLayoutParams(params);
            }
        }
//        如果x已经超过1/3屏幕宽度，则认为img已经到达了下一个位置
        if (Math.abs(x) > 1.0 / 3.0 * width_screen) {

            if (!flag && x > 0) {
                int resource = 0;
                resource = pictures.poll();
                pictures.add(resource);
                flag = true;
            }
            if (!flag && x < 0) {
                int resource = 0;
                resource = pictures.removeLast();
                pictures.offerFirst(resource);
                flag = true;
            }
            for (int i = 0; i < imgIDs.length; i++) {
                imgs[i].setX(models[i].getMaginLeft());
                imgs[i].setY(models[i].getMaginTop());

                ViewGroup.LayoutParams params = imgs[i].getLayoutParams();
                params.width = (int) models[i].getWidth();
                params.height = (int) models[i].getHeight();
                imgs[i].setLayoutParams(params);
                imgs[i].setSaveEnabled(true);
                imgs[i].setImageDrawable(getResources().getDrawable(pictures.get(i)));
            }
        }
        if (Math.abs(x) < 10) {
            if (flag && x > 0) {
                int resource = 0;
                resource = pictures.removeLast();
                pictures.offerFirst(resource);
            }
            if (flag && x < 0) {
                int resource = 0;
                resource = pictures.poll();
                pictures.add(resource);
            }
            flag = false;
            for (int i = 0; i < imgIDs.length; i++) {

                imgs[i].setX(models[i].getMaginLeft());
                imgs[i].setY(models[i].getMaginTop());

                ViewGroup.LayoutParams params = imgs[i].getLayoutParams();
                params.width = (int) models[i].getWidth();
                params.height = (int) models[i].getHeight();
                imgs[i].setLayoutParams(params);
                imgs[i].setSaveEnabled(true);
                imgs[i].setImageDrawable(getResources().getDrawable(pictures.get(i)));
            }
        }

        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(x) < 1.0 / 3.0 * width_screen) {
            if (flag && x > 0) {
                int resource = 0;
                resource = pictures.removeLast();
                pictures.offerFirst(resource);
            }
            if (flag && x < 0) {
                int resource = 0;
                resource = pictures.poll();
                pictures.add(resource);
            }
        }
        for (int i = 0; i < imgIDs.length; i++) {
            imgs[i].setX(models[i].getMaginLeft());
            imgs[i].setY(models[i].getMaginTop());

            ViewGroup.LayoutParams params = imgs[i].getLayoutParams();
            params.width = (int) models[i].getWidth();
            params.height = (int) models[i].getHeight();
            imgs[i].setLayoutParams(params);
            imgs[i].setSaveEnabled(true);
            imgs[i].setImageDrawable(getResources().getDrawable(pictures.get(i)));
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
////        这里监听手势Up的情况，抬起后应该让图片到应该在的位置
//        boolean isUp = (event.getAction() == MotionEvent.ACTION_UP);
////        gestureDetector.onTouchEvent(event)这句的作用是，将触摸事件交给gestureDetector处理
//        if (!gestureDetector.onTouchEvent(event) && isUp) {
//            Log.d("!!!!!!!!!!!!!", "onTouchEvent: ~~~~~~~~~~~~~~~~~~~~~~~");
//
//        }
//        return true;
        return gestureDetector.onTouchEvent(event);

    }


}
