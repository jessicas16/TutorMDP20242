package com.example.tutorm7front

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorm7front.databinding.ItemHeroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class HeroAdapter(
    private val heroes: List<HeroEntity>
) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    inner class HeroViewHolder(private val binding: ItemHeroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hero: HeroEntity) {
            binding.textViewName.text = hero.name
            binding.imageViewHero.setImageResource(android.R.color.darker_gray) // Gray background

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = URL(hero.image)
                    val connection = url.openConnection()
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000
                    connection.connect()
                    val input = connection.getInputStream()
                    val bitmap = BitmapFactory.decodeStream(input)

                    withContext(Dispatchers.Main) {
                        if (bitmap != null) {
                            binding.imageViewHero.setImageBitmap(bitmap)
                        }
                        // else do nothing, stay gray
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // On failure, just stay gray
                }
            }
        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(heroes[position])
    }

    override fun getItemCount(): Int = heroes.size
}

