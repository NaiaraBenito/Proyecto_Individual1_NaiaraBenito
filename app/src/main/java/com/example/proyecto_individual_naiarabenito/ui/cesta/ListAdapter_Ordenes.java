
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyecto_individual_naiarabenito.R;
import com.example.proyecto_individual_naiarabenito.db.DBHelper;
import java.util.List;

/* ################################### CLASE LIST_ADAPTER_ORDENES ##################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la lista de pedidos.

    *) Tipo: RecyclerView Adapter
*/
class ListAdapter_Ordenes extends RecyclerView.Adapter<ListAdapter_Ordenes.ViewHolder>{

// ___________________________________________ Variables ___________________________________________
    private List<Orden> lista_orden;    // Lista con los productos de la cesta
    private final LayoutInflater inflater;    // Describir de que archivo proviene la lista
    private final Context context;            // Contexto de CestaFragment
    private final String[] datosUser;  // Lista que contiene los datos del usuario para mantener la sesión

    private InterfazActualizarCesta listenerActualizado; // Listener para utilizar el método notificarCambios()


// __________________________________________ Constructor __________________________________________
    public ListAdapter_Ordenes(List<Orden> pList_ele, Context pContext, String[] pDatosUser, InterfazActualizarCesta listenerActualizado){
        this.inflater = LayoutInflater.from(pContext);
        this.context = pContext;
        this.lista_orden = pList_ele;
        this.datosUser = pDatosUser;
        this.listenerActualizado = listenerActualizado;
    }

// _______________________________________ Getter y Setter _________________________________________
    public List<Orden> getListaOrden(){
        return lista_orden;
    }

// _________________________________________________________________________________________________
    public void setListaOrden(List<Orden> lista){
        lista_orden=lista;
        notifyDataSetChanged();
    }

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreateViewHolder:
    --------------------------
        *) Parámetros (Input):
                1) (ViewGroup) parent: Contiene la vista principal a la que se debe adjuntar la
                   vista del RecyclerView.
                2) (int) viewType: Contiene el índice del elemento de la lista.
        *) Parámetro (Output):
                (ViewHolder) viewholder: Vista con el producto de la posición viewType.
        *) Descripción:
                Este método se ejecuta al crear el RecyclerView con los pedidos del usuario.
*/
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // Obtener la vista
        View view = inflater.inflate(R.layout.orden_cardview,parent,false);

        // Cargar las preferencias configuradas por el usuario
        cargar_configuracion(view);
        return new ViewHolder(view);
    }

// _________________________________________________________________________________________________

/*  Método onBindViewHolder:
    ------------------------
        *) Parámetros (Input):
                1) (ViewHolder) parent: Contiene la vista de un elemento de la lista.
                2) (int) position: Contiene el índice del elemento de la lista.
        *) Parámetro (Output):
                (ViewHolder) viewholder: Vista con el producto de la posición viewType.
        *) Descripción:
                Este método se ejecuta al crear el RecyclerView con los pedidos del usuario para
                cada elemento. Transfiere la información de la lista de órdenes a la vista.
*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

        // Cargar los datos de la lista a la vista
        holder.bindData(lista_orden.get(position));

        // Asignar acción al botón "-"
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // Si se ha pulsado
                // Consigue el valor actual de la cantidad del producto
                int cantidad = Integer.parseInt(holder.cantidad.getText().toString());

                // Comprobar que la cantidad sea superior a cero
                if(cantidad > 0){       // Si todavía quedan elementos

                    // Obtener el nombre del producto
                    String producto = holder.nombre.getText().toString();

                    // Actualizar cantidad (Disminuir)
                    int nuevaCantidad = cantidad - 1;

                    // Comprobar si se ha agotado la cantidad del producto
                    if (nuevaCantidad <= 0) { // Si se ha agotado: Eliminar producto de la cesta

                        // Enseñar un diálogo para confirmar la decisión
                        listenerActualizado.notificarBorrado(position);
                    }
                    else{   // Si no se ha agotado: Actualizar cantidad del producto de la cesta

                        // Actualizar lista
                        lista_orden.get(position).setCantidadProd(nuevaCantidad);

                        // Actualizar la BBDD
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.actualizarOrden(producto, -1, datosUser[2]);
                    }

                    // Notificar al RecyclerView que la lista ha sido modificada
                    notifyDataSetChanged();

                    // Notificar a CestaFragment que la lista ha sido modificada
                    listenerActualizado.notificarCambios();
                }
            }
        });

        // Asignar acción al botón "+"
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // Si se ha pulsado

                // Consigue el nombre del producto
                String producto = holder.nombre.getText().toString();

                // Actualizar cantidad del producto (Aumentar)
                int cantidad = Integer.parseInt(holder.cantidad.getText().toString()) + 1;
                lista_orden.get(position).setCantidadProd(cantidad);

                // Notificar al RecyclerView que la lista ha sido modificada
                notifyDataSetChanged();

                // Notificar a CestaFragment que la lista ha sido modificada
                listenerActualizado.notificarCambios();

                // Actualizar la BBDD
                DBHelper dbHelper = new DBHelper(context);
                dbHelper.actualizarOrden(producto, 1, datosUser[2]);
            }
        });
    }

// _________________________________________________________________________________________________

/*  Método getItemCount:
    --------------------
        *) Parámetros (Input):
        *) Parámetro (Output):
                (int) lista: Lista con los productos de la cesta.
        *) Descripción:
                Getter para obtener la lista de productos añadidos a la cesta.
*/
    @Override
    public int getItemCount(){
        return lista_orden.size();
    }

/* ######################################## CLASE VIEWHOLDER #######################################
    *) Descripción:
        La función de esta clase es modificar la vista del RecyclerView.

    *) Tipo: RecyclerView ViewHolder
*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img;          // ImageView con la foto del producto de la cesta
        private final TextView nombre;        // TextView con el nombre del producto de la cesta
        private final Button minus;           // Button para reducir la cantidad del producto
        private final Button plus;            // Button para aumentar la cantidad del producto
        private final TextView cantidad;      // TextView con la cantidad del producto de la cesta
        private final TextView precioUnidad;  // TextView con el precio por unidad del producto de la cesta
        private final TextView precioTotal;   // TextView con el precio total del producto de la cesta

        ViewHolder(View itemView){
            super(itemView);

            // Obtener los objetos de la vista
            img = itemView.findViewById(R.id.orden_img);
            nombre = itemView.findViewById(R.id.orden_nombre);
            cantidad = itemView.findViewById(R.id.orden_cantidad);
            precioUnidad = itemView.findViewById(R.id.orden_precio_uniProd);
            precioTotal = itemView.findViewById(R.id.orden_precio_totProd);
            minus = itemView.findViewById(R.id.orden_minus);
            plus = itemView.findViewById(R.id.orden_plus);
        }

// ____________________________________________ Métodos ____________________________________________

/*  Método bindData:
    ----------------
        *) Parámetros (Input):
                (Orden) item: Contiene la Orden de un elemento de la cesta.
        *) Parámetro (Output):
                void
        *) Descripción:
                Cargar la lista de pedidos en la vista.
*/
        void bindData(final Orden item){
            img.setImageResource(item.getImagenProd());
            nombre.setText(item.getNombreProd());
            cantidad.setText(String.valueOf(item.getCantidadProd()));
            precioUnidad.setText(String.valueOf(item.getPrecioProd()));

            // Calcular precio total del producto
            double tot = Math.round(item.getCantidadProd() * item.getPrecioProd() * 100.0) / 100.0;
            precioTotal.setText(String.valueOf(tot));
        }
    }

// _________________________________________________________________________________________________

/*  Método cargar_configuracion:
    ----------------------------
        *) Parámetros (Input):
                (View) view: Vista asociada al fragmento.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga las preferencias configuradas por el usuario (modo oscuro,
                orientación de la pantalla...).
*/
    private void cargar_configuracion(View view){

        // Obtener las preferencias configuradas por el usuario
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        // Comprobar el estado de la preferencia del modo oscuro
        boolean modoOscuro = sp.getBoolean("modo_oscuro", false);

        if(modoOscuro){     // Si el modo oscuro está activado: Pintar el fondo de gris
            view.setBackgroundColor(context.getResources().getColor(R.color.black));
        } else{             // Si el modo oscuro está desactivado: Pintar el fondo de blanco
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

// _________________________________________________________________________________________________

/*  Método eliminar:
    ----------------
        *) Parámetros (Input):
                (int) position: Índice del producto que se desea eliminar.
        *) Parámetro (Output):
                void
        *) Descripción:
                Este método carga las preferencias configuradas por el usuario (modo oscuro,
                orientación de la pantalla...).
*/
    public void eliminar(int position){
        String producto = lista_orden.get(position).getNombreProd();
        // Eliminar producto de la lista
        lista_orden.remove(position);

        // Eliminar producto de la BBDD
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.borrarOrden(producto, datosUser[2]);

        // Notificar al RecyclerView que la lista ha sido modificada
        notifyDataSetChanged();

        // Notificar a CestaFragment que la lista ha sido modificada
        listenerActualizado.notificarCambios();

    }
}