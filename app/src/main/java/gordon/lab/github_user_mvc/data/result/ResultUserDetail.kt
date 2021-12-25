package gordon.lab.github_user_mvc.data.result

import android.util.Log
import com.google.gson.Gson
import gordon.lab.github_user_mvc.data.ResultBase
import gordon.lab.github_user_mvc.data.model.UserDetail

class ResultUserDetail (requestOk: Boolean, rawData: String?) : ResultBase(requestOk, rawData) {

    private val mTAG = "ResultUserDetail"
    var data: UserDetail? = null
        private set

    init {
        run {
            if (!isSuccess)
                return@run

            try {
                data = Gson().fromJson(rawData!!.toString(), UserDetail::class.java)
            } catch (ex: Exception) {
                data = null
                Log.e(mTAG, "Parse Json Error: " + ex.localizedMessage)
            }
        }
    }
}