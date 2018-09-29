package ziox.ramiro.ubime

import android.content.Context
import android.graphics.*
import android.preference.PreferenceManager
import com.squareup.picasso.Transformation


/**
 * Creado por Ramiro el 9/28/2018 a las 7:48 PM para UbiMe.
 */
class Utils {
    companion object {
        fun getAPIUrl() : String{
            return "http://192.168.137.1:8000/"
        }

        fun getToken(context : Context?) : String{
            return PreferenceManager.getDefaultSharedPreferences(context).getString("token", "")
        }

        fun getUserId(context : Context?) : Int{
            return PreferenceManager.getDefaultSharedPreferences(context).getInt("id", 0)
        }

        fun validateEmail(email : String) : Boolean{
            return email.matches(Regex("[A-Za-z]+@[A-Za-z]+\\.[A-Za-z]+"))
        }

        fun dpToPixel(dp : Int, context: Context) : Int{
            return (context.resources.displayMetrics.density * dp).toInt()
        }
    }
}