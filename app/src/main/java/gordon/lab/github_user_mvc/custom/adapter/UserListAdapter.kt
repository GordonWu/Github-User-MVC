package gordon.lab.github_user_mvc.custom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gordon.lab.github_user_mvc.custom.ui.userlist.UserListViewHolder
import gordon.lab.github_user_mvc.data.model.UserItems
import gordon.lab.github_user_mvc.databinding.RowUserItemBinding
import java.lang.ref.WeakReference

class UserListAdapter:RecyclerView.Adapter<UserListViewHolder>() {
    interface OnItemListClickListener {
        fun onClick(data: UserItems)
    }

    interface OnBottomReachedListener {
        fun onBottomReached(userItem: UserItems)
    }

    var dataModel = arrayListOf<UserItems>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var mOnItemListListener: WeakReference<OnItemListClickListener>? = null
    private var mOnBottomReachListener: WeakReference<OnBottomReachedListener>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val itemBinding = RowUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(dataModel[position])
        holder.itemView.tag = position
        holder.itemView.setOnClickListener { it ->
            (it.tag as? Int)?.apply{
                if (this < itemCount)
                    mOnItemListListener?.get()?.onClick(dataModel[position])
            }
        }

        if (position == itemCount - 1){
            mOnBottomReachListener?.get()?.onBottomReached(dataModel[position])
        }
    }

    override fun getItemCount(): Int {
        return dataModel.size
    }

    fun setOnItemListListener(l: OnItemListClickListener) {
        mOnItemListListener = WeakReference(l)
    }

    fun setOnBottomReachedListener(l: OnBottomReachedListener) {
        mOnBottomReachListener = WeakReference(l)
    }

    fun setDataModel(mBookshelf: List<UserItems>) {
        dataModel.addAll(mBookshelf)
        notifyDataSetChanged()
    }
}