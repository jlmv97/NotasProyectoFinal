package com.example.notasproyectofinal.NotasFragment;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.notasproyectofinal.actualizar_notas;
import com.example.notasproyectofinal.DAOS.DAONota;
import com.example.notasproyectofinal.Nota;
import com.example.notasproyectofinal.R;

import java.util.ArrayList;

public class MostrarNFragment extends Fragment {

    private MostrarNViewModel mViewModel;

    public static MostrarNFragment newInstance() {
        return new MostrarNFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mostra_n_fragment, container, false);
    }
    private DAONota dao;
    private ListView lv;
    private ArrayList<Nota> notas;
    private ArrayAdapter<Nota> adapter;
    EditText criterio;
    Button buscar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MostrarNViewModel.class);
        // TODO: Use the ViewModel
        llenar();
        registerForContextMenu(lv);
        criterio = getActivity().findViewById(R.id.txt_buscadoN);
        buscar =getActivity().findViewById(R.id.btnBuscar);
        buscar.setOnClickListener(new View.OnClickListener() {//Busca por titulo de las notas
            @Override
            public void onClick(View v) {
                DAONota dao = new DAONota(getActivity());
                String[] nota = {criterio.getText().toString(),criterio.getText().toString()};
                lv = getView().findViewById(R.id.lv_mostrarnota);
                notas = dao.buscarporTitulo(nota);
                adapter= new ArrayAdapter<Nota>(getActivity(),android.R.layout.simple_list_item_1,notas);

                lv.setAdapter(adapter);

            }
        });

    }

    public void llenar (){
        DAONota dao = new DAONota(getActivity());
        String[] Notas = {"",""};
        lv = getView().findViewById(R.id.lv_mostrarnota);
        notas = dao.agregar(Notas);
        adapter= new ArrayAdapter<Nota>(getActivity(),android.R.layout.simple_list_item_1,notas);

        lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        llenar();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_context,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        dao = new DAONota(getActivity());
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        lv =  getActivity().findViewById(R.id.lv_mostrarnota);
        Nota nota = (Nota) lv.getItemAtPosition(info.position);
        switch (item.getItemId()){
            case R.id.btn_eliminar:
                dao.delete(nota.getId());
                llenar();
                return true;
            case R.id.btn_act:
                Intent intent = new Intent(getActivity(), actualizar_notas.class);
                intent.putExtra("nota",nota);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {//Se mantenga la info aun con cambios de panatalla
        super.onConfigurationChanged(newConfig);
        llenar();
    }
}