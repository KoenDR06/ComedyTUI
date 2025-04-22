package me.koendev.comedytui.components

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.comedytui.*
import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.borders.RoundedSingleBorder
import me.koendev.tuimaker.elements.Box

class CurrentComedian(tui: TUI) {
    private val x = 1
    private val y = 13
    private val width = 103
    private val height = tui.height-y+1-16

    private val color = config.colors.current.toColor()
    private val figlet = FigletFont(Thread.currentThread().contextClassLoader.getResourceAsStream("big.flf"))

    val box = Box(tui, x, y, width, height, RoundedSingleBorder(), borderColor = color, "Current comedian", padding = Pair(1,0))

    fun write(comedian: Comedian) {
        val notes = """
            Wants a light at: ${ if (comedian.lampTime % 1.0 == 0.0) comedian.lampTime.toInt() else comedian.lampTime }
            Plays for: ${ if (comedian.totalTime % 1.0 == 0.0) comedian.totalTime.toInt() else comedian.totalTime  } 
        """.trimIndent() + "\n\n" + comedian.notes

//        box.content = name + "\n\n" + notes
        this.write(comedian.name, notes)
    }

    fun write(header: String, notes: String = "") {
        box.content = figlet.convert(header)
            .split('\n')
            .filter { it.trim().isNotEmpty() }
            .joinToString("\n") { it.padEnd(width-2*box.padding.first, ' ').substring(0..< width-2*box.padding.first-3) }

        box.content += "\n".repeat(10-box.content.split('\n').size) + notes
    }
}
