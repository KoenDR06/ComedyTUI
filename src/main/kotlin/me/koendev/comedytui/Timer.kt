package me.koendev.comedytui

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.utils.times
import java.awt.Color
import java.io.File
import kotlin.concurrent.thread

class Timer(private val tui: TUI, x: Int, y: Int) {
    init {
        tui.drawBox(x, y, 100, 12, Color.CYAN)
        thread(isDaemon = true) {
            var t = 0
            while (true) {
                tui.write(4, 3, this.getTime(t++))
                Thread.sleep(1000)
            }
        }
    }

    private fun getTime(t: Int): String {
        val seconds = t % 60
        val minutes = t / 60

        val str = if (minutes == 0) seconds.toString() else if (seconds == 0) "${minutes}m" else "${minutes}m ${seconds}s"

        val out = FigletFont.convertOneLine(File("src/main/resources/univers.flf"), str)
            .split('\n')
            .filter { it.trim().isNotEmpty() }

        val maxLine = out.maxOf { it.length }

        return out.joinToString("\n") { (' ' * ((96 - maxLine) / 2) + it).padEnd(96, ' ') }
    }
}