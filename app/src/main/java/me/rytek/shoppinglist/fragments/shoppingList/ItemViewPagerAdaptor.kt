package me.rytek.shoppinglist.fragments.shoppingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.rytek.shoppinglist.databinding.FragmentViewPagerContentBinding

class ItemViewPagerAdaptor(val adaptors: List<ShoppingListAdaptor>) : RecyclerView.Adapter<ItemViewPagerAdaptor.PagerViewHolder>() {

    var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentViewPagerContentBinding.inflate(layoutInflater, parent, false)

        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.layoutRoot.layoutParams = lp

        return PagerViewHolder(binding)
    }

    override fun getItemCount() = adaptors.size
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val currentItem = adaptors[position]
        holder.binding.adapter = currentItem
        holder.binding.executePendingBindings()
    }

    fun setLoading(isLoading: Boolean) {
//        intArrayOf(0, 1).forEach {
//            val holder: PagerViewHolder? = mRecyclerView?.findViewHolderForAdapterPosition(it) as PagerViewHolder
//            if (isLoading)
//                holder?.binding?.shimmerViewContainer?.startShimmer()
//            else
//                holder?.binding?.shimmerViewContainer?.stopShimmer()
//        }
    }

    inner class PagerViewHolder(val binding: FragmentViewPagerContentBinding) : RecyclerView.ViewHolder(binding.root)
}