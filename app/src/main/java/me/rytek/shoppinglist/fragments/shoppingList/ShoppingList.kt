package me.rytek.shoppinglist.fragments.shoppingList

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_shopping_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.rytek.shoppinglist.MainActivity
import me.rytek.shoppinglist.R
import me.rytek.shoppinglist.ShoppingListViewModel
import me.rytek.shoppinglist.databinding.FragmentShoppingListBinding
import me.rytek.shoppinglist.model.Item


/**
 * A simple [Fragment] subclass.
 */
class ShoppingList : Fragment() {
    // Lazy Inject ViewModel
    val shoppingListViewModel: ShoppingListViewModel by navGraphViewModels(R.id.nav_graph)
    val pageCount: Int = 2
    val adaptorList = ArrayList<ShoppingListAdaptor>(pageCount)
    var itemViewPagerAdapter: ItemViewPagerAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val parent = activity as MainActivity;
        parent.normalLayout()
        for (i in 0 until pageCount) {
            val adaptor = ShoppingListAdaptor()
            adaptor.onItemClick = { itemClicked(it) }
            adaptorList.add(i, adaptor)
        }

        val binding: FragmentShoppingListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list, container, false);
        val view = binding.root

        itemViewPagerAdapter = ItemViewPagerAdaptor(adaptorList)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager.adapter = itemViewPagerAdapter

        TabLayoutMediator(tabs, pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Items"
                        tab.icon = this@ShoppingList.activity?.getDrawable(R.drawable.ic_shopping_cart_black_24dp)
                    }
                    1 -> {
                        tab.text = "Marked Off"
                        tab.icon = this@ShoppingList.activity?.getDrawable(R.drawable.ic_check_black_24dp)
                    }
                }
            }).attach()

        shoppingListViewModel.items.observe(viewLifecycleOwner, Observer {items -> run {
            Log.d(this::class.simpleName, items.toString())
            adaptorList[0].submitList(items)
        }})

        shoppingListViewModel.markedOffItems.observe(viewLifecycleOwner, Observer {items -> run {
            Log.d(this::class.simpleName, items.toString())
            adaptorList[1].submitList(items)
        }})


        // Setup shimmer
//        shoppingListViewModel.isLoadingItemData.observe(viewLifecycleOwner, Observer { isLoading -> run {
//            itemViewPagerAdapter!!.setLoading(isLoading)
//        } })

        extended_fab.setOnClickListener {

            // Get the layout inflater
            val inflater = this.layoutInflater
            val mView: View = inflater.inflate(R.layout.create_item_dialog, null)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add a List")
                .setView(mView)
                .setPositiveButton("Ok") { dialog, which -> run {
                    Log.d(this::class.simpleName, "Ok Clicked")
                    val name =
                        (mView.findViewById<View>(R.id.textFieldItemName) as EditText).text
                            .toString()
                    val quantity =
                        (mView.findViewById<View>(R.id.textFieldItemQuantity) as EditText).text
                            .toString().toInt()
                    val comments =
                        (mView.findViewById<View>(R.id.textFieldItemComments) as EditText).text
                            .toString()

                    GlobalScope.launch {
                        shoppingListViewModel.createItem(name, quantity, comments)
                    }

                } }
                .setNegativeButton("Cancel") { dialog, which -> run {
                    Log.d(this::class.simpleName, "Cancel Clicked")
                } }
                .show()
        }


    }

    private fun itemClicked(item: Item) {
        Log.d(this::class.simpleName, "Item Clicked $item")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                shoppingListViewModel.markOffItem(item)
            }
            catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    showSnackBar("Failed to mark off item")
                    e.printStackTrace()
                }
            }
        }

    }

    fun showSnackBar(message: String, length: Int = Snackbar.LENGTH_SHORT, viewCtx: View? = view) {
        Snackbar.make(viewCtx as View, message, length).show()
    }

}
