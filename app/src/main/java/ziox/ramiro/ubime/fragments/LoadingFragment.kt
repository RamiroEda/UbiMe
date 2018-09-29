package ziox.ramiro.ubime.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ziox.ramiro.ubime.R

/**
 * Creado por Ramiro el 9/28/2018 a las 6:14 PM para UbiMe.
 */
class LoadingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loading_loading, container, false)
    }
}