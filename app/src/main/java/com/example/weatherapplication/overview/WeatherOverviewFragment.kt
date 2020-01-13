package com.example.weatherapplication.overview


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentWeatherOverviewBinding
import com.example.weatherapplication.forecastdetails.ForecastDetailsFragmentDirections
import com.example.weatherapplication.formatDate

/**
 * A simple [Fragment] subclass.
 */
class WeatherOverviewFragment : Fragment() {

    private val viewModel: WeatheroverviewViewModel by lazy {
        requireNotNull(activity).let { activity ->
            ViewModelProviders.of(
                this,
                WeatheroverviewViewModel.Factory(activity.application)
            )[WeatheroverviewViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWeatherOverviewBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.weatherViewModel = viewModel

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        val forecastAdapter = WeatherForecastAdapter(ForecastListener {
            Toast.makeText(context, "Selected forecast for ${it.formatDate()}", Toast.LENGTH_LONG)
                .show()
            val action = WeatherOverviewFragmentDirections.
                actionWeatherOverviewFragmentToForecastDetailsFragment(it)
            findNavController().navigate(action)
        })
        binding.forecastList.adapter = forecastAdapter
        viewModel.forecast.observe(this, Observer {
            it?.let {
                forecastAdapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.refresh -> {
            Toast.makeText(context, "will refresh now", Toast.LENGTH_SHORT).show()
            viewModel.onRefresh()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
