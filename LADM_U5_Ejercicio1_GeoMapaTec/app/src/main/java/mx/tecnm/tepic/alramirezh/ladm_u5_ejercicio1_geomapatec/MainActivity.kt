package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab_personalizado.view.*
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.Pantallas.*
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var areaPertenencia = PolygonOptions()


    var baseRemota = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance().reference
    var negocios = ArrayList<Negocio>()
    lateinit var locacion : LocationManager
    var pantallaComida =  PantallaComida(this)
    var pantallaNegocio =  PantallaNegocio(this)
    var pantallaMapa = PantallaMapa(this)
    var pantallaBebida = PantallaBebidas(this)
    var negocio = Negocio()
    var permisoInternet = false
    var permisoGPS = false

    var buscarActivado = false

    var cargando = true
    var animacionCargando = RotateAnimation(0f , 360f, RotateAnimation.RELATIVE_TO_SELF , 0.5f , RotateAnimation.RELATIVE_TO_SELF , 0.5f)




    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

            areaPertenencia.add(LatLng(21.47804, -104.86831 ))
        areaPertenencia.add(LatLng(21.4768, -104.86876 ))
        areaPertenencia.add(LatLng(21.47477, -104.86235 ))
        areaPertenencia.add(LatLng(21.47645, -104.86158 ))
        areaPertenencia.add(LatLng(21.4768, -104.86247 ))
        areaPertenencia.add(LatLng(21.47617, -104.86282 ))
        areaPertenencia.strokeColor(ContextCompat.getColor(this,R.color.rojoOscuro))
        areaPertenencia .strokeWidth(5f)

        animacionCargando.duration = 1800
        animacionCargando.repeatCount = Animation.INFINITE


        botonCargando.startAnimation( animacionCargando )

        botonCargando.setOnClickListener {
            if(cargando) ToastPersonalizado( this, "Cargando...\nPor favor espere" , true ).show()
            else ToastPersonalizado( this, "App en funcionamiento" , true ).show()
        }

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            val controlador = ControladorPantallas( supportFragmentManager )
            controlador.addPantalla(pantallaComida , "Comidas" )
            controlador.addPantalla(pantallaBebida , "Bebidas" )
            controlador.addPantalla(pantallaMapa , "Ubicacion" )
            controlador.addPantalla(pantallaNegocio , "Negocio" )
            controlador.addPantalla(PantallaAcercaDe() , "Acerca de" )
            contenedorMain.adapter = controlador
            menuTabs.setupWithViewPager( contenedorMain )
            menuTabs.getTabAt(2)!!.select()
            personalizarTabs()
            GetNegocios()

            locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var oyente = Oyente(this)
            locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,01f,oyente)
            locacion.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,01f,oyente)



        }
        else {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)==PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET), 1)
            }else{
                permisoInternet = true
            }
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                 ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 2)
            else permisoGPS = true



        }

        /*
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }  */

    }



    fun personalizarTabs() {
        val tabComidas: View = LayoutInflater.from( this ).inflate( R.layout.tab_personalizado , null )
        tabComidas.imgTab.setImageResource( R.drawable.icono_comida )
        tabComidas.txtTitulo.setText("Comidas")
        menuTabs.getTabAt(0)!!.setCustomView(tabComidas)

        val tabBebidas: View = LayoutInflater.from( this ).inflate( R.layout.tab_personalizado , null )
        tabBebidas.imgTab.setImageResource( R.drawable.icono_bebida )
        tabBebidas.txtTitulo.setText("Bebidas")
        menuTabs.getTabAt(1)!!.setCustomView(tabBebidas)

        val tabUbicacion: View = LayoutInflater.from( this ).inflate( R.layout.tab_personalizado , null )
        tabUbicacion.imgTab.setImageResource( R.drawable.icono_ubicacion )
        tabUbicacion.txtTitulo.setText("GPS")
        menuTabs.getTabAt(2)!!.setCustomView(tabUbicacion)



        val tabNegocio: View = LayoutInflater.from( this ).inflate( R.layout.tab_personalizado , null )
        tabNegocio.imgTab.setImageResource( R.drawable.icono_negocio )
        tabNegocio.txtTitulo.setText("Negocio")
        menuTabs.getTabAt(3)!!.setCustomView(tabNegocio)

        val tabAcerca: View = LayoutInflater.from( this ).inflate( R.layout.tab_personalizado , null )
        tabAcerca.imgTab.setImageResource( R.drawable.icono_ayuda )
        tabAcerca.txtTitulo.setText("Acerca de")
        menuTabs.getTabAt(4)!!.setCustomView(tabAcerca)
    }




    fun GetNegocios() {
        baseRemota.collection("negocios").get().addOnSuccessListener { documentos ->
            var totalNegocios = documentos.size()
            try {
                var datosNegocios = ""
                for (doc in documentos!!) {
                    var nombre = doc.getString("nombre").toString()
                    var puntos = doc.get("puntos") as ArrayList<GeoPoint>
                    var puntocentral = doc.getGeoPoint("puntocentral")
                    var horario = doc.getString("horario").toString()
                    var telefono = doc.getString("telefono").toString()
                    var imagenesrutas = doc.get("imagenes") as ArrayList<String>
                    var id = doc.id
                    var desc = doc.getString("descripcion").toString()
                    val negocio = Negocio()
                    negocio.setDatos(nombre, puntos, puntocentral!!, horario, telefono, id , imagenesrutas.size, this,desc)
                    for (nombreArchivo in imagenesrutas) {
                        GetImagen(nombreArchivo, negocio, id+"/" , totalNegocios)
                    }
               }

            }catch(er: Exception){ ToastPersonalizado(this,er.message!!,true).show() }
            //pantallaNegocio.mostrarInfo(negocios.get(0))

        }.addOnFailureListener {
            ToastPersonalizado(this, "Fallido", true).show()
        }
    }


    var itemsCargados = 0
    fun GetMenu( neg: Negocio ) {
        baseRemota.collection("menu").whereEqualTo("IDNegocio" , neg.ID ).get().addOnSuccessListener { documentos ->
            neg.listaComidas.clear()
            neg.listaBebidas.clear()
            itemsCargados = 0
            var totalItems = documentos.size()
            try {
                for (doc in documentos!!) {
                    var nombre = doc.getString("nombre").toString()
                    var descripcion = doc.getString("descripcion").toString()
                    var precio = doc.getDouble("precio")!!
                    var tipo = doc.getString("tipo").toString()
                    var nombreImagen = doc.getString("imagen").toString()
                    val itemMenu = ItemMenu()
                    itemMenu.setDatos( nombre , precio , descripcion , tipo , this )
                    GetImagen(nombreImagen, itemMenu, "" , totalItems , neg)
                }
            }catch(er: Exception){ ToastPersonalizado(this,er.message!!,true).show() }
        }.addOnFailureListener {
            ToastPersonalizado(this, "Fallido", true).show()
        }
    }



    fun GetImagen(nombreArchivo:String, negocio:Negocio, carpeta:String , totalNegocios: Int) {
        try {
            var ref = storage.child(carpeta+nombreArchivo)
            var nombreExtension = nombreArchivo.split(".")
            val archivoImagen = File.createTempFile(nombreExtension[0], nombreExtension[1])
            ref.getFile(archivoImagen).addOnSuccessListener {
                negocio.addImagen(archivoImagen.absolutePath)
                if( negocio.totalImagenesDefault == 0 ) {
                    negocios.add( negocio )
                    if( negocios.size == totalNegocios ) {
                        botonCargando.clearAnimation()
                        cargando = false
                        ToastPersonalizado(this,"Negocios Sincronizados" , true).show()

                    }
                }
            }.addOnFailureListener {
                negocio.totalImagenesDefault--
                if( negocio.totalImagenesDefault == 0 ) {
                    negocios.add( negocio )
                    if( negocios.size == totalNegocios ) {
                        botonCargando.clearAnimation()
                        cargando = false
                        ToastPersonalizado(this,"Negocios Sincronizados" , true).show()
                    }
                }
                //ToastPersonalizado(this, it.message.toString() + "\n fallido", true).show()
            }
        }catch(e:Exception){
            ToastPersonalizado(this,e.message.toString(),true).show()
        }
    }


    fun GetImagen(nombreArchivo:String, itemMenu: ItemMenu, carpeta:String , totalItems: Int , neg: Negocio) {
        try {
            var ref = storage.child(carpeta+nombreArchivo)
            var nombreExtension = nombreArchivo.split(".")
            val archivoImagen = File.createTempFile(nombreExtension[0], nombreExtension[1])
            ref.getFile(archivoImagen).addOnSuccessListener {
                itemMenu.setImagen(archivoImagen.absolutePath)
                if( itemMenu.tipo == "COMIDA" ) neg.listaComidas.add( itemMenu )
                else   neg.listaBebidas.add( itemMenu )
                itemsCargados++
                if( itemsCargados == totalItems ) {
                    botonCargando.clearAnimation()
                    cargando = false
                    txtTitulo.setText( neg.nombre )
                    negocio = neg
                    ToastPersonalizado(this,"Negocio detectado:\n${neg.nombre}",true).show()
                    menuTabs.getTabAt(0)!!.select()
                }
            }
            .addOnFailureListener {
                itemsCargados++
                if( itemsCargados == totalItems ) {
                    botonCargando.clearAnimation()
                    cargando = false
                    txtTitulo.setText( neg.nombre )
                    negocio = neg
                    ToastPersonalizado(this,"Negocio detectado:\n${neg.nombre}",true).show()
                    menuTabs.getTabAt(0)!!.select()
                }
            //ToastPersonalizado(this, it.message.toString() + "\n fallido", true).show()
            }
        }catch(e:Exception){
            ToastPersonalizado(this,e.message.toString(),true).show()
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){
            if( ActivityCompat.checkSelfPermission( this ,
                    android.Manifest.permission.INTERNET ) == PackageManager.PERMISSION_GRANTED )
            {
                permisoInternet = true
                if(permisoGPS){
                    val controlador = ControladorPantallas( supportFragmentManager )
                    controlador.addPantalla(pantallaComida , "Comidas" )
                    controlador.addPantalla(pantallaBebida , "Bebidas" )
                    controlador.addPantalla(pantallaMapa , "Ubicacion" )
                    controlador.addPantalla(pantallaNegocio , "Negocio" )
                    controlador.addPantalla(PantallaAcercaDe() , "Acerca de" )
                    contenedorMain.adapter = controlador
                    menuTabs.setupWithViewPager( contenedorMain )
                    menuTabs.getTabAt(2)!!.select()
                    personalizarTabs()
                    GetNegocios()

                    locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    var oyente = Oyente(this)
                    locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,01f,oyente)
                    locacion.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,01f,oyente)
                }
            }else {
                finish()
            }
        }
        else if(requestCode==2){
            if( ActivityCompat.checkSelfPermission( this ,
                    android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED )
            {
                permisoGPS = true
                if(permisoInternet){
                    val controlador = ControladorPantallas( supportFragmentManager )
                    controlador.addPantalla(pantallaComida , "Comidas" )
                    controlador.addPantalla(pantallaBebida , "Bebidas" )
                    controlador.addPantalla(pantallaMapa , "Ubicacion" )
                    controlador.addPantalla(pantallaNegocio , "Negocio" )
                    controlador.addPantalla(PantallaAcercaDe() , "Acerca de" )
                    contenedorMain.adapter = controlador
                    menuTabs.setupWithViewPager( contenedorMain )
                    menuTabs.getTabAt(2)!!.select()
                    personalizarTabs()
                    GetNegocios()

                    locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    var oyente = Oyente(this)
                    locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,01f,oyente)
                    locacion.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,01f,oyente)
                }
            }else {
                finish()
            }
        }




    }



}




class Oyente(puntero:MainActivity): LocationListener {

    var p = puntero
    var  GPSenZona = false


    override fun onLocationChanged(ubicacion: Location) {
        if( p.pantallaMapa.mapa == null || p.cargando == true ) return
        p.pantallaMapa.mapa!!.clear()
        var neg = Negocio()
        var estoyEnZona = false
        val lat = ubicacion.latitude
        val lon = ubicacion.longitude

        // DIBUJAR LA UBICACION DEL GPS
        var circuloGPS = CircleOptions()
        circuloGPS.radius(2.0)
        circuloGPS.fillColor(ContextCompat.getColor(p, R.color.miUbicacionFill))
        circuloGPS.strokeColor(ContextCompat.getColor(p,R.color.miUbicacionBorde))
        circuloGPS.strokeWidth(2f)
        circuloGPS.center(LatLng(lat , lon))
        var miUbicacion = p.pantallaMapa.mapa!!.addCircle(circuloGPS)


        //Dibujar PERIMETRO

        p.pantallaMapa.mapa!!.addPolygon(p.areaPertenencia)


        //COMPROBAR SI ESTOY EN EL PERIMETRO
        if(PolyUtil.containsLocation(lat,lon,p.areaPertenencia.points,false)==false) {
            p.negocio = Negocio()
            GPSenZona = false
            p.txtTitulo.setText("Fuera de area\n no hay negocios cercanos")
            ToastPersonalizado(p, "Estas fuera del area delimitada", true).show()
            return
        }


        // VERIFICAR SI GPS ESTA EN UNA ZONA
        for(Negocio in p.negocios){
            if(Negocio.estaEnLaZona(lat , lon )){
                estoyEnZona=true
                neg = Negocio
                break
            }
        }

        if(estoyEnZona){
            p.pantallaMapa.mapa!!.addMarker(MarkerOptions().position(LatLng(neg.puntocentral.latitude,neg.puntocentral.longitude))
               .title(neg.nombre))
            if( p.negocio.ID != neg.ID || GPSenZona == false) {
                GPSenZona = true
                p.pantallaMapa.mapa!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat , lon),19f))
                // CUANDO NO HAY NEGOCIO MOSTRADO ACTUALMENTE
                p.botonCargando.startAnimation( p.animacionCargando )
                p.txtTitulo.setText("Negocio Encontrado...")
                p.cargando = true
                p.GetMenu( neg )
            }
        } else {

            neg = p.negocios.get(0)
            var distancia = neg.distanciaEnMetros(lat,lon)
            var i = 1
            while (i<p.negocios.size){
                var distancia2 = p.negocios.get(i).distanciaEnMetros(lat,lon)
                if(distancia2<distancia){
                    neg=p.negocios.get(i)
                    distancia = distancia2
                }
                i++

            }

                p.pantallaMapa.mapa!!.addMarker(
                    MarkerOptions().position(
                        LatLng(neg.puntocentral.latitude, neg.puntocentral.longitude)
                    )
                        .title(neg.nombre)
                )
                var recta = PolylineOptions()
                recta.add(LatLng(lat, lon))
                recta.add(LatLng(neg.puntocentral.latitude, neg.puntocentral.longitude))
                recta.color(ContextCompat.getColor(p, R.color.rojoOscuro))
                recta.width(3f)
                p.pantallaMapa.distancia = distancia



                p.pantallaMapa.mapa!!.addPolyline(recta)
                p.txtTitulo.setText("Negocio mas cercano:\n${neg.nombre}")
                 if( p.negocio.ID != neg.ID || GPSenZona==true) {
                     GPSenZona = false
                     p.pantallaMapa.mapa!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat , lon),19f))
                p.negocio = neg
                     p.negocio.listaComidas.clear()
                     p.negocio.listaBebidas.clear()

                p.menuTabs.getTabAt(0)!!.select()
                //ToastPersonalizado( p , "Negocio mas cercano localizado" , true ).show()
            }
        }

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }



}

