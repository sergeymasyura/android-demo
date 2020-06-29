package com.masyura.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.masyura.demo.R;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * TextView which toggles maxLines on click.
 */
public class ExpandableTextView extends AppCompatTextView {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = ExpandableTextView.class.getSimpleName();

    public static final int MAX_LINES_COLLAPSED_DEFAULT = 3;
    public static final int MAX_LINES_EXPANDED_DEFAULT = 30;

    private int maxLinesCollapsed;
    private int maxLinesExpanded;

    private boolean isExpanded = false;

    public ExpandableTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyle, 0);

        maxLinesCollapsed = a.getInt(R.styleable.ExpandableTextView_maxLinesCollapsed, MAX_LINES_COLLAPSED_DEFAULT);
        maxLinesExpanded = a.getInt(R.styleable.ExpandableTextView_maxLinesExpanded, MAX_LINES_EXPANDED_DEFAULT);

        a.recycle();

        setMaxLines(maxLinesCollapsed);

        setOnClickListener(v -> {
            int maxLines = isExpanded ? maxLinesCollapsed : maxLinesExpanded;
            setMaxLines(maxLines);
            isExpanded = !isExpanded;
        });
    }

    public int getMaxLinesCollapsed() {
        return maxLinesCollapsed;
    }

    public void setMaxLinesCollapsed(int maxLinesCollapsed) {
        this.maxLinesCollapsed = maxLinesCollapsed;
    }

    public int getMaxLinesExpanded() {
        return maxLinesExpanded;
    }

    public void setMaxLinesExpanded(int maxLinesExpanded) {
        this.maxLinesExpanded = maxLinesExpanded;
    }
}
