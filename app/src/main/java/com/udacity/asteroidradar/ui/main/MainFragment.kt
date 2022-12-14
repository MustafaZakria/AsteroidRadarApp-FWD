package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.AsteroidType

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((requireActivity().application as AsteroidApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        //handle clicking on items --> pass to the view model bec its the right place to handle the click events
        val adapter = MainAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidDetailNavigated()
            }
        })

//        val asteroidList =
//            listOf<Asteroid>(
//                Asteroid(
//                    1, "asteroid1", "2002",
//                    1.0, 2.0, 2.0, 1.0, true
//                ),
//            Asteroid(
//                    2, "asteroid2", "2002",
//                    1.0, 2.0, 2.0, 1.0, false
//                ),
//            Asteroid(
//                    3, "asteroid1", "2002",
//                    1.0, 2.0, 2.0, 1.0, true
//                )
//            )
//        adapter.submitList(asteroidList)

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            Log.d("&&&", it.toString())
            adapter.submitList(it)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.show_week_menu -> viewModel.setType(AsteroidType.WEEK)
            R.id.show_today_menu -> viewModel.setType(AsteroidType.TODAY)
            R.id.show_saved_menu -> viewModel.setType(AsteroidType.SAVED)
        }

        return true
    }
}
