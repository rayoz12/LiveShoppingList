package me.rytek.shoppinglist.fragments.shoppingList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import me.rytek.shoppinglist.MainActivity

import me.rytek.shoppinglist.R
import me.rytek.shoppinglist.databinding.FragmentViewPagerContentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerContentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPagerContentFragment(val adaptor: ShoppingListAdaptor) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val parent = activity as MainActivity;
        parent.normalLayout()
        val binding: FragmentViewPagerContentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager_content, container, false);
        val view = binding.root
        binding.adapter = adaptor
        Log.d(this::class.simpleName, this::class.simpleName + " OnCreateView")
        // Inflate the layout for this fragment
        return view
    }
}
