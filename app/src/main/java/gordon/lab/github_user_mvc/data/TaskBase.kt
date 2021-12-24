package gordon.lab.github_user_mvc.data

import android.text.TextUtils
import gordon.lab.github_user_mvc.config.Setting.SERVER_HOST
import java.lang.Thread.sleep

abstract class TaskBase : Runnable {

    companion object {
        const val HTTP_OK = 200
    }

    @Volatile
    private var mIsAbort: Boolean = false
    private var mRunningThread: Thread? = null


    // Thread related functions
    //

    override fun run() {
        mRunningThread = Thread.currentThread()
        try {
            onRun()
        } catch (ex: Exception) {
            ex.printStackTrace()
            try {
                mRunningThread!!.also { sleep(500) }
                onRun()
            } catch (e: Exception) {

            }

        }

        recycle()
    }

    fun abort() {
        mIsAbort = true

        if (null != mRunningThread) {
            val thread = mRunningThread
            thread!!.interrupt()
        }

        recycle()
    }


    // Abstract methods
    //

    @Throws(Exception::class)
    abstract fun onRun()

    abstract fun onRelease()


    // Public Methods
    //

    fun exec() {
        TaskExecutor.getInstance().execute(this)
    }

    fun remove() {
        TaskExecutor.getInstance().remove(this)
    }


    // Protected Methods
    //

    protected fun getApiUri(path: String): String {
        if (TextUtils.isEmpty(path))
            throw NullPointerException("Parameter path cannot be null or empty string")

        return String.format("%s%s",SERVER_HOST , path)
    }

    // Release resources
    //

    private fun recycle() {
        mRunningThread = null
        onRelease()
    }
}
