package apps.mobile.ostium;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class CardView extends View
{
    private Drawable mDrawable;
    private TextPaint mTextPaint;

    private String mString;
    private int mColor = Color.RED;

    private float mDimension = 0;
    private float mTextWidth;
    private float mTextHeight;

    public CardView(Context context)
    {
        super(context);
        init(null, 0);
    }

    public CardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CardView, defStyle, 0);

        mString = a.getString(R.styleable.CardView_exampleString);
        mColor = a.getColor(R.styleable.CardView_exampleColor, mColor);

        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mDimension = a.getDimension(R.styleable.CardView_exampleDimension, mDimension);

        if (a.hasValue(R.styleable.CardView_exampleDrawable))
        {
            mDrawable = a.getDrawable(R.styleable.CardView_exampleDrawable);
            mDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements()
    {
        mTextPaint.setTextSize(mDimension);
        mTextPaint.setColor(mColor);
        mTextWidth = mTextPaint.measureText(mString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mString,
                    paddingLeft + (contentWidth - mTextWidth) / 2,
                    paddingTop + (contentHeight + mTextHeight) / 2,
                       mTextPaint);

        // Draw the example drawable on top of the text.
        if (mDrawable != null)
        {
            mDrawable.setBounds(paddingLeft, paddingTop, paddingLeft + contentWidth, paddingTop + contentHeight);
            mDrawable.draw(canvas);
        }
    }

    public String getString()
    {
        return mString;
    }

    public void setString(String exampleString)
    {
        mString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int exampleColor)
    {
        mColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    public void setDimension(float exampleDimension)
    {
        mDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    public Drawable getDrawable()
    {
        return mDrawable;
    }

    public void setDrawable(Drawable exampleDrawable)
    {
        mDrawable = exampleDrawable;
    }
}