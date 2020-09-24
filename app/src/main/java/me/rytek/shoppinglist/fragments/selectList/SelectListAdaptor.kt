package me.rytek.shoppinglist.fragments.selectList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.rytek.shoppinglist.databinding.ShoppinglistItemBinding
import me.rytek.shoppinglist.model.ShoppingList

class SelectListAdaptor : ListAdapter<ShoppingList, SelectListAdaptor.ListViewHolder>(Companion) {

    var onItemClick: ((ShoppingList) -> Unit)? = null

    companion object: DiffUtil.ItemCallback<ShoppingList>() {
        override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean = oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ShoppinglistItemBinding.inflate(layoutInflater)

        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.layoutRoot.layoutParams = lp
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentShoppingList = getItem(position)
        holder.binding.list = currentShoppingList
        holder.binding.executePendingBindings()
    }

    inner class ListViewHolder(val binding: ShoppinglistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutRoot.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }
    }
}