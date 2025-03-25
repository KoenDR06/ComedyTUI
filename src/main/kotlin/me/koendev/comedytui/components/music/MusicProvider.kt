package me.koendev.comedytui.components.music

import me.koendev.comedytui.TUI
import me.koendev.comedytui.config
import java.awt.Color
import kotlin.concurrent.thread
import kotlin.math.floor
import kotlin.math.sin

abstract class MusicProvider(private val tui: TUI, private val x: Int, private val y: Int) {
    private var _currentSongName = ""
    internal var currentSongName
        set(value) {
            tui.write(x+27, y+2, value)
            _currentSongName = '"' + value + '"'
        }
        get() = _currentSongName

    private var runAnimation = false

    open fun pause()  { runAnimation = false }
    open fun play() { runAnimation = true }
    abstract fun play(name: String)
    abstract fun rewind()
    abstract fun chooseSong(name: String)

    private val color = config.colors.music.toColor()

    init {
        tui.drawBox(x, y, tui.width-x+1, tui.height-y+1, color, header="Music")
        tui.write(x+3, y+2, """
            Current song queued up:
            """.trimIndent())

        thread(isDaemon = true) {
            val n = tui.width-x-5
            val height = tui.height-y-4
            val stringify: (d: Double) -> String = fun(d: Double): String {
                val rest = when {
                    d*height % 1.0 < 0.125 -> ' '
                    d*height % 1.0 < 0.250 -> '▁'
                    d*height % 1.0 < 0.375 -> '▂'
                    d*height % 1.0 < 0.500 -> '▃'
                    d*height % 1.0 < 0.625 -> '▄'
                    d*height % 1.0 < 0.750 -> '▅'
                    d*height % 1.0 < 0.875 -> '▆'
                    d*height % 1.0 < 1.000 -> '▇'
                    else -> '█'
                }
                return ("█".repeat(floor(d*height).toInt()) + rest).reversed().padStart(height).chunked(1).joinToString("\n") //▁
            }

            var t = 0.0
            while (true) {
                for (i in 0..< n) {
                    if (runAnimation) {
                        tui.write(x+3+i, y+4, stringify((0.4*sin(0.1*i - 0.03*t) + 0.5)), Color(0,128,0))
                    } else {
                        tui.write(x+3+i, y+4, stringify(0.2*sin(0.1*i - 0.03*t) + 0.5), Color.DARK_GRAY)
                    }
                }
                t += if (runAnimation) 3.0 else 0.3
                Thread.sleep(100)
            }
        }
    }
}