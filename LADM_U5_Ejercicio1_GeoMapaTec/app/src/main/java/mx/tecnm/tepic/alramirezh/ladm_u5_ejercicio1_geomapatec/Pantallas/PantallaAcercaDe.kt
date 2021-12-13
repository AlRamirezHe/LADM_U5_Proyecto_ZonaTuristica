package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.Pantallas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.R


class PantallaAcercaDe : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pantalla_acerca_de, container, false)
    }


}