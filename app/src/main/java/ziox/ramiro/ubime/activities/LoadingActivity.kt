package ziox.ramiro.ubime.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.fragments.LoadingFragment
import ziox.ramiro.ubime.fragments.LoginFragment
import ziox.ramiro.ubime.fragments.RegisterFragment

/**
 * Creado por Ramiro el 9/28/2018 a las 2:26 PM para UbiMe.
 */
class LoadingActivity : AppCompatActivity() {
    companion object {
        val LOADING_LAYOUT = 0
        val LOGIN_LAYOUT = 1
        val REGISTER_LAYOUT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.LoadingTheme)
        setContentView(R.layout.activity_loading)

        Handler().postDelayed({
            changeFragment(LOADING_LAYOUT)
        }, 200)
    }

    fun changeFragment(fragmentName : Int){
        supportFragmentManager.beginTransaction().replace(R.id.loadingFragment, when(fragmentName){
            LOADING_LAYOUT -> LoadingFragment()
            LOGIN_LAYOUT -> LoginFragment()
            REGISTER_LAYOUT -> RegisterFragment()
            else -> Fragment()
        }).commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.first() is RegisterFragment){
            changeFragment(LOGIN_LAYOUT)
        }else{
            super.onBackPressed()
        }
    }
}