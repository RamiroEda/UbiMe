package ziox.ramiro.ubime.fragments

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_loading_register.view.*
import org.json.JSONObject
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils
import ziox.ramiro.ubime.activities.LoadingActivity

/**
 * Creado por Ramiro el 9/28/2018 a las 5:59 PM para UbiMe.
 */
class RegisterFragment : Fragment() {
    var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_loading_register, container, false)
        val queue = Volley.newRequestQueue(activity)
        val req = getRequest(rootView)

        Picasso.get().load("file:///android_asset/logo.png").into(rootView.logoRegister)

        rootView.buttonRegisterBack.setOnClickListener {
            if(activity is LoadingActivity){
                (activity as LoadingActivity).changeFragment(LoadingActivity.LOGIN_LAYOUT)
            }
        }

        rootView.registerButton.setOnClickListener {
            if(!isLoading){
                removeErrors(rootView)
                if(validateInputs(rootView)){
                    rootView.registerProgressBar.visibility = View.VISIBLE
                    isLoading = true
                    queue.add(req)
                }else{
                    putErrors(rootView)
                }
            }
        }

        return rootView
    }

    private fun getRequest(rootView: View) : StringRequest{
        return object : StringRequest(Request.Method.POST, Utils.getAPIUrl()+"api/register", {
            try{
                if(JSONObject(it).length() > 0){
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Registrado con éxito.", Toast.LENGTH_SHORT).show()
                        if(activity is LoadingActivity){
                            (activity as LoadingActivity).changeFragment(LoadingActivity.LOGIN_LAYOUT)
                        }
                        rootView.registerProgressBar.visibility = View.GONE
                    }
                }
            }catch (e : Exception){
                activity?.runOnUiThread {
                    Toast.makeText(activity, "No se pudo registrar.", Toast.LENGTH_SHORT).show()
                    rootView.registerProgressBar.visibility = View.GONE
                }
                Log.e("asd", e.toString())
            }
            isLoading = false
        },{
            activity?.runOnUiThread {
                rootView.registerProgressBar.visibility = View.GONE
                Toast.makeText(activity, "No se pudo registrar.", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }){
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Accept" to "application/json")
            }

            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("email" to rootView.registerEmail.editText?.text.toString(),
                        "password" to rootView.registerPassword.editText?.text.toString(),
                        "c_password" to rootView.registerConfirmPassword.editText?.text.toString(),
                        "name" to rootView.registerName.editText?.text.toString())
            }
        }
    }

    private fun validateInputs(rootView: View) : Boolean{
        return rootView.registerEmail.editText?.text.toString().isNotEmpty() &&
                rootView.registerPassword.editText?.text.toString().isNotEmpty() &&
                rootView.registerConfirmPassword.editText?.text.toString().isNotEmpty() &&
                rootView.registerName.editText?.text.toString().isNotEmpty()

    }

    private fun putErrors(rootView: View){
        activity?.runOnUiThread {
            if(rootView.registerEmail.editText?.text.toString().isEmpty()){
                rootView.registerEmail.error = "Este campo esta vacio"
            }

            if(rootView.registerName.editText?.text.toString().isEmpty()){
                rootView.registerName.error = "Este campo esta vacio"
            }

            if(rootView.registerPassword.editText?.text.toString().isEmpty()){
                rootView.registerPassword.error = "Este campo esta vacio"
            }

            if(rootView.registerConfirmPassword.editText?.text.toString().isEmpty()){
                rootView.registerConfirmPassword.error = "Este campo esta vacio"
            }
        }
    }

    private fun removeErrors(rootView: View){
        activity?.runOnUiThread {
            rootView.registerEmail.error = null
            rootView.registerName.error = null
            rootView.registerPassword.error = null
            rootView.registerConfirmPassword.error = null
        }
    }
}