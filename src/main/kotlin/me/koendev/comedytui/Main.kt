package me.koendev.comedytui

import me.koendev.comedytui.components.CurrentComedian
import me.koendev.comedytui.components.Timer
import me.koendev.comedytui.components.music.impl.WavMusicProvider

lateinit var stateMachine: StateMachine

fun main(args: Array<String>) {
    Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw </dev/tty"))

    val tui = TUI(args[1].toInt(), args[0].toInt())
    val timer = Timer(tui, 1, 1)
    val currentComedian = CurrentComedian(tui, 1, 13)
    val musicProvider = WavMusicProvider(tui, 101, 40)
    stateMachine = StateMachine(timer, currentComedian, musicProvider)

    currentComedian.write("Building")

    while (true) {
        val char = System.`in`.read().toChar()
        print('\b')
        when (char) {
            'q' -> break
            'n' -> stateMachine.nextState()
            'd' -> timer.stopFlashing()
            else -> {}
        }
    }

    Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw </dev/tty"))
    tui.shutdown()
}