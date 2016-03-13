package pl.mareklangiewicz.myintent

import android.os.Bundle
import android.view.View
import pl.mareklangiewicz.myfragments.MyWebFragment

/**
 * Created by Marek Langiewicz on 13.10.15.
 */
class MIHelpFragment : MyWebFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        if(url == "") url = "file:///android_asset/mi.html"

        settings.setUserAgentString(settings.getUserAgentString() + " " + getString(R.string.mi_user_agent_suffix))
        settings.setJavaScriptEnabled(true)

        super.onViewCreated(view, savedInstanceState)
    }
}
