package me.koendev.comedytui.components

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.comedytui.config
import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.borders.RoundedSingleBorder
import me.koendev.tuimaker.elements.Box
import java.awt.Color
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread


class Music(tui: TUI) {
    private val x = 1
    private val width = 103
    private val height = 16
    private val y = tui.height-height+1

    private val color = config.colors.music.toColor()

    val box = Box(tui, x, y, width, height, RoundedSingleBorder(), borderColor = color, "Music")

    init {
        thread(isDaemon = true) {
            while (true) {
                box.content = "$currentSong ($position / $length)" +

                if (playing) {
                    "${TUI.setForegroundColor(Color.GREEN)}\n"+
"""
 _____  _             _
|  __ \| |           (_)
| |__) | | __ _ _   _ _ _ __   __ _
|  ___/| |/ _` | | | | | '_ \ / _` |
| |    | | (_| | |_| | | | | | (_| |
|_|    |_|\__,_|\__, |_|_| |_|\__, |
                 __/ |         __/ |
                |___/         |___/
"""
                } else ""
                Thread.sleep(250)
            }
        }
    }

    fun play() {
        Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "play"))
    }

    fun pause() {
        Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "pause"))
    }

    fun rewind() {
        Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "position", "0"))
    }

    fun next() {
        Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "next"))
    }

    private val playing: Boolean
        get() {
            val launched = Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "status"))
            var output = ""
            BufferedReader(InputStreamReader(launched.inputStream)).use { inputStream ->
                while (true) {
                    val line = inputStream.readLine() ?: break
                    output = line
                }
            }
            return output == "Playing"
        }

    private val currentSong: String
        get() {
            val launched = Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "metadata", "title"))
            var title = ""
            BufferedReader(InputStreamReader(launched.inputStream)).use { inputStream ->
                while (true) {
                    val line = inputStream.readLine() ?: break
                    title = line
                }
            }
            return title
        }

    private val position: String
        get() {
            val launched = Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "position"))
            var position = 0
            BufferedReader(InputStreamReader(launched.inputStream)).use { inputStream ->
                while (true) {
                    val line = inputStream.readLine() ?: break
                    position = line.toDouble().toInt()
                }
            }

            return "${position/60}:${(position%60).toString().padStart(2, '0')}"
        }

    private val length: String
        get() {
            val launched = Runtime.getRuntime().exec(arrayOf("playerctl", "-p", "spotify", "metadata", "mpris:length"))
            var length = 0
            BufferedReader(InputStreamReader(launched.inputStream)).use { inputStream ->
                while (true) {
                    val line = inputStream.readLine() ?: break
                    length = line.toDouble().toInt() / 1000000
                }
            }

            return "${length/60}:${(length%60).toString().padStart(2, '0')}"
        }
}