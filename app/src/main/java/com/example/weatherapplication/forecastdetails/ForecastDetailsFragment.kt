package com.example.weatherapplication.forecastdetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentForecastDetailsBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ForecastDetailsFragment : Fragment() {

    private val args: ForecastDetailsFragmentArgs by navArgs()

    private val viewModel: ForecastDetailsViewModel by lazy {
        requireNotNull(activity).let {
            Timber.d("Creating view model with Dt: ${args.forecastDt}")
            ViewModelProviders.of(
                this,
                ForecastDetailsViewModel.Factory(it.application, args.forecastDt)
            )[ForecastDetailsViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentForecastDetailsBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}
