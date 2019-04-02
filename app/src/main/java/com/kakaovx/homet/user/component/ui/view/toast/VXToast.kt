package com.kakaovx.homet.user.component.ui.view.toast

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.kakaovx.homet.user.R
import kotlinx.android.synthetic.main.ui_toast.view.*

object VXToast {

    @SuppressLint("InflateParams")

    fun makeToast(context: Context, body: Int, duration: Int): Toast {
        return VXToast.makeToast(context, context.getString(body), duration)
    }

    fun makeToast(context: Context, body: String, duration: Int): Toast {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.ui_toast, null)
        v.message.text = body
        val toast = Toast(context)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.view = v
        toast.duration = duration
        return toast
    }
}