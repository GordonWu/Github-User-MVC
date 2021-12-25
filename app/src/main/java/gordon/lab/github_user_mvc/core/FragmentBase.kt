package gordon.lab.github_user_mvc.core

import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

open class FragmentBase :Fragment() {
    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onPause() {
        super.onPause()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}