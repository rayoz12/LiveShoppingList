package me.rytek.shoppinglist.fragments.selectFamily


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.android.synthetic.main.fragment_select_family.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.rytek.shoppinglist.MainActivity
import me.rytek.shoppinglist.R
import me.rytek.shoppinglist.ShoppingListViewModel
import me.rytek.shoppinglist.databinding.FragmentSelectFamilyBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class SelectFamily : Fragment() {
    // Lazy Inject ViewModel
    val shoppingListViewModel: ShoppingListViewModel by navGraphViewModels(R.id.nav_graph)
    val familyAdapter = FamilyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val parent = activity as MainActivity;
        parent.fullscreenLayout()
        val binding: FragmentSelectFamilyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_family, container, false);
        val view = binding.root
        binding.adapter = familyAdapter
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shoppingListViewModel.userFamilies.observe(viewLifecycleOwner, Observer {families -> run {
            Log.d(this::class.simpleName, families.toString())
            familyAdapter.submitList(families)
        }})

        Log.d(this::class.simpleName, shoppingListViewModel.isLoggedIn.value.toString())

        val userFams = shoppingListViewModel.userFamilies.value
        if (userFams != null) {
            if (userFams.isNotEmpty()) {
                familyAdapter.submitList(userFams)
            }
        }

        familyAdapter.onItemClick = {family ->
            Log.d(this::class.simpleName, "Family Clicked")
            shoppingListViewModel.setFamily(family)
            view.findNavController().navigate(R.id.action_selectFamily_to_selectList)
        }
    }


}
