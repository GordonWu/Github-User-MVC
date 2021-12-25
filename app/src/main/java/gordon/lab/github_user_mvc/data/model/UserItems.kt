package gordon.lab.github_user_mvc.data.model

import com.google.gson.annotations.SerializedName

data class UserItems(
    @SerializedName("avatar_url")  val avatarURL: String  = "",
    @SerializedName("events_url")  val eventsURL: String  = "",
    @SerializedName("followers_url")  val followersURL: String  = "",
    @SerializedName("following_url")  val followingURL: String  = "",
    @SerializedName("gists_url") val gistsURL: String  = "",
    @SerializedName("gravatar_id") val gravatarID: String  = "",
    @SerializedName("html_url") val htmlURL: String  = "",
    val id: Int  = 0,
    val login: String  = "",
    @SerializedName("node_id") val nodeID: String  = "",
    @SerializedName("organizations_url") val organizationsURL: String  = "",
    @SerializedName("received_events_url") val receivedEventsURL: String  = "",
    @SerializedName("repos_url") val reposURL: String  = "",
    @SerializedName("site_admin") val siteAdmin: Boolean  = false,
    @SerializedName("starred_url") val starredURL: String  = "",
    @SerializedName("subscriptions_url") val subscriptionsURL: String  = "",
    val type: String  = "",
    val url: String  = ""
)