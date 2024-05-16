package furhatos.app.openaichat.flow

import furhatos.app.openaichat.flow.chatbot.MainChat
import furhatos.app.openaichat.setting.Persona
import furhatos.app.openaichat.setting.hostPersona
import furhatos.app.openaichat.setting.personas
import furhatos.flow.kotlin.*
import furhatos.records.Location

val Greeting = state(Parent) {

    onEntry {
        furhat.attend(users.userClosestToPosition(Location(0.0, 0.0, 0.5)))
        askForAnything("Hi there")
        furhat.say("I'm here to show you an example of voice cloning.")
        if (furhat.askYN("But before I start, can I tell you some important things about it? ") == true) {
            furhat.say("Please say something to me and I will try to mimic your voice.")
            furhat.say {
                +"An elephant sounds like this"
                +Audio("file:///audioOut.wav", "test audio")}
            if (furhat.askYN("are you ready to try it out?") == true) {
            } else {
                furhat.say("Okay, maybe another time then")
                goto(Idle)
            }
        } else {
            furhat.say("Okay, let's skip right ahead. ")
        }
        goto(ChoosePersona())
    }
}


var currentPersona: Persona = hostPersona

fun ChoosePersona() = state(Parent) {

    val personasWithAvailableVoice = personas.filter { it.voice.first().isAvailable }
    val selectedPersonas = personasWithAvailableVoice.take(3)

    fun FlowControlRunner.presentPersonas() {

        furhat.say("You can choose to speak to one of these characters:")
        for (persona in selectedPersonas) {
            //activate(persona)
            delay(300)
            furhat.say(persona.fullDesc)
            delay(300)
        }
        //activate(hostPersona)
        reentry()
    }

    onEntry {
        presentPersonas()
    }

    onReentry {
        furhat.ask("Who would you like to talk to?")
    }

    onResponse("can you present them again", "could you repeat") {
        presentPersonas()
    }

    for (persona in personas) {
        onResponse(persona.intent) {
            furhat.say {
                random {
                    +"Okay, I will let you talk to ${persona.name}."
                    +"Okay, let's have a chat with ${persona.name}."
                    +"Sure, we can talk to ${persona.name}."
                }
                random {
                    +"When you want to end the conversation, just say, goodbye."
                    +"When it's time to end the conversation, just say, goodbye."
                    +"When you say the word, goodbye, the interaction will stop. "
                }
            }
            currentPersona = persona
            goto(MainChat)
        }
    }
}