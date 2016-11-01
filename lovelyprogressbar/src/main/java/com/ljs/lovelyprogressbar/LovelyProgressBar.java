package com.ljs.lovelyprogressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by anderson9 on 2016-10-27.
 */

public class LovelyProgressBar extends View {
    Paint mLinePaint;
    Paint mProPaint;
    int center; //圆心
    int radius;//半径
    RectF oval;
    Rect bounds;
    private float mLineWidth = 8;
    private int mLineColor;
    private float mTextsize = 40;

    private enum State {
        IDLE, PROGRESS, SUCCESS, ERROR,
    } //标示状态

    private State mProgreeState;//进度状态记录
    private State mAnimState;//开始动画记录
    String proStr;

    public LovelyProgressBar(Context context) {
        this(context, null);
    }

    public LovelyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LovelyProgressBar(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LovelyProgressBar
                , def, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if(attr==R.styleable.LovelyProgressBar_lineWidth) {
                mLineWidth = a.getInteger(attr, 8);
            }else if(attr==R.styleable.LovelyProgressBar_lineColor){
                mLineColor = a.getColor(attr, Color.GREEN);
            }else if(attr== R.styleable.LovelyProgressBar_textSize){
                mTextsize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
            }
        }
        a.recycle();
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);
        mProPaint = new Paint();
        mProPaint.setTextAlign(Paint.Align.LEFT);
        mProPaint.setTextSize(mTextsize);
        mProPaint.setColor(mLineColor);
        proStr = "0%";
        mAnimState = State.IDLE;
        mProgreeState = State.IDLE;
    }

    int baseline;
    int height;
    int width;
    float mDownh2; //初始加载的下载图坐标
    float mDownh3;
    float mDownh1;
    float mDownw1;
    float mDownw3;
    int mProgress;
    float mRightw2 ;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mAnimState) {
            case IDLE:
                canvas.drawLine(width / 2, height, width / 2, mDownh2, mLinePaint);
                canvas.drawLine(width / 2, height, mDownw1, mDownh1, mLinePaint);
                canvas.drawLine(width / 2, height, mDownw3, mDownh3, mLinePaint);
                break;
            case PROGRESS:
                mLinePaint.setStrokeWidth(mLineWidth);
                canvas.drawArc(oval, 90, (float) (mProgress * 3.6), false, mLinePaint); // 根据进度画圆弧
                canvas.drawText(proStr, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mProPaint);
                if (mProgress >= 100) {
                    mProgreeState = State.SUCCESS;
                }
                break;
            case SUCCESS:
                canvas.drawArc(oval, 180, mUpprogres, false, mLinePaint);
                canvas.drawArc(oval, 180, -mDownprogres, false, mLinePaint);
                if (mSuccess > 0) {
                    canvas.drawLine(0, height / 2, mSuccesW, mSuccesH, mLinePaint);
                }
                if (mSuccess > 0.4) {
                    canvas.drawLine(mRightw2, height, mSuccesW2, mSuccesH2, mLinePaint);
                }
                break;
            case ERROR:
                canvas.drawLine(beginw, beginh, mErrorLHw, mErrorlHh, mLinePaint);
                canvas.drawLine(beginw, beginh, mErrorLLw, mErrorLLh, mLinePaint);
                canvas.drawLine(beginw, beginh, mErrorRHw, mErrorRHh, mLinePaint);
                canvas.drawLine(beginw, beginh, mErrorRLw, mErrorRLh, mLinePaint);
                break;

        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        mDownh2 = (height * 0.2f);
        mDownh1 = mDownh3 = height / 2;
        mDownw3 = width * 0.8f;
        mDownw1 = width * 0.2f;
        mRightw2=(float)( width*0.38);
        center = getWidth() / 2; // 获取圆心的x坐标
        radius = center - (int) mLineWidth / 2;// 半径
        oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        bounds = new Rect();
        mProPaint.getTextBounds(proStr, 0, proStr.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mProPaint.getFontMetricsInt();
        baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
    }


    public void AnimRun() {
        ValueAnimator anim = ValueAnimator.ofFloat(0.2f, 1.0f);
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                mDownh2 = height * currentValue;
                if (currentValue >= 0.5f) {
                    mDownh3 = mDownh1 = height * currentValue;
                }
                if (currentValue >= 0.2f && currentValue <= 0.5f) {
                    mDownw1 = width * currentValue;
                    mDownw3 = width * (1 - currentValue);
                }
                invalidate();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mProgreeState = State.PROGRESS;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimState = State.PROGRESS;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    public void startload() {
        mDownh2 = (height * 0.2f);
        mDownh1 = mDownh3 = height / 2;
        mDownw3 = width * 0.8f;
        mDownw1 = width * 0.2f;
        mProgress = 0;
        mProgreeState = State.IDLE;
        mAnimState = State.IDLE;
    }

    public void setProgress(int value) {
        if (mProgreeState == State.IDLE) {
            AnimRun();
        }
        if (value < 1 || value > 100)
            return;
        mProgress = value;
        if (value == 100) {
            succesLoad();
        }
        proStr = value + "%";
        postInvalidate();

    }

    float mUpprogres;
    float mDownprogres;
    int mSuccesW;
    int mSuccesW2;
    int mSuccesH;
    int mSuccesH2;
    float mSuccess;

    public void succesLoad() {
        mSuccesW = 0;
        mSuccesW2 = 0;
        mSuccesH = height / 2;
        mSuccesH2 = 0;
        mAnimState = State.SUCCESS;
        mUpprogres = 270;
        mDownprogres = 90;
        mSuccess = 0;
        animSucces();
    }

    float mErrorRHw;
    float mErrorRHh;
    float mErrorRLw;
    float mErrorRLh;
    float mErrorLHw;
    float mErrorlHh;
    float mErrorLLw;
    float mErrorLLh;
    float beginw;
    float beginh;

    public void errorLoad() {
        beginw = width / 2;
        beginh = height / 2;
        mErrorRHw = beginw;
        mErrorRHh = beginh;
        mErrorRLw = beginw;
        mErrorRLh = beginh;
        mErrorLHw = beginw;
        mErrorlHh = beginh;
        mErrorLLw = beginw;
        mErrorLLh = beginh;
        mAnimState = State.ERROR;
        animError();
    }

    /**
     * 错误符号动画
     */
    private void animError() {
        ValueAnimator animErr = ValueAnimator.ofFloat(0, (float) (width * 0.3));
        animErr.setDuration(1000);
        animErr.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                mErrorRHw = beginw + currentValue;
                mErrorRHh = beginh + currentValue;
                mErrorRLw = beginw + currentValue;
                mErrorRLh = beginh - currentValue;
                mErrorLHw = beginw - currentValue;
                mErrorlHh = beginh + currentValue;
                mErrorLLw = beginw - currentValue;
                mErrorLLh = beginh - currentValue;
                invalidate();
            }
        });
        animErr.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mOnLoadListener!=null) {
                    mOnLoadListener.onAnimError();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animErr.start();
    }


    /**
     * 正确符号动画
     */
    private void animSucces() {
        ValueAnimator animRight = ValueAnimator.ofFloat(0, 1);
        animRight.setDuration(1000);
        animRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = mSuccess = (float) animation.getAnimatedValue();
                if (currentValue > 0 && currentValue <= 0.4f) {
                    mSuccesW = (int) (width * currentValue);
                    mSuccesH = (int) (height * (0.5 + (currentValue*1.25)));

                }
                if( currentValue > 0.4f) {
                    mSuccesH2 = (int) (height * ((-1.6* currentValue) + 1.6));
                    mSuccesW2 = (int) (width * currentValue);
                }

                invalidate();
            }
        });
        animRight.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(mOnLoadListener!=null) {
                    mOnLoadListener.onAnimSuccess();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ValueAnimator animcircle = ValueAnimator.ofFloat(270, 0);
        animcircle.setDuration(700);
        animcircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                mUpprogres = currentValue;
                if (currentValue <= 90) {
                    mDownprogres = currentValue;
                }
                invalidate();
            }
        });

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animcircle).before(animRight);
        animSet.start();
    }

    public interface OnLoadListener {

        void onAnimSuccess();

        void onAnimError();

    }
    OnLoadListener mOnLoadListener;
    public void setOnLoadListener(OnLoadListener listener) {
        mOnLoadListener = listener;
    }
}


