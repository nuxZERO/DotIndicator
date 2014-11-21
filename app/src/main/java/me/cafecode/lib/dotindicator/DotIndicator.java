package me.cafecode.lib.dotindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by Natthawut Hemathulin on 11/20/14 AD.
 * Email: natthawut1991@gmail.com
 */
public class DotIndicator extends View {

    /**
     * This class work with custom attributes(attrs.xml).
     */
    private Bitmap mActiveDotBitmap;
    private Bitmap mNormalDotBitmap;

    private Context mContext;
    private Paint mPaint;

    // Attributes
    private int mSize;
    private int mSpace;
    private int mRadius;
    private Drawable mNormalDotDrawable;
    private Drawable mActivedDotDrawable;

    private int mActivedPosition;

    public DotIndicator(Context context) {
        super(context);
        mContext = context;
        initUI();
    }

    /**
     *  Important. It minimum to provide a constructor.
     *
     * @param context
     * @param attrs
     */
    public DotIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DotIndicator, 0, 0);

        try {
            mSize = typedArray.getInteger(R.styleable.DotIndicator_size, 0);
            mSpace = typedArray.getDimensionPixelSize(R.styleable.DotIndicator_space, 0);
            mRadius = typedArray.getDimensionPixelSize(R.styleable.DotIndicator_radius, 0);
            mNormalDotDrawable = typedArray.getDrawable(R.styleable.DotIndicator_src_normal);
            mActivedDotDrawable = typedArray.getDrawable(R.styleable.DotIndicator_src_actived);
        } finally {
            typedArray.recycle();
        }
        mContext = context;
        initUI();
    }

    public DotIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initUI();
    }

    private void initUI() {

        mActivedPosition = 0;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mNormalDotDrawable == null) {
            mNormalDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.dot_normal));
        } else {
            mNormalDotBitmap = drawableToBitmap(mNormalDotDrawable);
        }

        if (mActivedDotDrawable == null) {
            mActiveDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.dot_actived));
        } else {
            mActiveDotBitmap = drawableToBitmap(mActivedDotDrawable);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDot(canvas);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getViewWidth();
        width = resolveSize(width, widthMeasureSpec);

        int height = mNormalDotBitmap.getHeight();
        height = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private void drawDot(Canvas canvas) {
        int axisX = 0;
        for (int i = 0; i < mSize; i++) {
            if (i == mActivedPosition) {
                canvas.drawBitmap(mActiveDotBitmap, axisX, 0, mPaint);
            } else {
                canvas.drawBitmap(mNormalDotBitmap, axisX, 0, mPaint);
            }
            axisX += mNormalDotBitmap.getWidth()+mSpace;
        }
    }

    public int getViewWidth() {
        if (mSpace == 0) {
            return mSize *mNormalDotBitmap.getWidth();
        }
        return mSize *(mNormalDotBitmap.getWidth()+mSpace)-mSpace;
    }

    public int getTotalDots() {
        return mSize;
    }

    public void setTotalDots(int mTotalDots) {
        this.mSize = mTotalDots;
    }

    public int getActivedPosition() {
        return mActivedPosition;
    }

    public void setActivedPosition(int activedPosition) {
        mActivedPosition = activedPosition;
        invalidate();
    }

    public void setPager(ViewPager viewPager) {
        mSize = viewPager.getAdapter().getCount();

        invalidate();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                setActivedPosition(i);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        if (mRadius == 0) {
            // Default dot radius size
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        } else {
            // Specify dot radius size
            bitmap = Bitmap.createBitmap(mRadius *2, mRadius *2, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
