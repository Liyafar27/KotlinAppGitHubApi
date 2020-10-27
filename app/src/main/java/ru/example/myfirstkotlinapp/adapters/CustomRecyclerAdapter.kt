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


class CustomRecyclerAdapter( private val urlAr: ArrayList<String>, private val nameAr: ArrayList<String>) :
        RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_repo, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.textName.text=nameAr[position]
            val uri = Uri.parse(urlAr[position])

            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.imageView2)
            setFadeAnimation(holder.itemView)
        }

    override fun getItemCount() = urlAr.size

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val textName: TextView = itemView.findViewById(R.id.textName)
            val imageView2: ImageView = itemView.findViewById(R.id.imageView2)
        }
    }

 fun setFadeAnimation(view: View) {

    val anim = AlphaAnimation(0.0f, 1.0f)
    anim.duration = 500
    view.startAnimation(anim)
}

