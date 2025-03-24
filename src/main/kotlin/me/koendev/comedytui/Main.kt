package me.koendev.comedytui

import me.koendev.comedytui.components.music.impl.WavMusic
import java.io.File

lateinit var stateMachine: StateMachine

fun main(args: Array<String>) {
    val music = WavMusic(File("audio/moveOnUp.wav"))

    music.play()
    Thread.sleep(1000)
    music.rewind()
    Thread.sleep(1000)
    music.play()

    music.chooseSong("audio/boogieWonderland.wav")
    music.play()
    Thread.sleep(10000)

    /*Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw </dev/tty"))

    val tui = TUI(args[1].toInt(), args[0].toInt())
    val timer = Timer(tui, 1, 1)
    val currentComedian = CurrentComedian(tui, 1, 13)
    stateMachine = StateMachine(timer, currentComedian)

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
    tui.shutdown()*/
}