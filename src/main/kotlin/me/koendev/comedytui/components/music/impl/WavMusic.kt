package me.koendev.comedytui.components.music.impl

import me.koendev.comedytui.components.music.Music
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.AudioSystem.getAudioInputStream
import javax.sound.sampled.Clip


class WavMusic(private var song: File): Music {
    private var player: Clip = AudioSystem.getClip().also { it.open(getAudioInputStream(song)) }

    override fun play() {
        player.start()
    }

    override fun chooseSong(name: String) {
        player.close()
        player = AudioSystem.getClip().also { it.open(getAudioInputStream(File(name))) }
    }

    override fun pause() {
        player.stop()
    }

    override fun rewind() {
        player.stop()
        player = AudioSystem.getClip().also { it.open(getAudioInputStream(song)) }
        player.start()
    }

}