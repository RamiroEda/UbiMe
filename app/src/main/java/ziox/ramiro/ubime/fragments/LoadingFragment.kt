package ziox.ramiro.ubime.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_loading_loading.view.*
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils
import ziox.ramiro.ubime.activities.LoadingActivity
import ziox.ramiro.ubime.activities.MainActivity

/**
 * Creado por Ramiro el 9/28/2018 a las 6:14 PM para UbiMe.
 */
class LoadingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_loading_loading, container, false)
        Picasso.get().load("file:///android_asset/logo.png").into(rootView.logoImageView)
        val queue = Volley.newRequestQueue(activity)
        val req = getRequest()
        if(!Utils.getToken(activity).isNotEmpty()){
            queue.add(req)
        }
        return rootView
    }

    private fun getRequest() : StringRequest{
        return StringRequest(Request.Method.GET, Utils.getAPIUrl(), {
            if(activity is LoadingActivity){
                Handler().postDelayed({
                    (activity as LoadingActivity).changeFragment(LoadingActivity.LOGIN_LAYOUT)
                }, 1000)
            }
        },{
            activity?.runOnUiThread {
                Toast.makeText(activity, "No se pudo conectar.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}