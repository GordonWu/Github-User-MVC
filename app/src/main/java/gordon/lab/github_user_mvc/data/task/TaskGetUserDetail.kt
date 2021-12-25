package gordon.lab.github_user_mvc.data.task

import android.util.Log
import gordon.lab.github_user_mvc.data.TaskBase
import gordon.lab.github_user_mvc.data.TaskHttpRequest
import gordon.lab.github_user_mvc.data.result.ResultUserDetail
import gordon.lab.github_user_mvc.data.result.ResultUserList
import org.greenrobot.eventbus.EventBus
import org.json.JSONException

class TaskGetUserDetail(private val username: String) : TaskBase() {

    private val mTAG = "TaskGetUserDetail"
    private fun apiURL() = getApiUri("/users/${username}")

    override fun onRun() {
        val resp = StringBuilder()
        do {
            try {
                val status = TaskHttpRequest
                    .newRequest(apiURL())
                    .method(TaskHttpRequest.Method.GET)
                    .syncCall(resp)

                if (HTTP_OK != status)
                    break

                EventBus.getDefault().post(ResultUserDetail(true,  resp.toString()))
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