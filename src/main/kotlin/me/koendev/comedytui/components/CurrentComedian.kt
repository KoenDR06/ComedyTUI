package me.koendev.comedytui.components

import com.github.lalyos.jfiglet.FigletFont
import me.koendev.comedytui.*

class CurrentComedian(private val tui: TUI, private val x: Int, private val y: Int) {
    private val w = 100
    private val h = tui.height-y+1

    private val color = config.colors.current.toColor()

    init {
        tui.drawBox(x, y, 100, tui.height-y+1, color, header="Current comedian")
    }

    fun write(comedian: Comedian) {
        // Name
        val str = FigletFont.convertOneLine("starwars.flf", comedian.name)
            .split('\n')
            .filter { it.trim().isNotEmpty() }
            .joinToString("\n") { it.padEnd(96, ' ').substring(0..95) }

        tui.clearBox(x+1, y+1, w-2, h-2)
        tui.write(x+3, y+1, str)

        val content = """
        Wants a light at: ${ if (comedian.lampTime % 1.0 == 0.0) comedian.lampTime.toInt() else comedian.lampTime }
        Plays for: ${ if (comedian.totalTime % 1.0 == 0.0) comedian.totalTime.toInt() else comedian.totalTime  } 
        """.trimIndent() + "\n\n" + comedian.notes

        var notesStr = ""
        for (line in content.split("\n")) {
            notesStr += line.chunked(94).joinToString("\n")+"\n"
        }
        tui.write(x+3, y+8, notesStr)
    }

    fun write(header: String, notes: String = "") {
        val str = FigletFont.convertOneLine("starwars.flf", header)
            .split('\n')
            .filter { it.trim().isNotEmpty() }
            .joinToString("\n") { it.padEnd(96, ' ').substring(0..95) }

        tui.clearBox(x+1, y+1, w-2, h-2)
        tui.write(x+3, y+1, str)

        var notesStr = ""
        for (line in notes.split("\n")) {
            notesStr += line.chunked(94).joinToString("\n")+"\n"
        }
        tui.write(x+3, y+8, notesStr)
    }
}