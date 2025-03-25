package me.koendev.comedytui.components.music.impl

import me.koendev.comedytui.TUI
import me.koendev.comedytui.components.music.MusicProvider
import me.koendev.comedytui.config
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.AudioSystem.getAudioInputStream
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import kotlin.concurrent.thread
import java.lang.Math.clamp

class WavMusicProvider(tui: TUI, x: Int, y: Int): MusicProvider(tui, x, y) {
    private var song: File? = null
    private var player: Clip = AudioSystem.getClip()

    override fun play() {
        super.play()
        player.start()
    }

    override fun play(name: String) {
        thread(isDaemon = true) {
            chooseSong(name)
            play()
            Thread.sleep(config.musicTime * 1000L)
            val gainControl = (player.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl)
            for (i in 0..797) {
                gainControl.value = clamp(gainControl.value - 0.1f, -80f, 0.0f)
                Thread.sleep(5)
            }
            pause()
        }
    }

    override fun chooseSong(name: String) {
        currentSongName = name.split("/").last().takeWhile { it != '.' }
        player.close()
        player = AudioSystem.getClip().also { it.open(getAudioInputStream(File(name))) }
    }

    override fun pause() {
        super.pause()
        player.stop()
    }

    override fun rewind() {
        player.stop()
        player = AudioSystem.getClip().also { it.open(getAudioInputStream(song)) }
        player.start()
    }
}