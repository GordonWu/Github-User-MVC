package gordon.lab.github_user_mvc.data

import org.json.JSONObject

open class ResultBase(val requestOk: Boolean, val rawData: JSONObject?) {

    companion object {
        const val KEY_IS_SUCCESS = "result"
        const val KEY_ERROR_CODE = "errCode"
        const val KEY_ERROR_MSG = "errMsg"
    }

    val isSuccess: Boolean
        get() {
            return try {
                requestOk && null != rawData && rawData.getBoolean(KEY_IS_SUCCESS)
            } catch (ignored: Exception) {
                false
            }
        }

    val errCode: String?
        get() {
            return try {
                if (requestOk && null != rawData)
                    rawData.getString(KEY_ERROR_CODE)
                else
                    null
            } catch (ignored: Exception) {
                null
            }
        }

    val errMsg: String?
        get() {
            return try {
                if (requestOk && null != rawData)
                    rawData.getString(KEY_ERROR_MSG)
                else
                    null
            } catch (ignored: Exception) {
                null
            }
        }

}
