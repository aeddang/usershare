package com.kakaovx.homet.user.component.ui.view.player

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.ui.PlayerView
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.player.Player
import kotlinx.android.synthetic.main.ui_player.view.*

class VXPlayer: Player {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun getAppName(): Int { return R.string.app_name }
    override fun getLayoutResId(): Int { return R.layout.ui_player }
    override fun getPlayerView(): PlayerView { return playerView }

}