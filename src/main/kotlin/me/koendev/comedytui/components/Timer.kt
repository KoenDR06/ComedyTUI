package me.koendev.comedytui.components

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.comedytui.TUI
import me.koendev.comedytui.config
import me.koendev.comedytui.stateMachine
import java.awt.Color
import java.time.LocalTime
import kotlin.concurrent.thread

class Timer(private val tui: TUI, private val x: Int, private val y: Int) {
    private var startTimestamp = 0L
    private var runTimer = true
    private var waiting = true
    private var flashing = true

    private val stats = mutableListOf<Pair<String, Int>>()

    private val figlet = FigletFont(Thread.currentThread().contextClassLoader.getResourceAsStream("univers.flf"))
    private val color = config.colors.timer.toColor()

    init {
        tui.drawBox(x, y, 100, 12, color, header="Timer")
        tui.write(x+3, x+2, this.timeToString(0))

        thread(isDaemon = true) {
            // Show the irl time before the show
            while (waiting) {
                tui.write(x+2, y+2, this.timeToString(LocalTime.now().toSecondOfDay(), true))
                Thread.sleep(100)
            }

            // Show the stopwatch time during the show
            while (runTimer) {
                val millisElapsed = System.currentTimeMillis() - startTimestamp
                val secondsElapsed = millisElapsed / 100

                var foreColor: Color
                var backColor: Color

                if (stateMachine.onStage == null) {
                    foreColor = Color.WHITE
                    backColor = Color.BLACK
                } else if (secondsElapsed > stateMachine.onStage!!.lampTime*60 && flashing) {
                    foreColor = if (millisElapsed % 1000 < 200) Color.BLACK else Color.WHITE
                    backColor = if (millisElapsed % 1000 < 200) Color.WHITE else Color.BLACK
                } else {
                    foreColor = Color.WHITE
                    backColor = Color.BLACK

                    flashing = flashing || secondsElapsed < stateMachine.onStage!!.lampTime*60
                }

                tui.write(x+2, y+2, this.timeToString(secondsElapsed.toInt()), foreColor, backColor)
                Thread.sleep(100)
            }

            // Show the irl time after the show
            while (true) {
                tui.write(x+2, y+2, this.timeToString(LocalTime.now().toSecondOfDay(), true))
                Thread.sleep(100)
            }
        }
    }

    private fun timeToString(t: Int, realTime: Boolean = false): String {
        val str: String

        if (!realTime) {
            val seconds = t % 60
            val minutes = t / 60
            str = if (minutes == 0) seconds.toString() else if (seconds == 0) "${minutes}m" else "${minutes}m ${seconds}s"
        } else {
            val h = t / 3600
            val m = (t - (h * 3600)) / 60
            val s = t % 60

            str = "${h.toString().padStart(2, '0')}: ${m.toString().padStart(2, '0')}: ${s.toString().padStart(2, '0')}"
        }

        val out = figlet.convert(str)
            .split('\n')
            .filter { it.trim().isNotEmpty() }

        val maxLine = out.maxOf { it.length }

        if (maxLine > 96) return listOf("","","","",str,"","","").joinToString("\n") { (" ".repeat((96 - str.length ) / 2) + it).padEnd(96, ' ') }

        return out.joinToString("\n") { (" ".repeat((96 - maxLine) / 2) + it).padEnd(96, ' ') }
    }

    fun getStats(): String {
        runTimer = false
        tui.clearBox(x+1,y+1,98, 10)

        var str = "Time      Name\n\n"

        for ((name, time) in stats) {
            val seconds = time % 60
            val minutes = time / 60
            val timeStr = "${minutes.toString().padStart(2, '0')}m ${seconds.toString().padStart(2, '0')}s"

            str += timeStr.padEnd(10) + name + "\n"
        }

        return str
    }


    fun start() {
        waiting = false
        startTimestamp = System.currentTimeMillis()
        flashing = true
    }

    fun loop(loopName: String) {
        val loopTime = (System.currentTimeMillis() - startTimestamp) / 1000

        stats.add(Pair(loopName, loopTime.toInt()))

        startTimestamp = System.currentTimeMillis()
    }

    fun stopFlashing() {
        flashing = false
    }
}
