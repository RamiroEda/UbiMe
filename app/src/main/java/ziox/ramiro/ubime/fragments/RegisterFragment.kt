package ziox.ramiro.ubime.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.activities.LoadingActivity

/**
 * Creado por Ramiro el 9/28/2018 a las 5:59 PM para UbiMe.
 */
class RegisterFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_loading_register, container, false)

        val queue = Volley.newRequestQueue(activity)


        val req = object : StringRequest(Request.Method.POST, "http://192.168.137.1:8000/api/register", {
            try{

            }catch (e : Exception){
                Log.e("asd", e.toString())
            }
        },{
            Log.e("asd", "$it -> ${it.networkResponse}")
        }){
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Accept" to "application/json")
            }

            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("email" to "ramiroeda3@jeje.com",
                        "password" to "holis123",
                        "c_password" to "holis123",
                        "name" to "Ramiro Estrada")
            }
        }


        //queue.add(req)

        return rootView
    }
}