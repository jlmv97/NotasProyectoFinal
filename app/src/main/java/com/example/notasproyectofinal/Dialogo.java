package com.example.notasproyectofinal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialogo extends AppCompatDialogFragment {

    private EditText txtDescripcion;
    private ExampleDilaogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialogo_descripcion, null);

        builder.setView(view)
                .setTitle(getString(R.string.dialog_description))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // poner un texto como "sin descripcion, o null
                    }
                })
                .setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String descripcion1 = txtDescripcion.getText().toString();
                        if (descripcion1.isEmpty()){
                            descripcion1="Sin Descripcion";
                        }

                        listener.applyTexts(descripcion1);
                    }
                });
        txtDescripcion = view.findViewById(R.id.descripcion);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDilaogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Debe implementar ExampleDialogListener");
        }
    }

    public interface ExampleDilaogListener{
        void applyTexts(String descripcion);
    }
}
