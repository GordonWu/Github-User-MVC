package gordon.lab.github_user_mvc.data.task

import android.util.Log
import gordon.lab.github_user_mvc.data.TaskBase
import gordon.lab.github_user_mvc.data.TaskHttpRequest
import gordon.lab.github_user_mvc.data.result.ResultUserList
import org.greenrobot.eventbus.EventBus
import org.json.JSONException

class TaskGetUserList(private val lastId: Int) : TaskBase() {

    private val mTAG = "TaskGetUserList"
    private fun apiURL() = getApiUri("/users")

    override fun onRun() {
        val resp = StringBuilder()
        do {
            try {
                val status = TaskHttpRequest
                    .newRequest(apiURL())
                    .method(TaskHttpRequest.Method.GET)
                    .setArg("since",lastId.toString())
                    .syncCall(resp)

                if (HTTP_OK != status)
                    break

                 EventBus.getDefault().post(ResultUserList(true, resp.toString()))
                return
            } catch (ex: JSONException) {
                Log.e(mTAG, "Json parse failed: " + ex.localizedMessage)
            } catch (ex: Exception) {
                Log.e(mTAG, "Generic Error: " + ex.localizedMessage)
            }
        } while (false)

        EventBus.getDefault().post(ResultUserList(false, null))
    }

    override fun onRelease() {}
}