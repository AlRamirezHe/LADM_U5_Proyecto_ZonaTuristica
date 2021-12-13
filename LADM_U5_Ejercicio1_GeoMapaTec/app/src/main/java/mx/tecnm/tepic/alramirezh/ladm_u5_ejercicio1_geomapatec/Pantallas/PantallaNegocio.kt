package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.Pantallas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_pantalla_negocio.*
import kotlinx.android.synthetic.main.fragment_pantalla_negocio.view.*
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.MainActivity
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.Negocio
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.R
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.ToastPersonalizado
import java.io.ByteArrayOutputStream

class PantallaNegocio(p:MainActivity) : Fragment() {

    var datos = ""
    var pantalla =  p
    var listaCirculos = ArrayList<ImageView>()
    var contador = 0


    fun mostrarInfo(datosNeg:String){
        datos = datosNeg
    }

    fun convertirABytes( img: Bitmap ): ByteArray {
        var stream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG , 100 , stream)
        return stream.toByteArray()
    }

    fun convertirABitmap( img: ByteArray ): Bitmap {
        return BitmapFactory.decodeByteArray( img , 0 , img.size )
    }

    fun SetImagenes(vista:View) {
        contador = 0
        vista.imgNegocio.setImageBitmap( pantalla.negocio.imagenes.get(contador) )
        vista.imgNegocio.setOnClickListener {
            contador++
            if( contador == pantalla.negocio.imagenes.size ) contador = 0
            vista.imgNegocio.setImageBitmap( pantalla.negocio.imagenes.get(contador) )
            listaCirculos.forEach {
                it.setColorFilter(
                    ContextCompat.getColor(pantalla, R.color.rojoLigero2),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            listaCirculos.get(contador).setColorFilter(
                ContextCompat.getColor(pantalla, R.color.rojoOscuro),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        (0 .. pantalla.negocio.imagenes.size-1 ).forEach{
            val imagenCirculo = ImageView(pantalla)
            vista.contenedorCirculos.addView(imagenCirculo)
            val p3 = imagenCirculo.layoutParams as LinearLayout.LayoutParams
            p3.width = 21
            p3.height = 21
            p3.rightMargin = 5
            imagenCirculo.layoutParams = p3
            imagenCirculo.setImageResource(R.drawable.icono_circulo)
            imagenCirculo.setColorFilter(
                ContextCompat.getColor(pantalla, R.color.rojoLigero2),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            listaCirculos.add(imagenCirculo)
        }
        listaCirculos.get(contador).setColorFilter(
            ContextCompat.getColor(pantalla, R.color.rojoOscuro),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_pantalla_negocio, container, false)
     //   vista.pruebaNegocio.setText(datos)
        if(pantalla.negocio.ID!="") {
            vista.txtHorario.setText(pantalla.negocio.horario)
            vista.txtTelefono.setText(pantalla.negocio.telefono)
            vista.txtDescripcion.setText(pantalla.negocio.descripcion)

            if (pantalla.negocio.imagenes.size != 0) SetImagenes(vista)
        }
        return vista
    }

}