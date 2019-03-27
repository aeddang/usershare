package com.kakaovx.homet.lib.page

class PageModel<T> : Model<T> {
    internal lateinit var homes: Array<T>
    internal var backStacks: Array<T>? = null
    override var currentPage: T? = null
    override fun onDestroy() {
    }

    override fun isHome(id:T):Boolean {
        var idx  = homes.indexOf(id)
        return idx != -1
    }

    override fun getHome(idx:Int):T {
        return homes[idx]
    }

    override fun isBackStack(id:T):Boolean {
        if( backStacks == null) return false
        var idx  = backStacks!!.indexOf(id)
        return idx != -1
    }

}