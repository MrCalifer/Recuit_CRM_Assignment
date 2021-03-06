package edu.califer.recuit_crmassignment.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.califer.recuit_crmassignment.Utils.Company
import edu.califer.recuit_crmassignment.database.entities.CompanyEntity
import edu.califer.recuit_crmassignment.databinding.CompanyRecyclerItemBinding

class CompanyAdapter(var companyList: List<CompanyEntity>, var companyListener: CompanyListener) :
    RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {


    class ViewHolder(private val binding: CompanyRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(companyEntity: CompanyEntity) {
            binding.setCompanyName(companyEntity.companyName)
            binding.setCompanyPhoneNumber(companyEntity.companyPhoneNumber)
            binding.setCompanyWebsite(companyEntity.companyWebsite)
            binding.companyType = companyEntity.companyType
            binding.companyAddress =
                companyEntity.companyAddress + "\n" + companyEntity.companyCity + "\n" + companyEntity.companyState + "\n" + companyEntity.companyCountry
        }
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
        holder.bind(companyList[position])
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    /**
     *function to edit the company details
     */
    fun notifyEditItem(
        position: Int
    ) {
        val companyList = companyList[position]

        companyListener.onItemEdit(
            Company(
                id = companyList.id,
                name = companyList.companyName,
                website = companyList.companyWebsite,
                number = companyList.companyPhoneNumber,
                address = companyList.companyAddress,
                city = companyList.companyCity,
                state = companyList.companyState,
                country = companyList.companyCountry,
                type = companyList.companyType
            )
        )
    }

    /**
     * Function to delete a company entry from database.
     */
    fun removeAt(position: Int) {
        val companyList = companyList[position]

        companyListener.onItemDelete(
            Company(
                id = companyList.id,
                name = companyList.companyName,
                website = companyList.companyWebsite,
                number = companyList.companyPhoneNumber,
                address = companyList.companyAddress,
                city = companyList.companyCity,
                state = companyList.companyState,
                country = companyList.companyCountry,
                type = companyList.companyType
            )
        )
    }

    // method for filtering our recyclerview items.
    fun filterList(filterllist: ArrayList<Company>) {
        // below line is to add our filtered
        // list in our course array list.

        val filterList = ArrayList<CompanyEntity>()
        for (i in 0 until filterllist.size) {
            filterList.add(
                CompanyEntity(
                    id = filterllist[i].id,
                    companyName = filterllist[i].name,
                    companyWebsite = filterllist[i].website,
                    companyPhoneNumber = filterllist[i].number,
                    companyAddress = filterllist[i].address,
                    companyCity = filterllist[i].city,
                    companyState = filterllist[i].state,
                    companyCountry = filterllist[i].country,
                    companyType = filterllist[i].type
                )
            )
        }

        companyList = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    interface CompanyListener {

        fun onItemEdit(company: Company): Company {
            return company
        }

        fun onItemDelete(company: Company): Company {
            return company
        }
    }
}