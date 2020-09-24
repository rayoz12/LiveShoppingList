package me.rytek.shoppinglist.fragments.shoppingList

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.rytek.shoppinglist.databinding.ItemItemBinding
import me.rytek.shoppinglist.model.Item
import java.lang.Exception

/**
 *The adaptor that holds the actual items iwthin the pager.
 *
 */
class ShoppingListAdaptor : ListAdapter<Item, ShoppingListAdaptor.ShoppingListViewHolder>(Companion) {

    var onItemClick: ((Item) -> Unit)? = null

    companion object: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemItemBinding.inflate(layoutInflater)

        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.layoutRoot.layoutParams = lp
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.item = currentItem
        holder.binding.isBusy = false

        if (currentItem.markedOff) {
            holder.binding.mtrlListItemText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        else {
            holder.binding.mtrlListItemText.paintFlags = holder.binding.mtrlListItemText.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }

        holder.binding.executePendingBindings()
    }

    inner class ShoppingListViewHolder(val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutRoot.setOnClickListener {
                try {
                    onItemClick?.invoke(getItem(bindingAdapterPosition))
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}