package gordon.lab.github_user_mvc.data.model

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("avatar_url") var avatarUrl : String  = "",
    val bio: String? = null ,
    val blog: String? = null ,
    val company: String? = null ,
    val email: String  = "",
    val id: Int  = 0,
    val location: String ? = null ,
    val login: String  = "",
    val name: String  = "",
)