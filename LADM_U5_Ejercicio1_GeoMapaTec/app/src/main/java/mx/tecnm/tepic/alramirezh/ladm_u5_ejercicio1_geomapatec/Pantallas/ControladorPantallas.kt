package mx.tecnm.tepic.alramirezh.ladm_u5_ejercicio1_geomapatec.Pantallas

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ControladorPantallas(supportFragmentManager: FragmentManager):FragmentPagerAdapter(supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var listaPantallas = ArrayList<Fragment>()
    var listaTitulosPantallas = ArrayList<String>()
    override fun getCount(): Int {
        return listaPantallas.size
    }

    override fun getItem(position: Int): Fragment {
        return listaPantallas.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listaTitulosPantallas.get(position)
    }

    fun addPantalla(pantalla:Fragment, titulo:String){
        listaPantallas.add(pantalla)
        listaTitulosPantallas.add(titulo)

    }

}