package me.rytek.shoppinglist.fragments.selectFamily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.rytek.shoppinglist.databinding.FamilyItemBinding
import me.rytek.shoppinglist.model.Family

class FamilyAdapter : ListAdapter<Family, FamilyAdapter.FamilyViewHolder>(Companion) {

    var onItemClick: ((Family) -> Unit)? = null

    companion object: DiffUtil.ItemCallback<Family>() {
        override fun areItemsTheSame(oldItem: Family, newItem: Family): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Family, newItem: Family): Boolean = oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FamilyItemBinding.inflate(layoutInflater)

        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.layoutRoot.layoutParams = lp
        return FamilyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamilyViewHolder, position: Int) {
        val currentFamily = getItem(position)
        holder.binding.family = currentFamily
        holder.binding.executePendingBindings()
    }

    inner class FamilyViewHolder(val binding: FamilyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.familyButton.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }
    }
}