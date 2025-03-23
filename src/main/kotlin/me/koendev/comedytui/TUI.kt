package me.koendev.comedytui

import java.awt.Color

const val HOR = '═'
const val VER = '║'
const val CUL = '╔'
const val CUR = '╗'
const val CDL = '╚'
const val CDR = '╝'

class TUI(val width: Int, val height: Int) {
    init { print("\u001b[H\u001B[3J\u001b[?25l") }

    private fun moveCursor(x: Int, y: Int) = "\u001b[$y;${x}H"

    private fun setForegroundColor(c: Color) = "\u001B[38;2;${c.red};${c.green};${c.blue};m"
    private fun setBackgroundColor(c: Color) = "\u001B[48;2;${c.red};${c.green};${c.blue};m"
    private fun resetColor() = "\u001B[0m"

    fun shutdown() = print(resetColor()+"\u001b[H\u001B[3J\u001b[?25h")

    fun drawBox(x: Int, y: Int, w: Int, h: Int, foreColor: Color? = null, backColor: Color? = null, header: String? = null) {
        if (x < 1 || x-1+w > width || y < 1 || y-1+h > height ) throw Exception("Box falls (partly) out of bounds")

        var str = (if (foreColor != null) setForegroundColor(foreColor) else "") +
                  (if (backColor != null) setBackgroundColor(backColor) else "") +
                  moveCursor(x, y) +
                  CUL + HOR.toString().repeat(w-2) + CUR

        for (line in y + 1..<y + h - 1) {
            str += moveCursor(x, line) + VER +
                    moveCursor(x + w - 1, line) + VER
        }

        str += moveCursor(x, y + h - 1) + CDL + HOR.toString().repeat(w-2) + CDR

        print(str + resetColor())

        write(x+2, y, "$CUR $header $CUL", foreColor)
    }

    fun clearBox(x: Int, y: Int, w: Int, h: Int) {
        if (x < 1 || x-1+w > width || y < 1 || y-1+h > height ) throw Exception("Box falls (partly) out of bounds")

        write(x, y, List(h) { " ".repeat(w) }.joinToString("\n"))
    }

    fun write(x: Int, y: Int, str: String, foreColor: Color? = null, backColor: Color? = null) {
        var output =
            (if (foreColor != null) setForegroundColor(foreColor) else "") +
            (if (backColor != null) setBackgroundColor(backColor) else "") +
            moveCursor(x, y)

        for ((i, line) in str.split('\n').withIndex()) {
            output += line + moveCursor(x, y+i+1)
        }
        print(output + resetColor())
    }
}