package moe.gkd.smsassistant.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import moe.gkd.smsassistant.R;

public class SquareCardView extends CardView {
    private float scale;
    private boolean isFixedWidth;

    public SquareCardView(@NonNull Context context) {
        this(context, null);
    }

    public SquareCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SquareCardView);
        scale = ta.getFloat(R.styleable.SquareCardView_scale, 0);
        isFixedWidth = ta.getBoolean(R.styleable.SquareCardView_isFixedWidth, true);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isFixedWidth) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (scale != 0) {
                float height = width * scale;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
            }
        } else {
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (scale != 0) {
                float width = height / scale;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
