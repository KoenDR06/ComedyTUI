package me.koendev.comedytui.components.music

interface Music {
    fun play()
    fun pause()
    fun rewind()
    fun chooseSong(name: String)
}