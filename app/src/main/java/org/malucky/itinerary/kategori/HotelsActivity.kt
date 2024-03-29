package org.malucky.itinerary.kategori

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_budaya.*
import kotlinx.android.synthetic.main.activity_hotels.*
import org.malucky.itinerary.BaseActivity
import org.malucky.itinerary.DetailLocationActivity
import org.malucky.itinerary.Presenters.NearbyAdapter
import org.malucky.itinerary.Presenters.NearbyPresenterImp
import org.malucky.itinerary.R
import org.malucky.itinerary.Views.NearbyViews
import org.malucky.itinerary.data.ResultsItem

class HotelsActivity : BaseActivity(), NearbyViews, NearbyAdapter.OnLocationItemClickListner {

    lateinit var presenter : NearbyPresenterImp
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun getView(): Int = R.layout.activity_hotels

    override fun onActivityCreated() {
        toolbar_hotel.setTitle(getString(R.string.rental))
        setSupportActionBar(toolbar_budaya)

        toolbar_hotel.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar_hotel.setNavigationOnClickListener {
            finish()
        }
        initPresenter()
        initView()
    }

    private fun initView() {
        var status = ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (status == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    val lati = location?.latitude
                    val lng = location?.longitude

                    presenter.getDataHotel(lati.toString(),lng.toString())

                }
        } else {
            ActivityCompat.requestPermissions(
                this@HotelsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                123
            )
        }    }

    private fun initPresenter() {
        presenter = NearbyPresenterImp(this)
    }

    override fun Success(datas: List<ResultsItem?>) {
        var adapter = NearbyAdapter(datas,this)
        rv_hotel.layoutManager = LinearLayoutManager(applicationContext)
        rv_hotel.adapter = adapter
    }

    override fun Error(pesan: String) {
        Toast.makeText(applicationContext, ""+pesan, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: List<ResultsItem?>, position: Int) {
        val intent = Intent(this, DetailLocationActivity::class.java)
        intent.putExtra("IMAGE", item.get(position)?.photos?.get(0)?.photoReference)
        intent.putExtra("PLACE_ID", item.get(position)?.placeId)
        intent.putExtra("LOCATION_NAME", item.get(position)?.name)
        intent.putExtra("LOCATION_VICINITY", item.get(position)?.vicinity)
        intent.putExtra("LOCATION_RATING", item.get(position)?.rating.toString())
        intent.putExtra("LOCATION_LAT", item.get(position)?.geometry?.location?.lat)
        intent.putExtra("LOCATION_LNG", item.get(position)?.geometry?.location?.lng)
        startActivity(intent)    }


}
