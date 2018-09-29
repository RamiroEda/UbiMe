package ziox.ramiro.ubime.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.SurfaceView
import github.nisrulz.qreader.QREader
import ziox.ramiro.ubime.R
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import github.nisrulz.qreader.QRDataListener
import kotlinx.android.synthetic.main.activity_add_persona_vinculada.*
import ziox.ramiro.ubime.Utils


/**
 * Creado por Ramiro el 9/29/2018 a las 9:32 AM para UbiMe.
 */
class AddPersonaVinculadaActivity : AppCompatActivity(){
    private lateinit var qrReader : QREader
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_persona_vinculada)

        val queue = Volley.newRequestQueue(this)

        qrReader = QREader.Builder(this, qrReaderSurface, QRDataListener { data ->
            if(data.startsWith("az1", false)){
                if(!isLoading){
                    runOnUiThread {
                        qrReaderMessage.text = "Vinculando..."
                    }
                    isLoading = true
                    Log.e("asd", data.removePrefix("az1"))
                    val req = getRequest(data.removePrefix("az1"))
                    queue.add(req)
                }
            }else{
                runOnUiThread {
                    qrReaderMessage.text = "QR incorrecto"
                }
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(qrReaderSurface.height)
                .width(qrReaderSurface.width)
                .build()
    }

    override fun onResume() {
        super.onResume()
        qrReader.initAndStart(qrReaderSurface)
    }

    override fun onPause() {
        super.onPause()
        qrReader.releaseAndCleanup()
    }

    private fun getRequest(ownerId : String) : StringRequest{
        return object : StringRequest(Method.POST, Utils.getAPIUrl()+"api/pulsera_user", {
            Toast.makeText(this, "Vinculado con éxito!", Toast.LENGTH_SHORT).show()
            this@AddPersonaVinculadaActivity.finish()
        },{
            isLoading = false
            Log.e("asd", it.toString())
        }){
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("pulsera_id" to "1",
                        "owner_id" to ownerId,
                        "permited_user" to Utils.getUserId(this@AddPersonaVinculadaActivity).toString())
            }
        }
    }
}