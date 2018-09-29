package ziox.ramiro.ubime.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_main_personas_vinculadas.view.*
import kotlinx.android.synthetic.main.item_personas_vinculadas.view.*
import org.json.JSONObject
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils

/**
 * Creado por Ramiro el 9/28/2018 a las 8:35 PM para UbiMe.
 */
class PersonasVinculadasFragment : Fragment() {
    val personasList = ArrayList<PersonasData>()
    data class PersonasData(val id : String, val name : String)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_personas_vinculadas, container, false)
        val queue = Volley.newRequestQueue(activity)
        val req = getRequest(rootView)

        rootView.personasVinculadasRecycler.layoutManager = LinearLayoutManager(activity)
        rootView.personasVinculadasRecycler.adapter = PersonasAdapter()

        showLoading(rootView)

        queue.add(req)

        rootView.swipePersonasVinculadas.setOnRefreshListener {
            showLoading(rootView)
            queue.add(req)
            rootView.swipePersonasVinculadas.isRefreshing = false
        }

        return rootView
    }

    private fun getRequest(rootView: View) : StringRequest{
        return object : StringRequest(Method.POST, Utils.getAPIUrl()+"api/vinculados",{
            try{
                if(JSONObject(it).length() > 0){
                    hideEmptyState(rootView)
                    rootView.personasVinculadasRecycler.adapter.notifyDataSetChanged()
                }else{
                    showEmptyState(rootView)
                }
            }catch (e : Exception){
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

    private fun showLoading(rootView: View){
        activity?.runOnUiThread {
            rootView.buttonAddPersona.visibility = View.GONE
            rootView.personasVinculadasProgressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading(rootView: View){
        activity?.runOnUiThread {
            rootView.buttonAddPersona.visibility = View.VISIBLE
            rootView.personasVinculadasProgressBar.visibility = View.GONE
        }
    }

    private fun showEmptyState(rootView: View){
        activity?.runOnUiThread {
            rootView.emptyStatePersonasVinculadas.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyState(rootView: View){
        activity?.runOnUiThread {
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
            holder.name.text = "Nombre ${data.name}"

            holder.buttonEliminar.setOnClickListener {
                personasList.remove(data)
                this.notifyDataSetChanged()
            }
        }


        inner class PersonasViewHolder constructor(item : View) : RecyclerView.ViewHolder(item){
            var name = TextView(this@PersonasVinculadasFragment.activity)
            var state = TextView(this@PersonasVinculadasFragment.activity)
            var buttonVer = Button(this@PersonasVinculadasFragment.activity)
            var buttonEliminar = Button(this@PersonasVinculadasFragment.activity)

            init {
                name = item.personasLabelNombre
                state = item.personasLabelEstado
                buttonVer = item.buttonPersonaVer
                buttonEliminar = item.buttonPersonaEliminar
            }
        }
    }
}