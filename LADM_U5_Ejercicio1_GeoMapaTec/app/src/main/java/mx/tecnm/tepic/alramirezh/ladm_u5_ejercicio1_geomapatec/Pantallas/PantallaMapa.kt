package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.Pantallas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_pantalla_mapa.view.*
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.MainActivity
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.R
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.ToastPersonalizado
import java.lang.Exception


class PantallaMapa(activity: MainActivity) : Fragment() , OnMapReadyCallback {

    var mapa : GoogleMap? = null
    var activity = activity
    var distancia = 0f
    var etiquetaDistancia: TextView? = null


    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap
        val tec = LatLng(21.4769, -104.86655)
        mapa!!.moveCamera(CameraUpdateFactory.newLatLngZoom(tec,18f))
        mapa!!.uiSettings.isZoomControlsEnabled = true
       mapa!!.addPolygon(activity.areaPertenencia)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapa = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_pantalla_mapa, container, false)
        mapa = null
        vista.txtDistancia.setText("${distancia} mts")
        etiquetaDistancia = vista.txtDistancia
        vista.txtDistancia.setOnClickListener {
            ToastPersonalizado(activity,"distancia en metros de tu ubicacion al negocio mas cercano",true).show()
        }

        try {
            val mapF = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
            mapF.getMapAsync(this)
        } catch(e: Exception){ ToastPersonalizado( activity,e.message.toString(),true).show()  }
        return vista
    }



}