package com.example.mooreli.game2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MooreLi on 2017/2/10.
 */

public class GameView extends View {
    private Context mContext;

    private int mWidth, mHeight;
    //每一格的宽度
    private int mUnit;
    //背景画笔
    private Paint mPaintBlock;
    //数字画笔
    private Paint mPaintNumber;
    private Rect mRect;
    private int padding;
    //总共16格
    private Number[] mAllNumbers = new Number[16];
    //当前显示的数字
    private List<Number> mShowingViews;
    //手指按下时的位置
    private int mDownX, mDownY;
    //方向 上下左右
    private final int DIRECTION_UP = 0;
    private final int DIRECTION_DOWN = 1;
    private final int DIRECTION_LEFT = 2;
    private final int DIRECTION_RIGHT = 3;
    //是否继续循环移动
    private boolean canMove2Left = true;
    private boolean canMove2Right = true;
    private boolean canMove2Up = true;
    private boolean canMove2Down = true;
    //游戏是否结束
    private boolean isGameOver = false;
    //游戏结束监听
    private OnGameOverListener mGameOverListener;

    public GameView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化画笔、新建两个数字
    private void init() {
        for (int i = 0; i < mAllNumbers.length; i++) {
            mAllNumbers[i] = null;
        }
        padding = ScreenUtil.dp2px(mContext, 2);
        mRect = new Rect();
        mPaintBlock = new Paint();
        mPaintBlock.setAntiAlias(true);
        mPaintNumber = new Paint();
        mPaintNumber.setTextSize(ScreenUtil.sp2px(mContext, 30));
        mPaintNumber.setAntiAlias(true);

        mShowingViews = new ArrayList<>();

        Number firstNum = generateNewNum();
        Number secNum = generateNewNum();

        mShowingViews.add(firstNum);
        mShowingViews.add(secNum);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = (int) event.getX();
            mDownY = (int) event.getY();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            int upX = (int) event.getX();
            int upY = (int) event.getY();

            //上下滑动
            if (Math.abs(upX - mDownX) < Math.abs(upY - mDownY)) {
                if ((upY - mDownY) >= 80) {
                    //下滑
                    handleUp(DIRECTION_DOWN);
                } else if ((mDownY - upY) >= 80) {
                    //上滑
                    handleUp(DIRECTION_UP);
                }
            }
            //左右滑动
            else {
                if ((upX - mDownX) >= 80) {
                    //右滑
                    handleUp(DIRECTION_RIGHT);
                } else if ((mDownX - upX) >= 80) {
                    //左滑
                    handleUp(DIRECTION_LEFT);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手指抬起，处理事件
     *
     * @param direction
     */
    private void handleUp(int direction) {
        if (isGameOver)
            return;
        switch (direction) {
            case DIRECTION_UP:
                for (int i = 0; i < mAllNumbers.length; i++) {
                    if (mAllNumbers[i] == null) {
                        continue;
                    }
                    moveNum2Up(i);
                    canMove2Up = true;
                }
                break;
            case DIRECTION_DOWN:
                for (int i = mAllNumbers.length - 1; i >= 0; i--) {
                    if (mAllNumbers[i] == null) {
                        continue;
                    }
                    moveNum2Down(i);
                    canMove2Down = true;
                }
                break;
            case DIRECTION_LEFT:
                for (int i = 0; i < mAllNumbers.length; i++) {
                    if (mAllNumbers[i] == null) {
                        continue;
                    }
                    moveNum2Left(i);
                    canMove2Left = true;
                }
                break;
            case DIRECTION_RIGHT:
                for (int i = mAllNumbers.length - 1; i >= 0; i--) {
                    if (mAllNumbers[i] == null) {
                        continue;
                    }
                    moveNum2Right(i);
                    canMove2Right = true;
                }
                break;
        }
        generateNewNum();
        add2Showing();
        invalidate();
        //通知分数 判断游戏是否结束
        isGameOver = isGameOver();
        if (mGameOverListener != null) {
            mGameOverListener.gameScore(isGameOver, getMaxValue());
        }
    }

    /**
     * 处理左滑
     *
     * @param index
     */
    private void moveNum2Left(int index) {
        Number number = mAllNumbers[index];
        if (null == number) {
            return;
        }
        int value = number.getmValue();
        //移动到最左边
        while (index % 4 > 0 && canMove2Left) {
            Number leftNum = mAllNumbers[index - 1];
            if (null == leftNum) {
                //左移一格
                calculateIndex(number, index - 1);
                mAllNumbers[index - 1] = number;
                mAllNumbers[index] = null;
                if ((index - 1) % 4 == 0)
                    canMove2Left = false;
                moveNum2Left(index - 1);
            } else {
                int leftValue = leftNum.getmValue();
                //合并到左边
                if (leftValue == value) {
                    number.setmValue(value * 2);
                    calculateIndex(number, index - 1);
                    mAllNumbers[index - 1] = number;
                    mAllNumbers[index] = null;
                    //只合并一次，不将合并后的结果再次合并
//                    moveNum2Left(index - 1);
                    canMove2Left = false;
                } else {
                    canMove2Left = false;
                }
            }
            invalidate();
        }
    }

    /**
     * 处理右滑
     *
     * @param index
     */
    private void moveNum2Right(int index) {
        Number number = mAllNumbers[index];
        if (null == number) {
            return;
        }
        int value = number.getmValue();
        while (index % 4 < 3 && canMove2Right) {
            Number rightNum = mAllNumbers[index + 1];
            if (null == rightNum) {
                //右移一格
                calculateIndex(number, index + 1);
                mAllNumbers[index + 1] = number;
                mAllNumbers[index] = null;
                if ((index + 1) % 4 == 3) {
                    canMove2Right = false;
                }
                moveNum2Right(index + 1);
            } else {
                int rightValue = rightNum.getmValue();
                if (rightValue == value) {
                    //合并到右边一格
                    number.setmValue(value * 2);
                    calculateIndex(number, index + 1);
                    mAllNumbers[index + 1] = number;
                    mAllNumbers[index] = null;
//                    moveNum2Right(index + 1);
                    canMove2Right = false;
                } else {
                    canMove2Right = false;
                }
            }
            invalidate();
        }
    }

    /**
     * 处理上滑
     *
     * @param index
     */
    private void moveNum2Up(int index) {
        Number number = mAllNumbers[index];
        if (null == number)
            return;
        int value = number.getmValue();
        while (index / 4 > 0 && canMove2Up) {
            Number upNumber = mAllNumbers[index - 4];
            if (null == upNumber) {
                //上移一格
                calculateIndex(number, index - 4);
                mAllNumbers[index - 4] = number;
                mAllNumbers[index] = null;
                if ((index - 4) / 4 == 0)
                    canMove2Up = false;
                moveNum2Up(index - 4);
            } else {
                int upValue = upNumber.getmValue();
                if (upValue == value) {
                    //合并到上一格
                    number.setmValue(value * 2);
                    calculateIndex(number, index - 4);
                    mAllNumbers[index - 4] = number;
                    mAllNumbers[index] = null;
                    canMove2Up = false;
                    //将下方的数字全部往上移一格


                } else {
                    canMove2Up = false;
                }
            }
            invalidate();
        }
    }

    /**
     * 处理下滑
     *
     * @param index
     */
    private void moveNum2Down(int index) {
        Number number = mAllNumbers[index];
        if (null == number)
            return;
        int value = number.getmValue();
        while (index / 4 < 3 && canMove2Down) {
            Number downNumber = mAllNumbers[index + 4];
            if (null == downNumber) {
                //移动到下一格
                calculateIndex(number, index + 4);
                mAllNumbers[index + 4] = number;
                mAllNumbers[index] = null;
                if ((index + 4) / 4 == 3)
                    canMove2Down = false;
                moveNum2Down(index + 4);
            } else {
                int downValue = downNumber.getmValue();
                if (value == downValue) {
                    //合并到下一格
                    number.setmValue(value * 2);
                    calculateIndex(number, index + 4);
                    mAllNumbers[index + 4] = number;
                    mAllNumbers[index] = null;
                    canMove2Down = false;
                } else {
                    canMove2Down = false;
                }
            }
        }
    }

    /**
     * 将显示的格子添加到列表
     */
    private void add2Showing() {
        mShowingViews.clear();
        for (int i = 0; i < mAllNumbers.length; i++) {
            Number number = mAllNumbers[i];
            if (null != number) {
                mShowingViews.add(number);
            }
        }
    }

    /**
     * 根据index计算数字所处的位置
     *
     * @param number
     * @param index
     */
    private void calculateIndex(Number number, int index) {
        number.setIndexX(index % 4);
        number.setIndexY(index / 4);
    }

    /**
     * 根据所处位置计算x和y的坐标
     */
    private void calculateLocation() {
        int realWidth = mWidth - margin * 2;
        Log.e("GameView", "width:" + mWidth + "  realWidth:" + realWidth);
        mUnit = (int) (realWidth / 4.0f);
        for (int i = 0; i < mShowingViews.size(); i++) {
            Number number = mShowingViews.get(i);
            int indexX = number.getIndexX();
            number.setmLocationX(mUnit * indexX + margin);
            int indexY = number.getIndexY();
            number.setmLocationY(mUnit * indexY + margin);
        }
    }

    /**
     * 生成一个新的数字
     *
     * @return
     */
    private Number generateNewNum() {
        Number number = new Number(mContext);
        //获取当前没有数字的格子
        List<Integer> freeIndexs = new ArrayList<>();
        for (int i = 0; i < mAllNumbers.length; i++) {
            if (mAllNumbers[i] == null) {
                freeIndexs.add(i);
            }
        }
        if (freeIndexs.size() == 0) {
            return null;
        }
        //从空闲格子中随机选一个，下标，所以减1
        int count = freeIndexs.size() - 1;
        int random = getRandom(0, count);
        int freeIndex = freeIndexs.get(random);
        number.setIndexX(freeIndex % 4);
        number.setIndexY(freeIndex / 4);
        //数值随机为2或4
        int valueRandom = getRandom(0, 1);
        number.setmValue(valueRandom == 0 ? 2 : 4);
        mAllNumbers[freeIndex] = number;
        return number;
    }

    /**
     * 判断游戏是否结束
     *
     * @return
     */
    private boolean isGameOver() {
        //如果当前显示格数不足16个，不结束
        if (mShowingViews.size() == 16) {
            //判断是否有能合并的数字
            boolean isHorizontalEnd = true;
            boolean isVerticalEnd = true;
            for (int i = 0; i < mAllNumbers.length - 1; i++) {
                //如果每一行最后一个，则跳过
                if (i % 4 == 3) {
                    continue;
                }
                //横向  如果找到一个相邻的值相同，则不结束
                if (mAllNumbers[i].getmValue() == mAllNumbers[i + 1].getmValue()) {
                    isHorizontalEnd = false;
                    break;
                }
            }
            for (int i = 0; i < mAllNumbers.length - 4; i++) {
                //如果每一列第一个，则跳过
                if (i / 4 == 0) {
                    continue;
                }
                //纵向  如果找到一个相邻的值相同，则不结束
                if (mAllNumbers[i].getmValue() == mAllNumbers[i + 4].getmValue()) {
                    isVerticalEnd = false;
                    break;
                }
            }
            return isHorizontalEnd && isVerticalEnd;
        } else {
            return false;
        }
    }

    /**
     * 找出最大值
     *
     * @return
     */
    private int getMaxValue() {
        int max = mShowingViews.get(0).getmValue();
        for (int i = 0; i < mShowingViews.size(); i++) {
            int value = mShowingViews.get(i).getmValue();
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateLocation();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x = mUnit * i + margin;
                int y = mUnit * j + margin;
                mPaintBlock.setColor(getResources().getColor(R.color.color_free));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(x + padding, y + padding, x + mUnit - padding, y + mUnit - padding, 10.0f, 10.0f, mPaintBlock);
                } else {
                    canvas.drawRect(x + padding, y + padding, x + mUnit - padding, y + mUnit - padding, mPaintBlock);
                }
            }
        }

        for (int i = 0; i < mShowingViews.size(); i++) {
            Number number = mShowingViews.get(i);
            //绘制背景
            mPaintBlock.setColor(number.getmColor());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(number.getmLocationX() + padding, number.getmLocationY() + padding, number.getmLocationX() + mUnit - padding, number.getmLocationY() + mUnit - padding, 10.0f, 10.0f, mPaintBlock);
            } else {
                canvas.drawRect(number.getmLocationX() + padding, number.getmLocationY() + padding, number.getmLocationX() + mUnit - padding, number.getmLocationY() + mUnit - padding, mPaintBlock);
            }
            //绘制数字
            if(number.getmValue() > 4)
            mPaintNumber.setColor(Color.parseColor("#FCFCFC"));
            else
                mPaintNumber.setColor(Color.parseColor("#999999"));
            String value = number.getmValue() + "";
            mPaintNumber.getTextBounds(value, 0, value.length(), mRect);
            int x = number.getmLocationX() + mUnit / 2 - mRect.width() / 2;
            int y = number.getmLocationY() + mUnit / 2 + mRect.height() / 2;
            canvas.drawText(value, x, y, mPaintNumber);
        }
    }

    private int margin;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        margin = getPaddingLeft();
        margin = margin > getPaddingRight() ? margin : getPaddingRight();
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = mWidth;
        setMeasuredDimension(mWidth,mHeight);
    }

    /**
     * 获取从start到end之间的一个随机数
     *
     * @param start
     * @param end
     * @return
     */
    private int getRandom(int start, int end) {
        Random rand = new Random();
        int randNum = rand.nextInt(end - start + 1) + start;
        return randNum;
    }

    public void setOnGameOverListener(OnGameOverListener onGameOverListener) {
        this.mGameOverListener = onGameOverListener;
    }

    /**
     * 重新开始
     */
    public void reset() {
        mShowingViews.clear();
        isGameOver = false;
        for (int i = 0; i < mAllNumbers.length; i++) {
            mAllNumbers[i] = null;
        }
        init();
        invalidate();
    }

    /**
     * 游戏进度监听接口
     */
    public interface OnGameOverListener {
        void gameScore(boolean isGameOver, int score);
    }
}
