package com.kakaovx.homet.user.component.ui.skeleton.view.player

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxFrameLayout
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener

abstract class Player : RxFrameLayout, Player.EventListener, VideoListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    companion object {
        private  var viewModelInstance: PlayerViewModel? = null
        fun getViewmodel(): PlayerViewModel {
            if(viewModelInstance == null) viewModelInstance = PlayerViewModel()
            return viewModelInstance!!
        }
    }
    private var viewModel:PlayerViewModel = com.kakaovx.homet.user.component.ui.skeleton.view.player.Player.getViewmodel()
    private var player: SimpleExoPlayer? = null
    private var playerView:PlayerView? = null
    protected open fun getUserAgent(): String { return "AwesomePlayer" }
    @StringRes abstract fun getAppName():Int
    abstract fun getPlayerView(): PlayerView
    override fun onCreatedView() {
    }

    override fun onDestroyedView() {
        releasePlayer()
    }

    open fun onPause(){
        releasePlayer()
    }

    open fun onResume() {
        initPlayer()
    }

    open fun initPlayer() {
        if( player != null ) return
        playerView = getPlayerView()
        player = ExoPlayerFactory.newSimpleInstance(context)
        player?.addListener( this )
        player?.addVideoListener( this )
        playerView?.player = player
        player?.playWhenReady = viewModel.playWhenReady
        player?.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
    }

    open fun releasePlayer() {
        player?.let {
            viewModel.playbackPosition = it.currentPosition
            viewModel.currentWindow = it.currentWindowIndex
            viewModel.playWhenReady = it.playWhenReady
            it.removeListener( this )
            it.removeVideoListener( this )
            it.release()
            player = null
            playerView = null
        }
    }
    private fun buildDataSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, context?.getString( getAppName())))
        return ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = this.getUserAgent()
        val extension = uri.lastPathSegment
        extension?.let {
            return if (it.contains("mp3") || it.contains("mp4")) ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
            else if (it.contains("m3u8")) HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
            else {
                val dashChunkSourceFactory = DefaultDashChunkSource.Factory(DefaultHttpDataSourceFactory("ua", DefaultBandwidthMeter()))
                val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
                DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
            }
        }
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
    }

    fun load(videoPath:String, isDataSorce:Boolean = false) {
        val uri = Uri.parse(videoPath)
        val source:MediaSource = if( isDataSorce ) buildDataSource(uri) else buildMediaSource(uri)
        if( player == null ) viewModel.source = source
        player?.let {
            it.prepare(source)
            viewModel.source = null
        }
    }

    fun pause(){
        player?.playWhenReady = false
    }

    fun resume(){
        player?.playWhenReady = true
    }

    fun seek(t:Long){
        player?.seekTo(t)
    }

    override fun onTimelineChanged( timeline: Timeline, manifest: Any? ,@Player.TimelineChangeReason reason: Int){}
    override fun onTracksChanged( trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
    override fun onLoadingChanged(isLoading: Boolean) {}
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {}
    override fun onRepeatModeChanged(@Player.RepeatMode repeatMode: Int) {}
    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
    override fun onPlayerError(error: ExoPlaybackException) {}
    override fun onPositionDiscontinuity(@Player.DiscontinuityReason reason: Int) {}
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
    override fun onSeekProcessed() {}
    override fun onVideoSizeChanged( width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) { }
    override fun onSurfaceSizeChanged(width: Int, height: Int) {}
    override fun onRenderedFirstFrame() {}

}