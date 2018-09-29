package ziox.ramiro.ubime.dialogs

import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.dialog_qr_generator.view.*
import ziox.ramiro.ubime.R
import ziox.ramiro.ubime.Utils
import java.lang.Exception

/**
 * Creado por Ramiro el 9/29/2018 a las 11:50 AM para UbiMe.
 */

class QRGeneratorDialog : DialogFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_qr_generator, container, false)
        rootView.QRImageView.setImageBitmap(genQR())
        return rootView
    }

    private fun genQR() : Bitmap?{
        val id = "az1"+ Utils.getUserId(activity).toString()
        val mformat = MultiFormatWriter()

        return try {
            val matrix = mformat.encode(id, BarcodeFormat.QR_CODE, 300,300)
            val enc = BarcodeEncoder()
            enc.createBitmap(matrix)
        }catch (e : Exception){
            null
        }
    }
}