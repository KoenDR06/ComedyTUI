package me.koendev.comedytui

import de.thelooter.toml.Toml
import java.awt.Color
import java.io.File

data class Comedian(val name: String, val lampTime: Double, val totalTime: Double)
private data class Config(val mc: Comedian, val comedians: List<Comedian>)

lateinit var comedians: List<Comedian>

fun main() {
    val toml = Toml().read(File("config.toml")).to(Config::class.java)
    comedians = toml.comedians.fold(listOf(toml.mc)) { acc, curr -> acc + listOf(curr, toml.mc) }

    val tui = TUI()
    Timer(tui, 1, 1)

    Thread.sleep(100100)

    tui.shutdown()
}