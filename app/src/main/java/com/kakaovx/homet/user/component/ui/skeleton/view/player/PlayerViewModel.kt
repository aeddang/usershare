package com.kakaovx.homet.user.component.ui.skeleton.view.player

import com.google.android.exoplayer2.source.MediaSource

class PlayerViewModel{

    var playbackPosition:Long = 0
    var currentWindow:Int = 0
    var playWhenReady:Boolean = false
    var source: MediaSource? = null

    constructor(
    )

    fun destroy(){
        source = null
    }

}