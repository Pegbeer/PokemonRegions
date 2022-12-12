package com.example.pokemonregions.ui.adapter
import com.example.pokemonregions.data.model.Pokemon
import com.example.pokemonregions.databinding.CardPokemonBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.pokemonregions.utils.capitalize

class PokemonsAdapter(
    private val onClick:(Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonsAdapter.PokemonsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Pokemon>() {

        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonsViewHolder {
        return PokemonsViewHolder(
            CardPokemonBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: PokemonsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Pokemon>?) {
        val ordered = list?.sortedBy { it.name }?.toList()
        differ.submitList(ordered)
    }

    inner class PokemonsViewHolder (private val binding:CardPokemonBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pokemon) = with(binding) {
            val typeAdapter = PokemonTypeAdapter()
            typeAdapter.submitList(item.types)
            pokemonTypesRecycler.adapter = typeAdapter
            pokemonNameTextView.text = item.name.capitalize()
            pokemonAddButton.setOnClickListener {
                onClick(item)
            }
        }
    }
}