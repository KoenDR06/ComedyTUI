package me.koendev.comedytui.components

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.comedytui.config
import me.koendev.comedytui.stateMachine
import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.borders.RoundedSingleBorder
import me.koendev.tuimaker.elements.Box
import java.awt.Color
import java.time.LocalTime
import kotlin.concurrent.thread

class Timer(tui: TUI) {
    private val x = 1
    private val y = 1
    private val width = 103
    private val height = 12

    private var startTimestamp = 0L
    private var runTimer = true
    private var waiting = true
    private var flashing = true

    private val stats = mutableListOf<Pair<String, Int>>()

    private val figlet = FigletFont(Thread.currentThread().contextClassLoader.getResourceAsStream("univers.flf"))
    private val color = config.colors.timer.toColor()

    val box = Box(tui, x, y, width, height, RoundedSingleBorder(), borderColor = color, "Timer", padding = Pair(0,1))

    init {
        box.content = timeToString(0)

        thread(isDaemon = true) {
            // Show the irl time before the show
            while (waiting) {
                box.content = timeToString(LocalTime.now().toSecondOfDay(), true)
                Thread.sleep(100)
            }

            // Show the stopwatch time during the show
            while (runTimer) {
                val millisElapsed = System.currentTimeMillis() - startTimestamp
                val secondsElapsed = millisElapsed / 1000

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

                box.foreColor = foreColor
                box.backColor = backColor
                box.content = timeToString(secondsElapsed.toInt())

                Thread.sleep(100)
            }

            // Show the irl time after the show
            while (true) {
                box.content = timeToString(LocalTime.now().toSecondOfDay(), true)
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

            str = "${h.toString().padStart(2, '0')} ${m.toString().padStart(2, '0')} ${s.toString().padStart(2, '0')}"
        }

        val out = figlet.convert(str)
            .split('\n')
            .filter { it.trim().isNotEmpty() }

        val maxLine = out.maxOf { it.length }

        val innerWidth = width-2*box.padding.first-2

        if (maxLine > innerWidth) throw Exception() //return listOf("","","","",str,"","","").joinToString("\n") { (" ".repeat((96 - str.length ) / 2) + it).padEnd(96, ' ') }

        return out.joinToString("\n") { (" ".repeat((innerWidth - maxLine) / 2) + it) }
    }

    fun getStats(): String {
        runTimer = false

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

    fun loop(loopName: String, startTime: Int = config.musicTime) {
        val loopTime = (System.currentTimeMillis() - startTimestamp) / 1000

        stats.add(Pair(loopName, loopTime.toInt()))

        startTimestamp = System.currentTimeMillis() + startTime * 1000
    }

    fun stopFlashing() {
        flashing = false
    }
}
