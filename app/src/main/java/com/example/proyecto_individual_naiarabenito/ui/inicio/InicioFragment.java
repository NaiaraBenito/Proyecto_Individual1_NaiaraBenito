package com.example.proyecto_individual_naiarabenito.ui.inicio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_individual_naiarabenito.R;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment implements SearchView.OnQueryTextListener{

    // Variables para mostrar la lista de categorias
    ListAdapter_Categorias adapterCategorias;
    RecyclerView recyclerViewCategorias;
    List<Categoria> lista_cat;

    //private FragmentInicioBinding binding;

    // Variables para mostrar la lista de productos
    List<Producto> lista_prod;
    RecyclerView recyclerViewProductos;
    ListAdapter_Productos adapterProductos;

    SearchView txtBuscar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //InicioViewModel inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        //binding = FragmentInicioBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //inicioViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

       // Obtener la vista de fragment_inicio
        View view = inflater.inflate(R.layout.fragment_inicio,container,false);

        // Cargar la lista de Categorias
        recyclerViewCategorias = view.findViewById(R.id.lista_categorias);
        cargarListaCategorias();
        mostrarDatosCategorias();

        // Cargar la lista de Productos
        recyclerViewProductos = view.findViewById(R.id.lista_productos);
        cargarListaProductos();
        mostrarDatosProductos();

        // Obtener el search View
        txtBuscar = (SearchView) view.findViewById(R.id.search_view);
        txtBuscar.setOnQueryTextListener(this);

        //return root;
        return view;
    }


    private void cargarListaCategorias() {
        lista_cat = new ArrayList<>();
        lista_cat.add(new Categoria("Pizzas","#775447",R.drawable.cat_pizza));
        lista_cat.add(new Categoria("Hamburguesas","#776927",R.drawable.cat_hamburguesa));
        lista_cat.add(new Categoria("Kebab","#629078",R.drawable.cat_kebab));
        lista_cat.add(new Categoria("Burritos","#494771",R.drawable.cat_burrito));
        lista_cat.add(new Categoria("Sandwitches","#573654",R.drawable.cat_sandwitch));
        lista_cat.add(new Categoria("Bocadillos","#593862",R.drawable.cat_bocadillo));
        lista_cat.add(new Categoria("Postres","#689053",R.drawable.cat_postres));
    }

    private void mostrarDatosCategorias() {
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        adapterCategorias = new ListAdapter_Categorias(lista_cat,getContext());
        recyclerViewCategorias.setAdapter(adapterCategorias);
    }

    private void cargarListaProductos() {
        lista_prod = new ArrayList<>();
        lista_prod.add(new Producto("4 Quesos","4 Quesos",R.drawable.prod_pizza_4quesos));
        lista_prod.add(new Producto("Barbacoa","Barbacoa",R.drawable.prod_pizza_barbacoa));
        lista_prod.add(new Producto("Calzone","Calzone",R.drawable.prod_pizza_calzone));
        lista_prod.add(new Producto("Diavola","Diavola",R.drawable.prod_pizza_diavola));
        lista_prod.add(new Producto("Margarita","Margarita",R.drawable.prod_pizza_margarita));
    }

    private void mostrarDatosProductos() {
        recyclerViewProductos.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapterProductos = new ListAdapter_Productos(lista_prod,getContext());
        recyclerViewProductos.setAdapter(adapterProductos);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String nombre = getActivity().getIntent().getStringExtra("nombreUsuario");

        TextView nombreUser = (TextView) view.findViewById(R.id.tv_nombreUsuario_inicio);
        nombreUser.setText("Hola " + nombre);
    }

    // Métodos para buscar en tiempo real el texto que pongamos en el SearchView
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterProductos.filtrado(newText);
        return false;
    }
}