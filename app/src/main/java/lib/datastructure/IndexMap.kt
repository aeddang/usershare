package lib.datastructure


import java.util.Iterator

class IndexMap<K,V>: Iterator<V>
{
    private val map = HashMap<K,V>()
    private val index = ArrayList<K>()
    private var currentIndex = 0
    var size = 0
        get(){ return index.size }
        private set

    operator fun iterator(): Iterator<V>
    {
        currentIndex = 0
        return this
    }

    fun indexOf(key:K):Int
    {
        return index.indexOf(key)
    }

    override fun hasNext(): Boolean
    {
        val has = if(currentIndex <= index.size) true else false
        return has
    }

    override fun next():V
    {
        val idx = currentIndex
        currentIndex ++
        return get(idx)!!
    }

    fun get(key:K):V?
    {
        return map.get(key)
    }

    fun get(idx:Int):V?
    {
        if(index.isEmpty()) return null
        val key = index.get(idx)
        return map.get(key)
    }

    fun clear()
    {
        map.clear()
        index.clear()
    }

    fun remove(idx:Int)
    {
        val key = index.get(idx)
        remove(key)
    }

    fun remove(key:K)
    {
        map.remove(key)
        index.remove(key)
    }

    fun isEmpty():Boolean
    {
        return index.isEmpty()
    }

    fun put(key:K,value:V):Int
    {
        index.add(key)
        map.put(key,value)
        return index.size-1
    }

    fun poll():V?
    {
        if(index.isEmpty()) return null
        val key = index.first()
        val v =  map.get(key)
        remove(key)
        return v
    }

    fun pop():V?
    {
        if(index.isEmpty()) return null
        val key = index.last()
        val v =  map.get(key)
        remove(key)
        return v
    }
}