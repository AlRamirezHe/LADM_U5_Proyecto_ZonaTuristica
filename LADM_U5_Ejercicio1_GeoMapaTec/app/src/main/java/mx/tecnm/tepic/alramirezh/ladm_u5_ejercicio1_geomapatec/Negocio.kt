package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.PolyUtil
import java.io.ByteArrayOutputStream

class Negocio {
    var nombre =""
    var puntos = PolygonOptions()
    var puntocentral = Location("puntocentral")
    var horario =""
    var descripcion=""
    var telefono = ""
    var imagenes = ArrayList<Bitmap>()
    var totalImagenesDefault = 0
    var listaComidas = ArrayList<ItemMenu>()
    var listaBebidas = ArrayList<ItemMenu>()
    var ID = ""

    fun setDatos(Nombre:String, arreglo:ArrayList<GeoPoint> , central:GeoPoint , Horario:String , Telefono:String ,
        id:String , totalImg: Int , pantalla: MainActivity,Descripcion:String) {
        nombre = Nombre
        horario=Horario
        telefono=Telefono
        ID = id
        descripcion = Descripcion
        totalImagenesDefault = totalImg
        puntos.fillColor(ContextCompat.getColor(pantalla,R.color.rojoTransparente))
        for(punto in arreglo){
            puntos.add(LatLng(punto.latitude, punto.longitude))
        }
        puntocentral.latitude = central.latitude
        puntocentral.longitude = central.longitude
/*
        ( 1 .. totalImagenesDefault ).forEach {
            var imagenDefault = pantalla.resources.getDrawable(R.drawable.icono_negocio) as BitmapDrawable
            imagenes.add( convertirABytes( imagenDefault.bitmap ) )
        } */
    }


    fun convertirABytes( img: Bitmap ): ByteArray {
        var stream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG , 100 , stream)
        return stream.toByteArray()
    }

    fun convertirABitmap( img: ByteArray ): Bitmap {
        return BitmapFactory.decodeByteArray( img , 0 , img.size )
    }


    fun estaEnLaZona(latitud: Double, longitud: Double):Boolean{
        return PolyUtil.containsLocation(latitud,longitud,puntos.points,false)


    }

    fun distanciaEnMetros(latitud:Double,longitud:Double): Float{
        var ubicacionGPS = Location("GPS")
        ubicacionGPS.latitude = latitud
        ubicacionGPS.longitude = longitud
        return puntocentral.distanceTo(ubicacionGPS)

    }

    fun addImagen(path:String){
        if( totalImagenesDefault != 0 ) {
         //   imagenes.removeAt(0)
            totalImagenesDefault--
        }
        imagenes.add( BitmapFactory.decodeFile(path) )
    }
}