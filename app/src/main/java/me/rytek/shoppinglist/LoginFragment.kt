package me.rytek.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // Lazy Inject ViewModel
    val shoppingListViewModel: ShoppingListViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parent = activity as MainActivity;
        parent.fullscreenLayout()

        loginButton.setOnClickListener {
            showSnackBar("Login Clicked")
        }

        registerButton.setOnClickListener {
            showSnackBar("Register Clicked")
        }

        shoppingListViewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {isloggedin -> run {
            showSnackBar("Logged in Changed! $isloggedin", Snackbar.LENGTH_LONG)
            if (isloggedin) {
                loginSubmit.findNavController().navigate(R.id.action_loginFragment_to_selectFamily)
            }
        }})

        shoppingListViewModel.user.observe(viewLifecycleOwner, Observer {user -> run {
            if (user == null) {
                return@run
            }
            showSnackBar("User Changed! ${user.firstName}", Snackbar.LENGTH_LONG)
        }})

        shoppingListViewModel.fullName.observe(viewLifecycleOwner, Observer {name -> run {
            if (name == null) {
                return@run
            }
            introText2.text = name
            showSnackBar("Logged in as $name!")
        }})



        loginSubmit.setOnClickListener {

            if (shoppingListViewModel.isLoggedIn.value as Boolean) {
                return@setOnClickListener
            }
            GlobalScope.launch {
                val response =
                    withContext(Dispatchers.IO) {
                        shoppingListViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                    }
                if (response == null) {
                    showSnackBar("Failed to Login", Snackbar.LENGTH_LONG)
                    return@launch
                }
                val families = withContext(Dispatchers.IO) { shoppingListViewModel.getUserFamilies() }
              //  view.findNavController().navigate(R.id.action_loginFragment_to_selectFamily)
            }
        }
    }

    fun showSnackBar(message: String, length: Int = Snackbar.LENGTH_SHORT, viewCtx: View? = view) {
        Snackbar.make(viewCtx as View, message, length).show()
    }
}
