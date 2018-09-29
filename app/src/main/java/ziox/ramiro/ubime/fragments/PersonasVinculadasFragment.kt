package ziox.ramiro.ubime.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main_personas_vinculadas.view.*
import kotlinx.android.synthetic.main.item_personas_vinculadas.view.*
import org.json.JSONArray
import org.json.JSONObject
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils
import ziox.ramiro.ubime.activities.AddPersonaVinculadaActivity
import ziox.ramiro.ubime.activities.MapsActivity

/**
 * Creado por Ramiro el 9/28/2018 a las 8:35 PM para UbiMe.
 */
class PersonasVinculadasFragment : Fragment() {
    companion object {
        val CAMERA_REQUEST_CODE = 1
        val STATE_CARGANDO = -1
        val STATE_CONECTADO = 0
        val STATE_DESCONECTADO = 1
    }
    private val personasList = ArrayList<PersonasData>()
    private lateinit var rootView : View
    data class PersonasData(val id : Int, val name : String, var state : Int = STATE_CARGANDO)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?{
        rootView = inflater.inflate(R.layout.fragment_main_personas_vinculadas, container, false)
        val queue = Volley.newRequestQueue(activity)
        val req = getRequest(rootView)

        rootView.personasVinculadasRecycler.layoutManager = LinearLayoutManager(activity)
        rootView.personasVinculadasRecycler.adapter = PersonasAdapter()

        showLoading(rootView)

        rootView.swipePersonasVinculadas.setOnRefreshListener {
            val len = personasList.size
            personasList.clear()
            rootView.personasVinculadasRecycler.adapter.notifyItemRangeRemoved(0,len)
            showLoading(rootView)
            queue.add(req)
            rootView.swipePersonasVinculadas.isRefreshing = false
        }

        rootView.buttonAddPersona.setOnClickListener {
            if(ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                startActivity(Intent(activity, AddPersonaVinculadaActivity::class.java))
            }else{
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        }

        return rootView
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_REQUEST_CODE){
            try {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startActivity(Intent(activity, AddPersonaVinculadaActivity::class.java))
                }else{
                    activity!!.runOnUiThread {
                        Toast.makeText(activity, "Permisos no concedidos", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e : Exception){
                Log.e("asd", e.toString())
            }
        }
    }

    private fun getRequest(rootView: View) : StringRequest{
        return object : StringRequest(Method.GET, Utils.getAPIUrl()+"api/pulsera_user/"+Utils.getUserId(activity),{
            try{
                val json = JSONArray(it).getJSONObject(0)
                if(json.length() > 0){
                    hideEmptyState(rootView)
                    Log.e("asd", json.toString())
                    for(key in json.keys()){
                        personasList.add(PersonasData(json.getInt(key), key))
                    }
                    rootView.personasVinculadasRecycler.adapter.notifyDataSetChanged()
                }else{
                    showEmptyState(rootView)
                }
            }catch (e : Exception){
                Log.e("asd", e.toString())
                showEmptyState(rootView)
            }
            hideLoading(rootView)
        },{
            showEmptyState(rootView)
            hideLoading(rootView)
        }){
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Accept" to "application/json")
            }

            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("token" to Utils.getToken(activity))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            showLoading(rootView)
            val len = personasList.size
            personasList.clear()
            rootView.personasVinculadasRecycler.adapter.notifyItemRangeRemoved(0,len)
            val queue = Volley.newRequestQueue(activity)
            val req = getRequest(rootView)
            queue.add(req)
        }catch (e : Exception){
            Log.e("asd", e.toString())
        }
    }

    private fun showLoading(rootView: View){
        activity!!.runOnUiThread {
            rootView.buttonAddPersona.visibility = View.GONE
            hideEmptyState(rootView)
            rootView.personasVinculadasProgressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading(rootView: View){
        activity!!.runOnUiThread {
            rootView.buttonAddPersona.visibility = View.VISIBLE
            rootView.personasVinculadasProgressBar.visibility = View.GONE
        }
    }

    private fun showEmptyState(rootView: View){
        activity!!.runOnUiThread {
            rootView.emptyStatePersonasVinculadas.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyState(rootView: View){
        activity!!.runOnUiThread {
            rootView.emptyStatePersonasVinculadas.visibility = View.GONE
        }
    }

    inner class PersonasAdapter : RecyclerView.Adapter<PersonasAdapter.PersonasViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonasViewHolder {
            return PersonasViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_personas_vinculadas, parent, false))
        }

        override fun getItemCount(): Int {
            return personasList.size
        }

        override fun onBindViewHolder(holder: PersonasViewHolder, position: Int) {
            val data = personasList[holder.adapterPosition]
            holder.name.text = data.name

            holder.buttonEliminar.setOnClickListener {
                val alert = AlertDialog.Builder(activity)
                alert.setMessage("¿Desea eliminar a esta persona?")
                        .setPositiveButton("Aceptar"){ _, _ ->
                            personasList.remove(data)
                            this@PersonasAdapter.notifyItemRemoved(holder.adapterPosition)

                        }.setNegativeButton("Cancelar", null)
                alert.create().show()
            }

            val queue = Volley.newRequestQueue(activity)
            val request = getRequestConnected(data, holder.state)

            queue.add(request)

            holder.buttonVer.setOnClickListener {
                if(data.state == STATE_CONECTADO){
                    val intent = Intent(activity, MapsActivity::class.java)
                    intent.putExtra("id", data.id)
                    startActivity(intent)
                }else{
                    Toast.makeText(activity, "El usuario no está disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }


        inner class PersonasViewHolder constructor(item : View) : RecyclerView.ViewHolder(item){
            var name = TextView(this@PersonasVinculadasFragment.activity)
            var state = TextView(this@PersonasVinculadasFragment.activity)
            var buttonVer = Button(this@PersonasVinculadasFragment.activity)
            var buttonEliminar = Button(this@PersonasVinculadasFragment.activity)
            var view = ConstraintLayout(this@PersonasVinculadasFragment.activity)

            init {
                name = item.personasLabelNombre
                state = item.personasLabelEstado
                buttonVer = item.buttonPersonaVer
                buttonEliminar = item.buttonPersonaEliminar
                view = item.itemParent

                state.text = "Cargando..."
                state.setTextColor(resources.getColor(R.color.colorPrimary))

                Picasso.get().load("https://randomuser.me/api/portraits/men/71.jpg").transform(PerfilFragment.CircleTransform()).into(item.itemImageUser)
            }
        }
    }

    private fun getRequestConnected(data : PersonasData, label: TextView) : StringRequest{
        return StringRequest(Request.Method.GET, Utils.getAPIUrl()+"api/user_transport_ubication/${data.id}",{
            try {
                label.text = "Conectado"
                label.setTextColor(activity?.resources!!.getColor(R.color.colorSuccess))
                data.state = STATE_CONECTADO
            }catch (e : Exception){
                Log.e("asd", e.toString())
            }
        },{
            try{
                label.text = "Desconectado"
                label.setTextColor(activity?.resources!!.getColor(R.color.colorDanger))
                data.state = STATE_DESCONECTADO
            }catch (e : Exception){
                Log.e("asd", e.toString())
            }
        })
    }
}