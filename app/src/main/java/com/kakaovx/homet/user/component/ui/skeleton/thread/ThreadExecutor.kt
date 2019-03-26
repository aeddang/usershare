package com.kakaovx.homet.user.component.ui.skeleton.thread
import java.util.concurrent.Executor

interface ThreadExecutor : Executor{
    fun shutdown( isSafe:Boolean = false )
}