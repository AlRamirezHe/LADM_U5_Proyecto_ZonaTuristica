package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.Pantallas

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_pantalla_comida.view.*
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.ItemMenu
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.MainActivity
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.R
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.ToastPersonalizado


class PantallaComida(activity: MainActivity) : Fragment() {

    var datos = ""
    var activity = activity



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_pantalla_comida, container, false)
        //vista.txtPrueba.setText(datos)
        try {
            activity.negocio.listaComidas.forEach {
                mostrarComidas(vista, it)
            }
        }catch(er:Exception){ ToastPersonalizado(activity , er.message!! , true).show() }
        return vista
    }




    fun mostrarDatos( Datos: String ) {
        datos = Datos
    }


    fun mostrarComidas( vista: View , itemMenu: ItemMenu ) {
        // LINEAR LAYOUT PRINCIPAL
        val contenedorItem = LinearLayout(activity)
        vista.contenedorComidas.addView(contenedorItem)

        val p1 = contenedorItem.layoutParams as LinearLayout.LayoutParams
        p1.width = LinearLayout.LayoutParams.MATCH_PARENT
        p1.height = LinearLayout.LayoutParams.WRAP_CONTENT
        p1.bottomMargin = 20
        contenedorItem.layoutParams = p1
        contenedorItem.orientation = LinearLayout.VERTICAL
       // contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.white))


        //=================================================
        // FRAME LAYOUT IMAGEN-PRECIO
        //=================================================
        val contenedorImg = FrameLayout(activity)
        contenedorItem.addView(contenedorImg)
        val p2 = contenedorImg.layoutParams as LinearLayout.LayoutParams
        p2.width = LinearLayout.LayoutParams.MATCH_PARENT
        p2.height = 400
        contenedorImg.layoutParams = p2


        //=================================================
        // IMAGE VIEW COMIDA
        //=================================================
        val imagenComida = ImageView(activity)
        contenedorImg.addView(imagenComida)

        val p3 = imagenComida.layoutParams as FrameLayout.LayoutParams
        p3.width = FrameLayout.LayoutParams.MATCH_PARENT
        p3.height = FrameLayout.LayoutParams.MATCH_PARENT
        imagenComida.layoutParams = p3

        if( itemMenu.imagen == null ) {
            imagenComida.setImageResource( R.drawable.icono_comida )
            imagenComida.setBackgroundColor(ContextCompat.getColor( activity , R.color.rojoLigero))
            imagenComida.setColorFilter(
                ContextCompat.getColor( activity , R.color.rojoOscuro),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        else {
            imagenComida.setImageBitmap( itemMenu.imagen )
            imagenComida.scaleType = ImageView.ScaleType.CENTER_CROP
        }


        //=================================================
        // TEXT VIEW ( PRECIO )
        //=================================================
        val txtPrecio = TextView(activity)
        contenedorImg.addView(txtPrecio)

        val p4 = txtPrecio.layoutParams as FrameLayout.LayoutParams
        p4.width = FrameLayout.LayoutParams.WRAP_CONTENT
        p4.height = FrameLayout.LayoutParams.WRAP_CONTENT
        p4.gravity = Gravity.RIGHT
        txtPrecio.layoutParams = p4

        txtPrecio.setPadding(7, 4, 7, 4)
        txtPrecio.setText("$ ${itemMenu.precio}")
        txtPrecio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f)
        txtPrecio.setTypeface(Typeface.DEFAULT_BOLD)
        txtPrecio.setBackgroundColor( ContextCompat.getColor( activity , R.color.rojoLigero) )
        txtPrecio.setTextColor(ContextCompat.getColor(activity, R.color.rojoOscuro) )



        //=================================================
        // LINEAR LAYOUT TITULO-INFO
        //=================================================
        val contenedorTitulo = LinearLayout(activity)
        contenedorItem.addView(contenedorTitulo)
        val p5 = contenedorTitulo.layoutParams as LinearLayout.LayoutParams
        p5.width = LinearLayout.LayoutParams.MATCH_PARENT
        p5.height = LinearLayout.LayoutParams.WRAP_CONTENT
        contenedorTitulo.layoutParams = p5
        contenedorTitulo.orientation = LinearLayout.HORIZONTAL
        contenedorTitulo.setPadding(5 , 12 , 5 , 12)


        //=================================================
        // TEXT VIEW ( TITULO )
        //=================================================
        val txtTitulo = TextView(activity)
        contenedorTitulo.addView(txtTitulo)

        val p7 = txtTitulo.layoutParams as LinearLayout.LayoutParams
        p7.width = 1
        p7.height = LinearLayout.LayoutParams.WRAP_CONTENT
        p7.weight  = 2f
        p7.gravity = Gravity.CENTER_VERTICAL
        txtTitulo.layoutParams = p7

        txtTitulo.setText(itemMenu.nombre)
        txtTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21f)
        txtTitulo.setTypeface(Typeface.DEFAULT_BOLD)
        txtTitulo.textAlignment = View.TEXT_ALIGNMENT_CENTER
        txtTitulo.setTextColor(ContextCompat.getColor(activity, R.color.rojoOscuro))


        //=================================================
        // IMAGE VIEW MAS-INFORMACION
        //=================================================
        val iconoMasInfo = ImageView(activity)
        contenedorTitulo.addView(iconoMasInfo)
        val p6 = iconoMasInfo.layoutParams as LinearLayout.LayoutParams
        p6.width = 80
        p6.height = 80
        p6.gravity = Gravity.CENTER_VERTICAL
        iconoMasInfo.layoutParams = p6
        iconoMasInfo.setImageResource(R.drawable.icono_ayuda)
        iconoMasInfo.setColorFilter(
            ContextCompat.getColor(activity, R.color.rojoLigero2),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        iconoMasInfo.setPadding(10 , 0 , 10 , 0)


        //=================================================
        // LINEAR LAYOUT DESCRIPCION
        //=================================================
        val contenedorDescripcion = LinearLayout(activity)
        contenedorItem.addView(contenedorDescripcion)
        val p11 = contenedorDescripcion.layoutParams as LinearLayout.LayoutParams
        p11.width = LinearLayout.LayoutParams.MATCH_PARENT
        p11.height = 0
        contenedorDescripcion.layoutParams = p11
        contenedorDescripcion.orientation = LinearLayout.HORIZONTAL
        contenedorDescripcion.setPadding(10 , 0 , 10 , 0)


        //=================================================
        // TEXT VIEW ( DESCRIPCION )
        //=================================================
        val txtDescripcion = TextView(activity)
        contenedorDescripcion.addView(txtDescripcion)

        val p9 = txtDescripcion.layoutParams as LinearLayout.LayoutParams
        p9.width = 1
        p9.height = LinearLayout.LayoutParams.WRAP_CONTENT
        p9.weight  = 2f
        txtDescripcion.layoutParams = p9
        txtDescripcion.setText(itemMenu.descripcion)
        txtDescripcion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        txtDescripcion.setTypeface(Typeface.DEFAULT_BOLD)
        txtDescripcion.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        txtDescripcion.setTextColor(ContextCompat.getColor(activity, R.color.negroLigero))


        //=================================================
        // OCULTAR/MOSTRAR DESCRIPCION
        //=================================================
        var contenidoOculto = true
        iconoMasInfo.setOnClickListener {
            if( contenidoOculto ) {
                val p10 = contenedorDescripcion.layoutParams as LinearLayout.LayoutParams
                p10.height = LinearLayout.LayoutParams.WRAP_CONTENT
                contenedorDescripcion.layoutParams = p10

                iconoMasInfo.setColorFilter(
                    ContextCompat.getColor(activity, R.color.rojoOscuro),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                contenidoOculto = false
            }
            else {
                val p10 = contenedorDescripcion.layoutParams as LinearLayout.LayoutParams
                p10.height = 0
                contenedorDescripcion.layoutParams = p10

                iconoMasInfo.setColorFilter(
                    ContextCompat.getColor(activity, R.color.rojoLigero2),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                contenidoOculto = true
            }
        }
    }

}