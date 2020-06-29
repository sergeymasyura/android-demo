package com.masyura.demo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.masyura.demo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;

public class AboutDialog extends DialogFragment {

    public static final String TAG = AboutDialog.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_about, null);

        TextView textViewDescription = view.findViewById(R.id.dialog_about_description);
        TextView textViewContact = view.findViewById(R.id.dialog_about_contact);

        setTextFromHtml(textViewDescription, R.string.about_description);
        setTextFromHtml(textViewContact, R.string.about_contact);

        builder.setTitle(R.string.about);
        builder.setPositiveButton(android.R.string.ok, null);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setView(view);

        return dialog;
    }

    private void setTextFromHtml(TextView textView, @StringRes int resId) {
        Spanned spanned = HtmlCompat.fromHtml(getString(resId), HtmlCompat.FROM_HTML_MODE_LEGACY);
        textView.setText(spanned);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
