package org.d3if3115.mobpro1.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.d3if3115.catalogkendaraan.MainActivity
import org.d3if3115.catalogkendaraan.ui.main.KendaraanAdapter
import org.d3if3115.mobpro1.network.ApiStatus


class KendaraanFragment : Fragment() {
    private val viewModel: KendaraanViewModel by lazy {
        ViewModelProvider(this)[KendaraanViewModel::class.java]
    }
    private lateinit var binding: FragmentMainBinding
    private lateinit var myAdapter: KendaraanAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        myAdapter = KendaraanAdapter()
        with(binding.recyclerView) {
            addItemDecoration(
                DividerItemDecoration(context,
                    RecyclerView.VERTICAL))
            adapter = myAdapter
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
        }
        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }
        viewModel.scheduleUpdater(requireActivity().application)
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }
            }
            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }

}