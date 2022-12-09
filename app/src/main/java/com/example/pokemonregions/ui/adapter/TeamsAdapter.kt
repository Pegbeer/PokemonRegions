package com.example.pokemonregions.ui.adapter

import com.example.pokemonregions.data.model.PokemonTeam
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.pokemonregions.databinding.CardTeamBinding

class TeamsAdapter : RecyclerView.Adapter<TeamsAdapter.TeamsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<PokemonTeam>() {

        override fun areItemsTheSame(oldItem: PokemonTeam, newItem: PokemonTeam): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: PokemonTeam, newItem: PokemonTeam): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        return TeamsViewHolder(CardTeamBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<PokemonTeam>) {
        differ.submitList(list)
    }

    class TeamsViewHolder constructor(private val binding: CardTeamBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PokemonTeam) {
            with(binding){
                titleTeamCard.text = item.name
                countTeamCard.text = String.format("%i/6")
            }
        }
    }

}