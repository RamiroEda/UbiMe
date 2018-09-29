package ziox.ramiro.ubime.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.fragments.PerfilFragment
import ziox.ramiro.ubime.fragments.PersonasVinculadasFragment

class MainActivity : AppCompatActivity() {
    companion object {
        val PERFIL_LAYOUT = 0
        val PERSONAS_VINCULADAS = 1
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navPerfil -> {
                changeFragment(PERFIL_LAYOUT)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navPersonasVinculadas -> {
                changeFragment(PERSONAS_VINCULADAS)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        changeFragment(PERFIL_LAYOUT)
    }

    private fun changeFragment(fragmentName : Int){
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameContainer, when(fragmentName){
            PERFIL_LAYOUT -> PerfilFragment()
            PERSONAS_VINCULADAS -> PersonasVinculadasFragment()
            else -> Fragment()
        }).commit()
    }
}
