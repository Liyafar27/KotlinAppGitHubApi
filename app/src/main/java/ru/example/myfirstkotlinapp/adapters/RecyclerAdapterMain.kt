package ru.example.myfirstkotlinapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.example.myfirstkotlinapp.R
import ru.example.myfirstkotlinapp.api.GitHubRepo


class RecyclerAdapterMain(
    private val values: List<GitHubRepo>,
    private val clickListener: (GitHubRepo) -> Unit
) : RecyclerView.Adapter<RecyclerAdapterMain.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_repo, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = values[position]
        val urlAvatar = Uri.parse(item.owner!!.avatar_url)
        val position1 = position + 1
        val owner = item.owner.login

        val message =
            position1.toString() + ".  " + "Repo Name: " + "\n" + item.name + "\n" + "Owner: " + owner + "\n" + "Created at: " + "\n" + item.created_at + "\n" + "⭐ Stargazers count: " + item.stargazers_count

        holder.textName.text = message
        holder.dateText.text = item.created_at?.toString()
        holder.bindValue(values[position], clickListener)

        Glide.with(holder.itemView.context)
            .load(urlAvatar)
            .into(holder.imageView2)

        setFadeAnimation(holder.itemView)
    }

    override fun getItemCount() = values.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textName: TextView = itemView.findViewById(R.id.textName)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val imageView2: ImageView = itemView.findViewById(R.id.imageView2)

        fun bindValue(itemModel: GitHubRepo, listener: (GitHubRepo) -> Unit) {

            itemView.setOnClickListener { listener(itemModel) }
        }
    }

    private fun setFadeAnimation(view: View) {

        val anim = AlphaAnimation(0.3f, 1.0f)
        anim.duration = 300
        view.startAnimation(anim)
    }
}








