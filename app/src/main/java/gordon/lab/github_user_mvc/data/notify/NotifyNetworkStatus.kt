package gordon.lab.github_user_mvc.data.notify

import java.util.concurrent.LinkedBlockingQueue

class NotifyNetworkStatus(val status: Boolean, val targetTask: LinkedBlockingQueue<Runnable>? = null)