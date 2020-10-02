package moe.gkd.smsassistant.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import moe.gkd.smsassistant.databinding.ItemHomeConfigListBinding
import moe.gkd.smsassistant.entity.ConfigStatusEntity

class ConfigListAdapter(private val data: MutableList<ConfigStatusEntity>) :
    RecyclerView.Adapter<ConfigListAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemHomeConfigListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeConfigListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = data[position]
        binding.checkbox.setText(item.name)
        binding.checkbox.isChecked = item.isEnable
    }

    override fun getItemCount(): Int {
        return data.size
    }
}