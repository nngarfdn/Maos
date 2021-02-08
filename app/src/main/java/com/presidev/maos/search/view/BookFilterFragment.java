package com.presidev.maos.search.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.presidev.maos.R;
import com.presidev.maos.search.model.BookFilter;

import static com.presidev.maos.utils.Constants.EXTRA_BOOK_FILTER;

public class BookFilterFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private BookFilter filter;
    private BookFilterListener listener;

    private CheckBox cbOnlyAvailable;

    public BookFilterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cbOnlyAvailable = view.findViewById(R.id.cb_on_available_bf);

        Button btnApply = view.findViewById(R.id.btn_apply_bf);
        btnApply.setOnClickListener(v -> {
            filter.setOnlyAvailable(cbOnlyAvailable.isChecked());

            listener.receiveData(filter);
            dismiss();
        });

        Bundle bundle = getArguments();
        if (bundle.containsKey(EXTRA_BOOK_FILTER)){
            filter = bundle.getParcelable(EXTRA_BOOK_FILTER);
            cbOnlyAvailable.setChecked(filter.isOnlyAvailable());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_apply_bf:
                filter.setOnlyAvailable(cbOnlyAvailable.isChecked());
                listener.receiveData(filter);
                dismiss();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BookFilterFragment.BookFilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement " + BookFilterFragment.BookFilterListener.class.getSimpleName());
        }
    }

    public interface BookFilterListener{
        void receiveData(BookFilter filter);
    }
}