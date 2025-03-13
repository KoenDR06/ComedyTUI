package me.koendev.comedytui

import de.thelooter.toml.Toml
import java.awt.Color
import java.io.File

data class Comedian(
    val name: String,
    val lampTime: Double,
    val totalTime: Double,
    val notes: String
)

data class TOMLColor(
    val r: Int,
    val g: Int,
    val b: Int
) {
    fun toColor() = Color(r,g,b)
}

data class Colors(
    val timer: TOMLColor,
    val current: TOMLColor
)

data class Config(
    val mc: Comedian,
    val comedians: List<Comedian>,
    val breakIndex: Int,
    val colors: Colors
)

val config: Config = Toml().read(File("config.toml")).to(Config::class.java)