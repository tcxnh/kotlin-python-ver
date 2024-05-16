package furhatos.app.openaichat

import furhatos.app.openaichat.flow.*
import furhatos.app.openaichat.audiofeed.FurhatAudioFeedPlayback
import furhatos.app.openaichat.audiofeed.FurhatAudioFeedRecorder
import furhatos.app.openaichat.audiofeed.FurhatAudioFeedStreamer
import furhatos.skills.Skill
import furhatos.flow.kotlin.*
import furhatos.nlu.LogisticMultiIntentClassifier
import java.io.File
import java.io.IOException

class OpenaichatSkill : Skill() {
    override fun start() {
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {

    //LogisticMultiIntentClassifier.setAsDefault()
    //Skill.main(args)

    //val streamer = FurhatAudioFeedStreamer()
    //streamer.start("localhost")

    /** Choose one of them to record or playback audio */
    //recordAudio(streamer)
    //playbackAudio(streamer)

   // println("Starting streaming, press return to stop.")
    //readlnOrNull()
    //streamer.stop()
    generateVoice()

}

fun recordAudio(streamer: FurhatAudioFeedStreamer) {
    val recorder = FurhatAudioFeedRecorder(streamer)

    /** Choose one of them to record audio in, out or both */
    recorder.startRecordSeparate(audioInFile = File("audioIn.wav"), audioOutFile = File("audioOut.wav"))
}


fun generateVoice() {
    val pythonFilePath = "OpenVoice/execute_notebook.py"

    try {
        // use the python environment contain all the packages in requirements
        val pythonExecutablePath = "/opt/anaconda3/envs/openvoice/bin/python"

        val processBuilder = ProcessBuilder(pythonExecutablePath, pythonFilePath)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        val inputStream = process.inputStream

        // Read the output from the Python script
        val outputBytes = inputStream.readBytes()
        val output = String(outputBytes)
        println("Python script output: $output")

        val exitCode = process.waitFor()
        println("Python script exited with code: $exitCode")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

