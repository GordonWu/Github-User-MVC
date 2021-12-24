package gordon.lab.github_user_mvc.config

object Setting {
    const val SERVER_HOST = "https://api.github.com"

    // Http Client Settings
    const val HTTP_CONNECT_TIMEOUT = 5L // 秒
    const val HTTP_WRITE_TIMEOUT = 5L // 秒
    const val HTTP_READ_TIMEOUT = 5L // 秒
}