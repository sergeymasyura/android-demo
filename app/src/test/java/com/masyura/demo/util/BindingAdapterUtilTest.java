package com.masyura.demo.util;

import android.view.View;
import android.widget.TextView;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BindingAdapterUtilTest {
    @Test
    public void test_setVisibility_true() {
        View view = mock(View.class);

        BindingAdapterUtil.setVisibility(view, true);

        verify(view, times(1)).setVisibility(View.VISIBLE);
    }

    @Test
    public void test_setVisibility_false() {
        View view = mock(View.class);

        BindingAdapterUtil.setVisibility(view, false);

        verify(view, times(1)).setVisibility(View.GONE);
    }

    @Test
    public void test_formatRevision() {
        TextView textView = mock(TextView.class);

        BindingAdapterUtil.formatRevision(textView, "378fb7309f140941d571893c1ebd12ca7027ee9f");

        verify(textView, times(1)).setText("378fb7");
    }
}