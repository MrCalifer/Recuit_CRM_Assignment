package edu.califer.recuit_crmassignment.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.califer.recuit_crmassignment.Utils.Company
import edu.califer.recuit_crmassignment.databinding.CompanyRecyclerItemBinding

class CompanyAdapter(var companyList : List<Company>) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>(){


    class ViewHolder(val binding: CompanyRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CompanyRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

}