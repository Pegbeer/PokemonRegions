package com.example.pokemonregions.ui.adapter
import com.example.pokemonregions.data.model.PokemonType
import com.example.pokemonregions.databinding.ChipPokemonTypeBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.pokemonregions.utils.capitalize

class PokemonTypeAdapter : RecyclerView.Adapter<PokemonTypeAdapter.PokemonTypeViewHolder>() {

    val diffCallback = object : DiffUtil.ItemCallback<PokemonType>() {

        override fun areItemsTheSame(oldItem: PokemonType, newItem: PokemonType): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: PokemonType, newItem: PokemonType): Boolean {
            TODO("not implemented")
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTypeViewHolder {
        return PokemonTypeViewHolder(
            ChipPokemonTypeBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonTypeViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<PokemonType>) {
        differ.submitList(list)
    }

    inner class PokemonTypeViewHolder(private val binding:ChipPokemonTypeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PokemonType) = with(binding) {
            name = item.name.capitalize()
        }
    }
}