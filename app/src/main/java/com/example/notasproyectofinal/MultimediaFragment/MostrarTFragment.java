package com.example.notasproyectofinal.MultimediaFragment;

import androidx.lifecycle.ViewModelProviders;

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.notasproyectofinal.DAOS.DAONota;
import com.example.notasproyectofinal.DAOS.DAOTareas;
import com.example.notasproyectofinal.Nota;
import com.example.notasproyectofinal.R;
import com.example.notasproyectofinal.Tarea;

import java.util.ArrayList;

public class MostrarTFragment extends Fragment {

    private MostrarTViewModel mViewModel;

    public static MostrarTFragment newInstance() {
        return new MostrarTFragment();
    }

    private ListView lv;
    private DAOTareas dao;
    private ArrayList<Tarea> tareas;
    private ArrayAdapter<Tarea> adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mostrar_t_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MostrarTViewModel.class);
        // TODO: Use the ViewModel
        llenar();
        registerForContextMenu(lv);
    }

    public void llenar(){
        DAOTareas dao = new DAOTareas(getContext());
        String[] Tareas = {""};
        lv = getView().findViewById(R.id.lv_mostrartarea);
        tareas = dao.agregar(Tareas);
        adapter= new ArrayAdapter<Tarea>(getActivity(),android.R.layout.simple_list_item_1,tareas);

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
        dao = new DAOTareas(getContext());
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        lv = getView().findViewById(R.id.lv_mostrartarea);
        Tarea tarea = (Tarea) lv.getItemAtPosition(info.position);
        switch (item.getItemId()){
            case R.id.btn_eliminar:
                dao.eliminar(tarea.getId());
                llenar();
                return true;
            case R.id.btn_act:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
