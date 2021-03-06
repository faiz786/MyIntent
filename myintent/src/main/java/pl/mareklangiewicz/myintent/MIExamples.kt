package pl.mareklangiewicz.myintent

import pl.mareklangiewicz.myutils.DEFAULT_EXAMPLE_COMMANDS
import pl.mareklangiewicz.myutils.DEFAULT_EXAMPLE_RULES
import pl.mareklangiewicz.myutils.RERule
import java.util.ArrayList


/**
 * Created by Marek Langiewicz on 31.10.15.
 */
object MIExamples {

    val EXAMPLE_COMMANDS: MutableList<String> = ArrayList<String>().apply {
        addAll(DEFAULT_EXAMPLE_COMMANDS)
        add("settings bluetooth")
        add("settings roaming")
        add("settings display")
        add("settings internal storage")
        add("settings location")
        add("settings apps")
        add("settings memory card")
        add("settings network")
        add("settings nfc")
        add("settings privacy")
        add("settings search")
        add("settings security")
        add("settings sound")
        add("settings sync")
        add("settings wi-fi")
        add("settings wireless")
        add("settings")
        add("start custom action listen")
        add("start custom action say data hello world!")
        add("say have a nice day!")
        add("say something funny")
        add("say something smart")
        add("say something positive")
        add("say something motivational")
        add("say time")
        add("say date")
        add("play some drums")
        add("play some more drums")
        add("teleport to beach")
        add("teleport to new york")
        add("take me to my house")
        add("take me to woodstock")
        add("data google.navigation:q=wroclaw")
        add("data geo:0,0?q=mount+everest")
        add("my name is john")
        add("what's your name")
        add("hey you")
        add("silence")
        add("translate duck")
        add("weather warsaw today")
        add("weather new york tomorrow")
        add("weather 1 jelcz laskowice")
        add("weather 5 jelcz laskowice")
        add("weather 9 san francisco")
        add("exit")
        add("action edit data content://contacts/people/1")
        add("action show alarms")
        add("action insert type contacts")
        add("action insert data calendar events")
        add("action insert data calendar events extra string title iron maiden concert extra string eventLocation stadion wroclaw poland")
        add("action insert type contacts extra string name Satan extra string phone 666")
        add("action main category music")
        add("action main category browser")
        add("action main category calculator")
        add("action main category calendar")
        add("action main category contacts")
        add("action main category email")
        add("action main category gallery")
        add("action main category maps")
        add("action main category market")
        add("action main category messaging")
    }

    val EXAMPLE_RULES: MutableList<RERule> = ArrayList<RERule>().apply {
        addAll(DEFAULT_EXAMPLE_RULES)
        add(RERule("^play (some )?drums", "type application/ogg data http://mareklangiewicz.pl/homepage_2007/muzyka/hydrokoza.ogg", "", "", true))
        add(RERule("^play (some )?more drums", "type application/ogg data http://mareklangiewicz.pl/homepage_2007/muzyka/drum.ogg", "", "", true))
        add(RERule("^take me to( the | a | )", "teleport to ", "", "", true))
        add(RERule("^teleport to beach$", "data google.streetview:cbll=-23.3036925,151.9150093", "", "", true))
        add(RERule("^teleport to new york$", "data google.streetview:cbll=40.7584954,-73.9851351", "", "", true))
        add(RERule("^teleport to my house$", "data google.streetview:cbll=48.1848573,16.3122329", "", "", true))
        add(RERule("^teleport to woodstock$", "data https://www.youtube.com/watch?v=mscYptbJrkA", "", "", true))
        add(RERule("^translate (.*)$", "action send component com.google.android.apps.translate/.TranslateActivity extra text $1", "", "", true))
        add(RERule("^weather (.*) today$", "weather 1 $1", "", "", true))
        add(RERule("^weather (.*) tomorrow$", "weather 2 $1", "", "", true))
        add(RERule("^weather (\\d+)( in | at | )(.*)$", "start custom action weather extra string appid 8932d2a1192be84707c381df649a2925 "
                        + "extra string city $3 extra string units metric extra integer day $1", "", "", true))
        add(RERule("^my name is (\\w+)\\b.*", "say Hi $1.", "", "", true))
        add(RERule("^what's your name\\b.*", "say My name is 4. Nexus 4.", "", "", true))
        add(RERule("^hey you\\b.*", "say Are you talking to me?", "", "", true))
        add(RERule("^silence\\b.*", "say I kill you!", "", "", true))
        add(RERule("^say the time", "say time", "", "", true))
        add(RERule("^what time is it", "say time", "", "", true))
        add(RERule("^say ", "start custom action say data ", "say", "", true))
        add(RERule("^about$", "fragment pl.mareklangiewicz.myintent.MIAboutFragment", "about", "", true))
        add(RERule("^(exit|quit|finish)\\b", "start custom action exit", "exit", "", true))
    }
}
