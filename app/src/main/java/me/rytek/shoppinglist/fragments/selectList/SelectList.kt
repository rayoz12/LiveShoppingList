package me.rytek.shoppinglist.fragments.selectList

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_select_list.*
import me.rytek.shoppinglist.MainActivity

import me.rytek.shoppinglist.R
import me.rytek.shoppinglist.ShoppingListViewModel
import me.rytek.shoppinglist.databinding.FragmentSelectListBinding

/**
 * A simple [Fragment] subclass.
 */
class SelectList : Fragment() {
    // Lazy Inject ViewModel
    val shoppingListViewModel: ShoppingListViewModel by navGraphViewModels(R.id.nav_graph)
    val shoppingListsAdapter = SelectListAdaptor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parent = activity as MainActivity;
        parent.normalLayout()
        val binding: FragmentSelectListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_list, container, false);
        val view = binding.root
        binding.adapter = shoppingListsAdapter

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shoppingListViewModel.selectedFamily.observe(viewLifecycleOwner, Observer {family -> run {
            Log.d(this::class.simpleName, family.toString())
            shoppingListsAdapter.submitList(family.lists)
        }})

        Log.d(this::class.simpleName, shoppingListViewModel.isLoggedIn.value.toString())

        val selectedFamily = shoppingListViewModel.selectedFamily.value
        if (selectedFamily != null) {
            if (selectedFamily.lists.isNotEmpty()) {
                shoppingListsAdapter.submitList(selectedFamily.lists)
            }
        }

        shoppingListsAdapter.onItemClick = {list ->
            Log.d(this::class.simpleName, "List Clicked")
            shoppingListViewModel.setShoppingList(list)
            view.findNavController().navigate(R.id.action_selectList_to_shoppingList)
        }

        extended_fab.setOnClickListener {
            val titleBox = EditText(context);
            titleBox.hint = "Name"
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add a List")
                .setView(titleBox)
                .setPositiveButton("Ok", {dialog, which -> run {
                    Log.d(this::class.simpleName, "Ok Clicked")
                } })
                .setNegativeButton("Cancel", {dialog, which -> run {
                    Log.d(this::class.simpleName, "Cancel Clicked")
                } })
                .show()
        }
    }
}
