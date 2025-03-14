package me.koendev.comedytui.components

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.comedytui.*

class CurrentComedian(private val tui: TUI, private val x: Int, private val y: Int) {
    private val color = config.colors.current.toColor()

    init {
        tui.drawBox(x, y, 100, tui.height-y+1, color)
        tui.write(x+2, y, "$CUR Current comedian $CUL", color)
    }

    fun write(comedian: Comedian) {
        // Name
        val str = FigletFont.convertOneLine("starwars.flf", comedian.name)
            .split('\n')
            .filter { it.trim().isNotEmpty() }
            .joinToString("\n") { it.padEnd(96, ' ').substring(0..95) }

        tui.write(x+3, y+1, str)

        tui.write(x+3, y+8,"""
            Wants a light at: ${ if (comedian.lampTime % 1.0 == 0.0) comedian.lampTime.toInt() else comedian.lampTime }
            Plays for: ${ if (comedian.totalTime % 1.0 == 0.0) comedian.totalTime.toInt() else comedian.totalTime  } 
            
            ${comedian.notes.chunked(94).joinToString("\n")}""".trimIndent()
        )
    }

    fun clear() {
        tui.write(x+1, y+1, List(tui.height-y-1) { " ".repeat(98) }.joinToString("\n"))
    }
}
