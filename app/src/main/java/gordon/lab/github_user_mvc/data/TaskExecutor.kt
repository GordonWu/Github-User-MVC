package gordon.lab.github_user_mvc.data

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import gordon.lab.github_user_mvc.core.GithubApp
import gordon.lab.github_user_mvc.data.notify.NotifyNetworkStatus
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class TaskExecutor {

    companion object {
        private var sInstance: TaskExecutor? = null

        fun getInstance(): TaskExecutor {
            if (null == sInstance)
                sInstance = TaskExecutor()

            return sInstance!!
        }

        fun shutdown() {
            if (null == sInstance)
                return

            sInstance?.stop()
            sInstance = null
        }
    }

    private var mThreadPool: ThreadPoolExecutor? = null
    private var mQueue: LinkedBlockingQueue<Runnable>? = null
    private var mNetErrorQueue: LinkedBlockingQueue<Runnable>? = null

    init {
        val nNumOfCores = Runtime.getRuntime().availableProcessors()
        mNetErrorQueue = LinkedBlockingQueue()
        mQueue = LinkedBlockingQueue()
        mThreadPool = ThreadPoolExecutor(nNumOfCores, nNumOfCores, 1, TimeUnit.SECONDS, mQueue)
    }

    private fun isConnected(): Boolean {
        val cm = GithubApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


    fun execute(task: TaskBase?) {
        if (null == task) {
            return
        }
        try {
            if (!isConnected()) {
                mNetErrorQueue?.add(task)
                EventBus.getDefault().post(NotifyNetworkStatus(!isConnected(), mNetErrorQueue!!))
                mNetErrorQueue = LinkedBlockingQueue()
            } else {
                mThreadPool?.execute(task)
            }
        } catch (ex: Exception) {
            // 當使用者快速建立一堆 tasks，但卻關閉可能造成會死，因此做保護而不做 recovery
            Log.e("gw", "exception:" + ex.message)
        }
    }

    fun remove(task: TaskBase?) {
        if (null == task)
            return

        mThreadPool?.remove(task)
    }

    fun stop() {
        if (null != mThreadPool) {
            mThreadPool?.shutdown()
            mThreadPool = null
        }

        if (null != mQueue) {
            mQueue?.clear()
            mQueue = null
        }
    }
}