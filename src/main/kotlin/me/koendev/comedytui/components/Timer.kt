package me.koendev.comedytui.components

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.comedytui.CUL
import me.koendev.comedytui.CUR
import me.koendev.comedytui.TUI
import me.koendev.comedytui.config
import java.io.File
import kotlin.concurrent.thread

class Timer(private val tui: TUI, private val x: Int, private val y: Int) {
    private var t = 0
    private var runTimer = true
    private var waiting = true
    private val stats = mutableListOf<Pair<String, Int>>()

    private val color = config.colors.timer.toColor()

    init {
        tui.drawBox(x, y, 100, 12, color)
        tui.write(x+2, y, "$CUR Timer $CUL", color)
        tui.write(x+3, x+2, this.timeToString(0))

        thread(isDaemon = true) {
            while (waiting) {Thread.sleep(10)}
            while (runTimer) {
                tui.write(x+3, y+2, this.timeToString(t++))
                Thread.sleep(1000)
            }
        }
    }

    private fun timeToString(t: Int): String {
        val seconds = t % 60
        val minutes = t / 60

        val str = if (minutes == 0) seconds.toString() else if (seconds == 0) "${minutes}m" else "${minutes}m ${seconds}s"

        val out = FigletFont.convertOneLine("classpath:univers.flf", str)
            .split('\n')
            .filter { it.trim().isNotEmpty() }

        val maxLine = out.maxOf { it.length }

        return out.joinToString("\n") { (" ".repeat((96 - maxLine) / 2) + it).padEnd(96, ' ') }
    }

    fun showStats() {
        runTimer = false
        tui.write(x+3, y+2, List(8) { " ".repeat(98) }.joinToString("\n"))
        tui.write(x+3, y+1, stats.joinToString("\n"))
    }

    fun start() { waiting = false }

    fun loop(loopName: String) {
        val loopTime = t

        stats.add(Pair(loopName, loopTime))

        t = 0
    }
}