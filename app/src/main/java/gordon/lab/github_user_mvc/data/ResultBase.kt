package gordon.lab.github_user_mvc.data

open class ResultBase(private val requestOk: Boolean, private val rawData: String?) {


    val isSuccess: Boolean
        get() {
            return try {
                requestOk && null != rawData
            } catch (ignored: Exception) {
                false
            }
        }

}
