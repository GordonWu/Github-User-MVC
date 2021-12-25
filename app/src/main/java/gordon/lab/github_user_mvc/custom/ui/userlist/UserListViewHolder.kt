package gordon.lab.github_user_mvc.custom.ui.userlist

import androidx.recyclerview.widget.RecyclerView
import gordon.lab.github_user_mvc.core.GlideApp
import gordon.lab.github_user_mvc.data.model.UserItems
import gordon.lab.github_user_mvc.databinding.RowUserItemBinding

class UserListViewHolder(private val itemBinding: RowUserItemBinding) :RecyclerView.ViewHolder(itemBinding.root){

    private lateinit var item: UserItems

    fun bind(i: UserItems ) {
        item = i
        if (item.avatarURL.startsWith("https")) {
            GlideApp.with(itemBinding.root)
                .load(item.avatarURL)
                .centerCrop()
                .into(itemBinding.ivUserAvatar)
        }
        itemBinding.tvUserName.text = item.login
    }
}