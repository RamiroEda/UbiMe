package ziox.ramiro.ubime.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_main_perfil.view.*
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.activities.LoadingActivity

/**
 * Creado por Ramiro el 9/28/2018 a las 8:35 PM para UbiMe.
 */
class PerfilFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_perfil, container, false)

        rootView.toolbar.inflateMenu(R.menu.menu_main)
        rootView.toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.menu_close_sesion -> {
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply()
                    startActivity(Intent(activity, LoadingActivity::class.java))
                }
            }
            true
        }

        return rootView
    }


}
