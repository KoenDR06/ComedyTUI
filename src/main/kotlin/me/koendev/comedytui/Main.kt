package me.koendev.comedytui

import me.koendev.comedytui.components.CurrentComedian
import me.koendev.comedytui.components.Timer
import me.koendev.tuimaker.TUI

lateinit var stateMachine: StateMachine

fun main(args: Array<String>) {
    val tui = TUI(103, args[0].toInt())

    val timer = Timer(tui)
    tui.elements.add(timer.box)

    val currentComedian = CurrentComedian(tui)
    tui.elements.add(currentComedian.box)

    tui.eventHandler = { char ->
        when (char) {
            'q' -> throw Exception()
            'n' -> stateMachine.nextState()
            'd' -> timer.stopFlashing()
            else -> {}
        }
    }

    currentComedian.write("Building")
    stateMachine = StateMachine(timer, currentComedian)

    tui.show()


//    val currentComedian = CurrentComedian(tui, 1, 13)
//    val musicProvider = MusicProvider(tui, 1, 1)
//    stateMachine = StateMachine(timer, currentComedian)
//
//    currentComedian.write("Building")
//
//    while (true) {
//        val char = System.`in`.read().toChar()
//        print('\b')
//        when (char) {
//            'q' -> break
//            'n' -> stateMachine.nextState()
//            'd' -> timer.stopFlashing()
//            else -> {}
//        }
//    }
//
//    Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw </dev/tty"))
//    tui.shutdown()
}