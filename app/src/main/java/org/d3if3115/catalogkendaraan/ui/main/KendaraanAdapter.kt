package org.d3if3115.catalogkendaraan.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if3115.catalogkendaraan.R
import org.d3if3115.catalogkendaraan.databinding.ListKendaraanBinding
import org.d3if3115.catalogkendaraan.model.Kendaraan
import org.d3if3115.mobpro1.network.KendaraanApi


class KendaraanAdapter : RecyclerView.Adapter<KendaraanAdapter.ViewHolder>() {
    private val data = mutableListOf<Kendaraan>()
    fun updateData(newData: List<Kendaraan>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }


    class ViewHolder(
        private val binding: ListKendaraanBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kendaraan: Kendaraan) = with(binding) {
            namaTextView.text = kendaraan.merk
            latinTextView.text = kendaraan.asal
            Glide.with(imageView.context)
                .load(KendaraanApi.getKendaraanUrl(kendaraan.ImageId))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView)



            root.setOnClickListener {
                val message = root.context.getString(R.string.message, kendaraan.merk)
                Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListKendaraanBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

}