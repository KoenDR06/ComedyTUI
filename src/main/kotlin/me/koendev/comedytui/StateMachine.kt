package me.koendev.comedytui

import me.koendev.comedytui.components.CurrentComedian
import me.koendev.comedytui.components.Timer

enum class StateMachineState {
    BUILDING,
    OPENING,
    MC,
    COMEDIAN,
    BREAK,
    CLOSING
}

class StateMachine(private val timer: Timer, private val currentComedian: CurrentComedian) {
    var state = StateMachineState.BUILDING
    var onStage: Comedian? = null

    private var comedianIndex = 0
    private var beforeBreak = true

    fun nextState(): StateMachineState {
        when (state) {
            StateMachineState.BUILDING -> {
                state = StateMachineState.OPENING
            }
            StateMachineState.OPENING -> {
                state = StateMachineState.MC
                onStage = config.mc
                currentComedian.write(onStage!!)
                timer.start()
            }
            StateMachineState.MC -> {
                if (comedianIndex == config.breakIndex && beforeBreak) {
                    timer.loop(onStage!!.name)
                    state = StateMachineState.BREAK
                    onStage = null
                    beforeBreak = false
                } else if (comedianIndex < config.comedians.size) {
                    timer.loop(onStage!!.name)
                    state = StateMachineState.COMEDIAN
                    onStage = config.comedians[comedianIndex++]
                    currentComedian.write(onStage!!)
                } else {
                    state = StateMachineState.CLOSING
                    timer.loop(onStage!!.name)
                    onStage = null
                    timer.showStats()
                }
            }
            StateMachineState.COMEDIAN -> {
                state = StateMachineState.MC
                timer.loop(onStage!!.name)
                onStage = config.mc
                currentComedian.write(onStage!!)
            }
            StateMachineState.BREAK -> {
                state = StateMachineState.MC
                timer.loop("Break")
                onStage = config.mc
                currentComedian.write(onStage!!)
            }
            StateMachineState.CLOSING -> { // TODO Make the application close here?
                StateMachineState.CLOSING
            }
        }
        return state
    }
}