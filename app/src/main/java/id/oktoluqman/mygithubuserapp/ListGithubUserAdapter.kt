package id.oktoluqman.mygithubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.oktoluqman.mygithubuserapp.databinding.ItemRowGithubUserBinding
import id.oktoluqman.mygithubuserapp.model.GithubUserOld

class ListGithubUserAdapter(
    private val listGithubUserOld: ArrayList<GithubUserOld>,
    private val onItemClickCallback: (data: GithubUserOld) -> Unit
) : RecyclerView.Adapter<ListGithubUserAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemRowGithubUserBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_row_github_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val mGithubUser = listGithubUserOld[position]

        Glide.with(holder.itemView.context)
            .load(mGithubUser.avatar)
            .apply(RequestOptions().override(72, 72))
            .into(holder.binding.imgItemPhoto)

        holder.binding.tvItemUsername.text = mGithubUser.username
        holder.binding.tvItemName.text = mGithubUser.name
        holder.binding.tvItemLocation.text = mGithubUser.location

        holder.itemView.setOnClickListener { onItemClickCallback(mGithubUser) }
    }

    override fun getItemCount(): Int {
        return listGithubUserOld.size
    }
}