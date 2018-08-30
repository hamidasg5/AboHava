package com.abohava.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.abohava.R;
import com.abohava.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlotterView extends View {

    public enum DataType {
        HISTORY, CURRENT, FORECAST
    }

    private DataType mDataType;
    private int mDrawingColor;
    private float mPointRadius;

    private Paint mThinPaint;
    private Paint mThickPaint;
    private Paint mTextPaint;

    private String mUnits;
    private List<PointF> mPoints;

    public PlotterView(Context context) {
        this(context, null);
    }

    public PlotterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlotterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlotterView, 0, 0);

            mDataType = DataType.values()[a.getInteger(R.styleable.PlotterView_dataType, 0)];
            mDrawingColor = a.getColor(R.styleable.PlotterView_drawingColor, 0);

            a.recycle();
        }

        float density = context.getResources().getDisplayMetrics().density;

        mPointRadius = density * 3;
        mThinPaint = new Paint();
        mThickPaint = new Paint();
        mTextPaint = new Paint();
        mThinPaint.setStrokeWidth(1 * density);
        mThickPaint.setStrokeWidth(2 * density);
        mTextPaint.setTextSize(8 * density);
        mThinPaint.setColor(mDrawingColor);
        mThickPaint.setColor(mDrawingColor);
        mTextPaint.setColor(mDrawingColor);

        mPoints = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        int pointsCount = mPoints.size();
        if (pointsCount > 1) {
            float startX = (float) (width * 0.1);
            float startY = (float) (height * 0.1);
            float stopX = (float) (width * 0.9);
            float stopY = (float) (height * 0.9);
            float plotWidth = stopX - startX;
            float plotHeight = stopY - startY;

            float xMin = 1000;
            float yMin = 1000;
            float xMax = -1000;
            float yMax = -1000;
            for (PointF point : mPoints) {
                if (point.x < xMin) xMin = point.x;
                if (point.x > xMax) xMax = point.x;
                if (point.y < yMin) yMin = point.y;
                if (point.y > yMax) yMax = point.y;
            }
            float xDomain = xMax - xMin;
            float yDomain = yMax - yMin;

            for (int i = 0; i < pointsCount - 1; i++) {
                PointF current = mPoints.get(i);
                PointF next = mPoints.get(i + 1);
                canvas.drawCircle(
                        ((current.x - xMin) / xDomain) * plotWidth + startX,
                        height - ((current.y - yMin) / yDomain) * plotHeight - startY,
                        mPointRadius,
                        mThickPaint);
                canvas.drawLine(
                        ((current.x - xMin) / xDomain) * plotWidth + startX,
                        height - ((current.y - yMin) / yDomain) * plotHeight - startY,
                        ((next.x - xMin) / xDomain) * plotWidth + startX,
                        height - ((next.y - yMin) / yDomain) * plotHeight - startY,
                        mThickPaint);
            }
            canvas.drawCircle(
                    ((mPoints.get(pointsCount - 1).x - xMin) / xDomain) * plotWidth + startX,
                    height - ((mPoints.get(pointsCount - 1).y - yMin) / yDomain) * plotHeight - startY,
                    mPointRadius,
                    mThickPaint);

            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            float xBaseLine = width / 2;
            float yBaseLine = (stopY + height) / 2;

            // x axis
            canvas.drawLine(startX, stopY, stopX, stopY, mThinPaint);

            switch (mDataType) {
                case HISTORY:
                    // y axis
                    canvas.drawLine(startX, startY, startX, stopY, mThinPaint);
                    // y values
                    canvas.drawText(StringUtils.getTempLabel((int)yMax, mUnits), startX/2, startY, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.75), mUnits), startX/2, startY+plotHeight*0.25f, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.5), mUnits), startX/2, startY+plotHeight*0.5f, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.25), mUnits), startX/2, startY+plotHeight*0.75f, mTextPaint);
                    // x values
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf(hour)+":00"), startX, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+6)%24)+":00"), startX+plotWidth*0.25f, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+12)%24)+":00"), startX+plotWidth*0.5f, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+18)%24)+":00"), startX+plotWidth*0.75f, yBaseLine, mTextPaint);
                    canvas.drawText("حالا", stopX, yBaseLine, mTextPaint);
                    break;
                case CURRENT:
                    // current line
                    canvas.drawLine(width / 2, 0, width / 2, stopY, mThinPaint);
                    // y values
                    canvas.drawText(StringUtils.getTempLabel((int)yMax, mUnits), xBaseLine, startY, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.75), mUnits), xBaseLine, startY+plotHeight*0.25f, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.5), mUnits), xBaseLine, startY+plotHeight*0.5f, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.25), mUnits), xBaseLine, startY+plotHeight*0.75f, mTextPaint);
                    // x values
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+12)%24)+":00"), startX, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+18)%24)+":00"), startX+plotWidth*0.25f, yBaseLine, mTextPaint);
                    canvas.drawText("حالا", width / 2 ,yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+6)%24)+":00"), startX+plotWidth*0.75f, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+12)%24)+":00"), stopX, yBaseLine, mTextPaint);
                    break;
                case FORECAST:
                    // y axis
                    canvas.drawLine(startX, startY, startX, stopY, mThinPaint);
                    // y values
                    canvas.drawText(StringUtils.getTempLabel((int)yMax, mUnits), startX/2, startY, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.75), mUnits), startX/2, startY+plotHeight*0.25f, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.5), mUnits), startX/2, startY+plotHeight*0.5f, mTextPaint);
                    canvas.drawText(StringUtils.getTempLabel((int)(yMin+yDomain*0.25), mUnits), startX/2, startY+plotHeight*0.75f, mTextPaint);
                    // x values
                    canvas.drawText("حالا", startX, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+6)%24)+":00"), startX+plotWidth*0.25f, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+12)%24)+":00"), startX+plotWidth*0.5f, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf((hour+18)%24)+":00"), startX+plotWidth*0.75f, yBaseLine, mTextPaint);
                    canvas.drawText(StringUtils.toPersianDigits(String.valueOf(hour)+":00"), stopX, yBaseLine, mTextPaint);
                    break;
            }
        }
    }

    public void setPoints(List<PointF> points, String units) {
        mUnits = units;
        mPoints = points;
        invalidate();
    }
}
