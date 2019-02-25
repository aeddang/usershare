package com.kakaovx.homet.lib.page

import java.util.*
import kotlin.collections.ArrayList

class PageModel<T>: Model<T> {
    internal lateinit var homes: Array<T>

    override fun onDestroy() {
    }

    override fun isHome(id:T):Boolean {
        var idx  = homes.indexOf(id)
        return idx != -1
    }

    override fun getHome(idx:Int):T {
        return homes[idx]
    }

}