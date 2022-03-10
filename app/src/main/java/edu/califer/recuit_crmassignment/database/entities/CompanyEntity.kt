package edu.califer.recuit_crmassignment.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_table")
data class CompanyEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "name")
    var companyName: String = "",

    @ColumnInfo(name = "website")
    var companyWebsite: String = "",

    @ColumnInfo(name = "number")
    var companyPhoneNumber: String = "",

    @ColumnInfo(name = "address")
    var companyAddress: String= "",

    @ColumnInfo(name = "city")
    var companyCity: String= "",

    @ColumnInfo(name = "state")
    var companyState: String= "",

    @ColumnInfo(name = "country")
    var companyCountry: String= "",

    @ColumnInfo(name = "type")
    var companyType: String= ""
)