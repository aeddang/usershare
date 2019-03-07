package com.kakaovx.homet.user.ui.player

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class PlayerFragment : Fragment() {

    val TAG = javaClass.simpleName

    companion object {
        fun newInstance() = PlayerFragment()
    }

    private lateinit var viewModel: PlayerViewModel

    private fun routeToMainPage() {
        Log.d(TAG, "routeToMainPage()")
        val i = Intent(AppConst.HOMET_ACTIVITY_MAIN)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
