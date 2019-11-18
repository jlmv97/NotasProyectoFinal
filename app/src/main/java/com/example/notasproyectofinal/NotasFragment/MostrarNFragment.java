package com.example.notasproyectofinal.NotasFragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.notasproyectofinal.DAONota;
import com.example.notasproyectofinal.Nota;
import com.example.notasproyectofinal.R;

import java.util.ArrayList;

public class MostrarNFragment extends Fragment {

    private MostrarNViewModel mViewModel;

    public static MostrarNFragment newInstance() {
        return new MostrarNFragment();
    }

    ListView lv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mostra_n_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MostrarNViewModel.class);
        // TODO: Use the ViewModel
        DAONota dao = new DAONota(getContext());
        lv = getView().findViewById(R.id.lv_mostrarnota);
        //Llena el listview
        SimpleCursorAdapter adp =
                new SimpleCursorAdapter(
                        getContext(),
                        android.R.layout.simple_list_item_2,
                        dao.getAllCursor(),
                        new String[]{"_titulo","_fecha"},
                        new int[]{android.R.id.text1,android.R.id.text2},
                        SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE
                );
        lv.setAdapter(adp);

        //Aqui va el codigo para el fragmento oswi


    }

}