package org.malucky.itinerary.kategori

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_kuliner.*
import kotlinx.android.synthetic.main.activity_terdekat.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.malucky.itinerary.BaseActivity
import org.malucky.itinerary.DetailLocationActivity
import org.malucky.itinerary.Presenters.NearbyAdapter
import org.malucky.itinerary.Presenters.NearbyPresenterImp
import org.malucky.itinerary.R
import org.malucky.itinerary.Views.NearbyViews
import org.malucky.itinerary.data.Note
import org.malucky.itinerary.data.ResultsItem

class KulinerActivity : BaseActivity(), NearbyViews, NearbyAdapter.OnLocationItemClickListner {

    companion object {
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            return Intent(activity, KulinerActivity::class.java)
        }
    }

    lateinit var presenter : NearbyPresenterImp
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun getView(): Int =
        R.layout.activity_kuliner

    override fun onActivityCreated() {
        toolbar_kuliner.setTitle("Kuliner Sekitar Kamu")
        setSupportActionBar(toolbar_terdekat)

        toolbar_kuliner.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar_kuliner.setNavigationOnClickListener {
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

                    presenter.getDataKuliner(lati.toString(),lng.toString())

                }
        } else {
            ActivityCompat.requestPermissions(
                this@KulinerActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                123
            )
        }

    }

    private fun initPresenter() {
        presenter = NearbyPresenterImp(this)
    }

    override fun Success(datas: List<ResultsItem?>) {
        var adapter = NearbyAdapter(datas,this)
        rv_restaurant.layoutManager = LinearLayoutManager(applicationContext)
        rv_restaurant.adapter = adapter
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
        startActivity(intent)
    }





}
