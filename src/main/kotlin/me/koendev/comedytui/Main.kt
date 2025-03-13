package me.koendev.comedytui

import me.koendev.comedytui.components.CurrentComedian
import me.koendev.comedytui.components.Timer

fun main() {
    val tui = TUI()
    val timer = Timer(tui, 1, 1)
    val currentComedian = CurrentComedian(tui, 1, 13)
    val stateMachine = StateMachine(timer, currentComedian)

    currentComedian.write(config.comedians[3])

    /*while (stateMachine.state != StateMachineState.CLOSING) {
        Thread.sleep(5000)
        stateMachine.nextState()
    }*/

    readln()

    tui.shutdown()
}