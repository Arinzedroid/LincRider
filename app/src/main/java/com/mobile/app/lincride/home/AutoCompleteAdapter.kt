package com.mobile.app.lincride.home

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.mobile.app.lincride.models.CustomPlacesModel

class AutoCompleteAdapter(
    context: Context,
    private val manager: PlacesManager
) : ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line), Filterable {

    private var resultList: List<CustomPlacesModel> = listOf()

    override fun getCount() = resultList.size

    override fun getItem(position: Int): String = resultList[position].address

    fun getPlaces(position: Int) = resultList[position]

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint.isNullOrEmpty()) return results

                val query = constraint.toString()
                val predictions = manager.getPredictions(query)

                try {
                    val uniquePredictions = predictions.distinctBy { it.address }
                    resultList = uniquePredictions

                    results.values = uniquePredictions
                    results.count = uniquePredictions.size
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }


}
