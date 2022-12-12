package com.example.pokemonregions.ui.adapter

import android.content.Intent
import android.os.Bundle
import com.example.pokemonregions.data.model.Region
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.pokemonregions.databinding.CardRegionBinding
import com.example.pokemonregions.ui.view.RegionActivity
import com.example.pokemonregions.utils.capitalize

class RegionsAdapter(
    private val onClick:(Region) -> Unit
) : RecyclerView.Adapter<RegionsAdapter.RegionsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Region>() {

        override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionsViewHolder {
        return RegionsViewHolder(CardRegionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Region>?) {
        differ.submitList(list)
    }

    inner class RegionsViewHolder(private val binding:CardRegionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Region) = with(binding) {
            name = item.name.capitalize()
            root.setOnClickListener {
                onClick(item)
                /*val intent = Intent(it.context,RegionActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable(REGION_SELECTED,item)
                intent.putExtras(bundle)
                it.context.startActivity(intent)*/
            }
        }
    }

    companion object{
        const val REGION_SELECTED = "regionSelected"
    }
}