package me.koendev.comedytui.components

import me.koendev.comedytui.config
import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.borders.RoundedSingleBorder
import me.koendev.tuimaker.elements.Box

class Music(tui: TUI) {
    private val x = 1
    private val width = 103
    private val height = 16
    private val y = tui.height-height+1

    private val color = config.colors.music.toColor()

    val box = Box(tui, x, y, width, height, RoundedSingleBorder(), borderColor = color, "Music")

    fun play() {
        Runtime.getRuntime().exec(arrayOf("playerctl", "pause"))
    }

    fun pause() {
        TODO()
    }

    fun rewind() {

    }
}
