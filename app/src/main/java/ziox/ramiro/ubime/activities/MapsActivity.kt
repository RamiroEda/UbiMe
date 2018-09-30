package ziox.ramiro.ubime.activities

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*

import org.json.JSONObject
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var runnable: Runnable
    private lateinit var handler : Handler
    private var id: Int = 0
    private var isDragged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        id = intent.extras.getInt("id")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initInterval(2)
    }

    private fun initInterval(intervalSeconds : Int){
        val queue = Volley.newRequestQueue(this)
        val req = getRequest()
        handler = Handler()


        runnable = Runnable {
            queue.add(req)
            handler.postDelayed(runnable, intervalSeconds * DateUtils.SECOND_IN_MILLIS)
        }

        handler.post(runnable)
    }

    override fun onResume() {
        super.onResume()
        if(::handler.isInitialized){
            handler.post(runnable)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun getRequest() : StringRequest{
        return StringRequest(Request.Method.GET, Utils.getAPIUrl()+"api/user_transport_ubication/$id",{
            try{
                positionMarkerMap.visibility = View.VISIBLE
                val json = JSONObject(it)
                mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                        LatLng(json.getString("latitud").toDouble(),json.getString("longitud").toDouble()), 15f
                                )
                        )
                )
            }catch (e : Exception){
                Log.e("asd", e.toString())
            }
        },{
            Log.e("asd", it.toString())
        })
    }
}
