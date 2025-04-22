package me.koendev.comedytui

import me.koendev.comedytui.components.CurrentComedian
import me.koendev.comedytui.components.Music
import me.koendev.comedytui.components.Timer

enum class StateMachineState {
    BUILDING,
    OPENING,
    MC,
    COMEDIAN,
    BREAK,
    CLOSING
}

class StateMachine(private val timer: Timer, private val currentComedian: CurrentComedian, private val music: Music) {
    var state = StateMachineState.BUILDING
    var onStage: Comedian? = null

    private var comedianIndex = 0
    private var beforeBreak = true

    fun nextState(): StateMachineState {
        when (state) {
            StateMachineState.BUILDING -> {
                state = StateMachineState.OPENING
                currentComedian.write("Opening")
                music.next()
                music.play()
            }
            StateMachineState.OPENING -> {
                state = StateMachineState.MC
                onStage = config.mc
                currentComedian.write(onStage!!)
                timer.start()
            }
            StateMachineState.MC -> {
                if (comedianIndex == config.breakIndex && beforeBreak) {
                    timer.loop(onStage!!.name, 0)
                    state = StateMachineState.BREAK
                    onStage = null
                    beforeBreak = false
                    currentComedian.write("Break")
                    music.play()
                } else if (comedianIndex < config.comedians.size) {
                    timer.loop(onStage!!.name)
                    state = StateMachineState.COMEDIAN
                    onStage = config.comedians[comedianIndex++]
                    currentComedian.write(onStage!!)
                    music.rewind()
                    music.play()
                } else {
                    state = StateMachineState.CLOSING
                    timer.loop(onStage!!.name)
                    onStage = null
                    currentComedian.write("Done", timer.getStats())
                    music.play()
                }
            }
            StateMachineState.COMEDIAN -> {
                state = StateMachineState.MC
                timer.loop(onStage!!.name)
                onStage = config.mc
                currentComedian.write(onStage!!)
                music.rewind()
                music.play()
            }
            StateMachineState.BREAK -> {
                state = StateMachineState.MC
                timer.loop("Break")
                onStage = config.mc
                currentComedian.write(onStage!!)

                music.next()
                music.play()
            }
            StateMachineState.CLOSING -> {
                StateMachineState.CLOSING
            }
        }
        return state
    }
}