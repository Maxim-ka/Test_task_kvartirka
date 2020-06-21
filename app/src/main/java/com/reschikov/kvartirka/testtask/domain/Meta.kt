package com.reschikov.kvartirka.testtask.domain

data class Meta(val offset : Int, val nearest : Int, val limit: Int){

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Meta) return false
        return other.offset == offset && other.nearest == nearest && other.limit == limit
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}