package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ItemMenu {
    var nombre = ""
    var precio = 0.0
    var imagen: Bitmap? = null
    var descripcion = ""
    var tipo = ""


    fun setDatos( Nombre:String , Precio: Double , Descripcion:String , Tipo:String , pantalla:MainActivity ) {
        nombre = Nombre
        precio = Precio
        descripcion = Descripcion
        tipo = Tipo
        if( tipo == "COMIDA" ) imagen = BitmapFactory.decodeResource( pantalla.resources , R.drawable.icono_comida )
        else imagen = BitmapFactory.decodeResource( pantalla.resources , R.drawable.icono_bebida )
    }

    fun setImagen(path:String) {
        imagen = BitmapFactory.decodeFile(path)
    }
}