package com.example.pokemonregions.ui.adapter


import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.pokemonregions.R
import com.example.pokemonregions.data.model.PokemonTeam
import com.example.pokemonregions.databinding.CardTeamBinding
import com.example.pokemonregions.ui.view.DetailTeamActivity

class TeamsAdapter : RecyclerView.Adapter<TeamsAdapter.TeamsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<PokemonTeam?>() {
        override fun areItemsTheSame(oldItem: PokemonTeam, newItem: PokemonTeam): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: PokemonTeam, newItem: PokemonTeam): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        return TeamsViewHolder(CardTeamBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<PokemonTeam?>) {
        differ.submitList(list.sortedBy { it?.number }.toList())
    }

    class TeamsViewHolder constructor(private val binding: CardTeamBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PokemonTeam?) {
            with(binding){
                titleTeamCard.text = item?.name ?: ""
                countTeamCard.text = root.context.getString(R.string.pokemon_team_counter,item?.pokemons?.size ?: 0)
                numberTeamCard.text = root.context.getString(R.string.number_team_card_prefix,item?.number ?: 0)
                root.setOnClickListener {
                    val intent = Intent(it.context,DetailTeamActivity::class.java)
                    intent.putExtra(DETAIL,item)
                    it.context.startActivity(intent)
                }
            }
        }

        companion object{
            const val DETAIL = "detail_to_update"
        }
    }

}