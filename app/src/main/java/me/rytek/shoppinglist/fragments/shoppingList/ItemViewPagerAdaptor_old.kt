package me.rytek.shoppinglist.fragments.shoppingList

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ItemViewPagerAdaptor_old (activity: AppCompatActivity, val itemsCount: Int, val adaptors: List<ShoppingListAdaptor>) :
    FragmentStateAdapter(activity) {

    init {
//        assert(itemsCount == adaptors.size)
    }

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return ViewPagerContentFragment(adaptors[position])
    }
}