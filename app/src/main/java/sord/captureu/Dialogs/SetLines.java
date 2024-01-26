package sord.captureu.Dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import sord.captureu.R;

@SuppressLint("ValidFragment")
public class SetLines extends DialogFragment {
    @Nullable
    private NumberPicker set_lines_number;
    private Context context;
    private Button set_lines_cancel,set_lines_set;
    private int num;

    @SuppressLint("ValidFragment")
    public SetLines(Context context, int num){
        this.context=context;
        this.num=num;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.set_lines_dialog,null);
        set_lines_number=view.findViewById(R.id.set_lines_number);
        set_lines_number.setMinValue(0);
        set_lines_number.setMaxValue(3);
        set_lines_number.setValue(num);
        set_lines_cancel=view.findViewById(R.id.set_lines_cancel);
        set_lines_set=view.findViewById(R.id.set_lines_set);
        set_lines_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();

        set_lines_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("lines",set_lines_number.getValue());
                editor.commit();
                EditDialogListener activity = (EditDialogListener) getActivity();
                activity.updateResult(set_lines_number.getValue());
                dismiss();
            }
        });

        return view;
    }
    public interface EditDialogListener {
        void updateResult(int num);
    }
}
