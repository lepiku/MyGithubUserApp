package id.oktoluqman.mygithubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.oktoluqman.mygithubuserapp.databinding.ItemRowGithubUserBinding
import id.oktoluqman.mygithubuserapp.model.GithubUser

class ListGithubUserAdapter(
    private val onItemClickCallback: (data: GithubUser) -> Unit
) : RecyclerView.Adapter<ListGithubUserAdapter.ListViewHolder>() {

    private val listGithubUser = ArrayList<GithubUser>()

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemRowGithubUserBinding.bind(itemView)

        fun bind(mGithubUser: GithubUser) {
            Glide.with(itemView.context)
                .load(mGithubUser.avatarUrl)
                .apply(RequestOptions().override(40, 40))
                .into(binding.imgItemPhoto)

            binding.tvItemName.text = mGithubUser.username
            binding.tvItemUsername.text = mGithubUser.htmlUrl

            itemView.setOnClickListener { onItemClickCallback(mGithubUser) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_row_github_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listGithubUser[position])
    }

    override fun getItemCount(): Int {
        return listGithubUser.size
    }

    fun setData(items: List<GithubUser>) {
        listGithubUser.clear()
        listGithubUser.addAll(items)
        notifyDataSetChanged()
    }
}