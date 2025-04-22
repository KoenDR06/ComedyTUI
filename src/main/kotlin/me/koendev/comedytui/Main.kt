package me.koendev.comedytui

import me.koendev.comedytui.components.CurrentComedian
import me.koendev.comedytui.components.Music
import me.koendev.comedytui.components.Timer
import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.CloseException
import kotlin.system.exitProcess

lateinit var stateMachine: StateMachine

fun main(args: Array<String>) {
    val tui = TUI(103, args[0].toInt())

    val timer = Timer(tui)
    val currentComedian = CurrentComedian(tui)
    val music = Music(tui)

    tui.elements.add(timer.box)
    tui.elements.add(currentComedian.box)
    tui.elements.add(music.box)

    tui.eventHandler = { char ->
        when (char) {
            'q' -> throw CloseException()
            'n' -> stateMachine.nextState()
            'd' -> timer.stopFlashing()
            'p' -> music.pause()
            else -> {}
        }
    }

    currentComedian.write("Pre - Show")
    stateMachine = StateMachine(timer, currentComedian, music)

    try {
        tui.show()
    } catch (e: Exception) {
        tui.close()

        throw e
    }
}
