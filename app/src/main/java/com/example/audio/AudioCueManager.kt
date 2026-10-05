package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import kotlin.math.exp
import kotlin.math.sin

class AudioCueManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    // Pre-computed PCM audio buffer for the 528 Hz + 1056 Hz harmonic chime
    private val chimeBuffer: ShortArray by lazy {
        generateChimePcm()
    }

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.let { engine ->
                    val result = engine.setLanguage(Locale.US)
                    if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                        engine.setSpeechRate(0.9f) // Calm, deliberate pace as in HTML
                        engine.setPitch(1.0f)
                        isTtsReady = true
                    }
                }
            }
        }
    }

    /**
     * Synthesizes the dual-frequency Solfeggio 528 Hz & 1056 Hz harmonic overtone chime
     * with exponential gain decay ramp over 1.2 seconds, identical to the Web Audio API implementation.
     */
    private fun generateChimePcm(): ShortArray {
        val sampleRate = 44100
        val durationSeconds = 1.2
        val totalSamples = (sampleRate * durationSeconds).toInt()
        val pcm = ShortArray(totalSamples)

        // Web Audio exponential ramp: starts at 0.25 (idx 0) and 0.08 (idx 1), decays to 0.0001
        // decay rate: exp(-6.519 * t)
        val decayRate = 6.519

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            val decay = exp(-decayRate * t)
            val osc1 = 0.25 * sin(2.0 * Math.PI * 528.0 * t)
            val osc2 = 0.08 * sin(2.0 * Math.PI * 1056.0 * t)
            val sample = (osc1 + osc2) * decay
            pcm[i] = (sample * 32767.0).coerceIn(-32768.0, 32767.0).toInt().toShort()
        }
        return pcm
    }

    fun playChime() {
        try {
            val buffer = chimeBuffer
            val sampleRate = 44100
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
        } catch (e: Exception) {
            Log.e("AudioCueManager", "Failed to play chime", e)
        }
    }

    fun speak(text: String) {
        if (isTtsReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "cue_${System.currentTimeMillis()}")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
