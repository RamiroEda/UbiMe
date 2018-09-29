package ziox.ramiro.ubime.fragments

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.fragment_main_perfil.view.*
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils
import ziox.ramiro.ubime.activities.LoadingActivity

/**
 * Creado por Ramiro el 9/28/2018 a las 8:35 PM para UbiMe.
 */
class PerfilFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_perfil, container, false)

        Picasso.get().load("https://randomuser.me/api/portraits/lego/1.jpg").transform(CircleTransform()).into(rootView.perfilImagen)

        rootView.toolbar.inflateMenu(R.menu.menu_main)
        rootView.toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.menu_close_sesion -> {
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply()
                    startActivity(Intent(activity, LoadingActivity::class.java))
                }
            }
            true
        }

        return rootView
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
