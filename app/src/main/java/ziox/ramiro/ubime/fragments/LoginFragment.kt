package ziox.ramiro.ubime.fragments

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_loading_login.view.*
import org.json.JSONObject
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils
import ziox.ramiro.ubime.activities.LoadingActivity
import ziox.ramiro.ubime.activities.MainActivity

/**
 * Creado por Ramiro el 9/28/2018 a las 5:19 PM para UbiMe.
 */
class LoginFragment : Fragment(){
    var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_loading_login, container, false)
        val queue = Volley.newRequestQueue(activity)
        val req = getRequest(rootView)

        Picasso.get().load("file:///android_asset/logo.png").into(rootView.logoLogin)

        rootView.registerButton.setOnClickListener {
            if(activity is LoadingActivity){
                (activity as LoadingActivity).changeFragment(LoadingActivity.REGISTER_LAYOUT)
            }
        }

        rootView.loginButton.setOnClickListener {
            if(!isLoading){
                removeErrors(rootView)
                if(validateInputs(rootView)){
                    rootView.loginProgressBar.visibility = View.VISIBLE
                    isLoading = true
                    queue.add(req)
                }else{
                    putErrors(rootView)
                }
            }
        }

        return rootView
    }

    private fun validateInputs(rootView: View) : Boolean{
        return rootView.loginEmail.editText!!.text.isNotEmpty() && rootView.loginPassword.editText!!.text.isNotEmpty()
    }

    private fun putErrors(rootView: View){
        if(rootView.loginEmail.editText!!.text.isEmpty()){
            rootView.loginEmail.error = "Este campo está vacío."
        }

        if(rootView.loginPassword.editText!!.text.isEmpty()){
            rootView.loginPassword.error = "Este campo está vacío."
        }
    }

    private fun removeErrors(rootView: View){
        rootView.loginEmail.error = null
        rootView.loginPassword.error = null
    }

    private fun getRequest(rootView: View) : StringRequest{
        return object : StringRequest(Request.Method.POST, Utils.getAPIUrl()+"api/login", {
            try{
                val json = JSONObject(it)
                Log.e("asd", json.toString())
                val token = JSONObject(json.getString("success")).getString("token")
                Log.e("asd", token)

                PreferenceManager.getDefaultSharedPreferences(activity).edit()
                        .putString("token", token).apply()

                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }catch (e : Exception){
                Toast.makeText(activity, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show()
            }
            rootView.loginProgressBar.visibility = View.GONE
            isLoading = false
        },{
            Toast.makeText(activity, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show()
            rootView.loginProgressBar.visibility = View.GONE
            isLoading = false
        }){
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Accept" to "application/json")
            }

            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("email" to rootView.loginEmail.editText?.text.toString(),
                        "password" to rootView.loginPassword.editText?.text.toString())
            }
        }
    }
}