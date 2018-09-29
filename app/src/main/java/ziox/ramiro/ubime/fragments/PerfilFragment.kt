package ziox.ramiro.ubime.fragments

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.fragment_main_perfil.view.*
import org.json.JSONObject
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils
import ziox.ramiro.ubime.activities.LoadingActivity
import ziox.ramiro.ubime.dialogs.QRGeneratorDialog

/**
 * Creado por Ramiro el 9/28/2018 a las 8:35 PM para UbiMe.
 */
class PerfilFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_perfil, container, false)
        val queue = Volley.newRequestQueue(activity)
        val req = getRequest(rootView)
        Picasso.get().load("https://randomuser.me/api/portraits/men/71.jpg").transform(CircleTransform()).into(rootView.perfilImagen)

        rootView.toolbar.inflateMenu(R.menu.menu_main)
        rootView.toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.menu_close_sesion -> {
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply()
                    startActivity(Intent(activity, LoadingActivity::class.java))
                }
                R.id.menu_gen_qr -> {
                    QRGeneratorDialog().show(childFragmentManager, "qr_dialog")
                }
            }
            true
        }

        rootView.buttonRecargarSaldo.setOnClickListener {
            queue.add(paySaldoRequest(rootView, queue))
        }

        queue.add(req)

        return rootView
    }

    private fun getRequest(rootView: View) : StringRequest {
        return object : StringRequest(Method.POST, Utils.getAPIUrl()+"api/details",{
            try{
                val json = JSONObject(it).getJSONObject("success")
                rootView.perfilNombre.text = json.getString("name")
                rootView.perfilSaldo.text = "\$${json.getString("saldo").toFloat()}"
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putInt("id",json.getInt("id")).apply()
            }catch (e : Exception){
                activity?.runOnUiThread {
                    Toast.makeText(activity, "Ha ocurrido un error al cargar tu usuario", Toast.LENGTH_SHORT).show()
                }
            }
        },{
            Log.e("asd", it.toString())
        }){
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Accept" to "application/json",
                        "Authorization" to "Bearer "+Utils.getToken(activity),
                        "Content-Type" to "application/x-www-form-urlencoded")
            }
        }
    }

    private fun paySaldoRequest(rootView: View, queue : RequestQueue) : StringRequest{
        return object : StringRequest(Method.POST, Utils.getAPIUrl()+"api/user_payment",{
            try{
                val json = JSONObject(it)
                Log.e("asd", json.toString())
                queue.add(getRequest(rootView))
            }catch (e : Exception){
                activity?.runOnUiThread {
                    Toast.makeText(activity, "Ha ocurrido un error al recargar saldo", Toast.LENGTH_SHORT).show()
                }
            }
        },{

        }){
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("module_id" to "0",
                        "user_id" to Utils.getUserId(activity).toString(),
                        "saldo" to "50")
            }
        }
    }

    class CircleTransform : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)

            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, source.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader
            paint.isAntiAlias = true

            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)

            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }
}
