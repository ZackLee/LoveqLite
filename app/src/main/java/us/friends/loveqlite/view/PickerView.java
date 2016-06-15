package us.friends.loveqlite.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 滚动选择器
 *
 */
public class PickerView extends View {

    private static final int LEFT = -1;
    private static final int RIGHT = 1;

    //默认Jan
    private String currentValue = "Jan";
    /**
     * text间距和minTextSize之比
     */
    public static final float MARGIN_SCALE = 3f;

    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 3;

    private List<String> datas;

    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint;

    private float maxTextSize;
    private float minTextSize;

    private float maxTextAlpha = 255;
    private float minTextAlpha = 120;

    private int viewHeight;
    private int viewWidth;

    private float lastDownX;
    /**
     * 滑动的距离
     */
    private float moveDistance = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(moveDistance) < SPEED) {
                moveDistance = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                moveDistance = moveDistance - moveDistance / Math.abs(moveDistance) * SPEED;
            invalidate();
        }

    };

    private void performSelect() {
        if (mSelectListener != null) {
            mSelectListener.onSelect(datas.get(mCurrentSelected));
            currentValue = datas.get(mCurrentSelected);
        }
    }

    public void setData(List<String> datas) {
        this.datas = datas;
        mCurrentSelected = datas.size() / 2;
        invalidate();
    }

    /**
     * 选择选中的item的index
     *
     * @param selected
     */
    public void setSelected(int selected) {
        mCurrentSelected = selected;
        int distance = datas.size() / 2 - mCurrentSelected;
        if (distance < 0)
            for (int i = 0; i < -distance; i++) {
                moveHeadToTail();
                mCurrentSelected--;
            }
        else if (distance > 0)
            for (int i = 0; i < distance; i++) {
                moveTailToHead();
                mCurrentSelected++;
            }
        invalidate();
    }

    private void moveHeadToTail() {
        String head = datas.get(0);
        datas.remove(0);
        datas.add(head);
    }

    private void moveTailToHead() {
        String tail = datas.get(datas.size() - 1);
        datas.remove(datas.size() - 1);
        datas.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        maxTextSize = viewWidth / 7.5f;
        minTextSize = maxTextSize / 1.5f;
        isInit = true;
    }

    private void init() {
        timer = new Timer();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        int mColorText = 0x333333;
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(viewHeight / 4.0f, moveDistance);
        float size = (maxTextSize - minTextSize) * scale + minTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((maxTextAlpha - minTextAlpha) * scale + minTextAlpha));

        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (viewWidth / 2.0 + moveDistance);
        float y = (float) (viewHeight / 2.0);

        float baselineY = (y - (mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(datas.get(mCurrentSelected), x, baselineY, mPaint);

        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, LEFT);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < datas.size(); i++) {
            drawOtherText(canvas, i, RIGHT);
        }
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = MARGIN_SCALE * minTextSize * position + type
                * moveDistance;

        float scale = parabola(viewHeight / 4.0f, d);

        float size = (maxTextSize - minTextSize) * scale + minTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((maxTextAlpha - minTextAlpha) * scale + minTextAlpha));
        float x = (float) (viewWidth / 2.0 + type * d);
        float baseline = (float) (viewHeight / 2.0 - (mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(datas.get(mCurrentSelected + type * position), x,
                baseline, mPaint);
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        lastDownX = event.getX();
    }

    private void doMove(MotionEvent event) {

        moveDistance += (event.getX() - lastDownX);

        if (moveDistance > MARGIN_SCALE * minTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead();
            moveDistance = moveDistance - MARGIN_SCALE * minTextSize;
        } else if (moveDistance < -MARGIN_SCALE * minTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail();
            moveDistance = moveDistance + MARGIN_SCALE * minTextSize;
        }

        lastDownX = event.getX();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(moveDistance) < 0.0001) {
            moveDistance = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public interface onSelectListener {
        void onSelect(String text);
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    public String getCurrentValue() {
        return currentValue;
    }
}
