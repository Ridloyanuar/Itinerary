package org.malucky.itinerary.kategori

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_terdekat.*
import org.malucky.itinerary.BaseActivity
import org.malucky.itinerary.DetailLocationActivity
import org.malucky.itinerary.Presenters.NearbyAdapter
import org.malucky.itinerary.Presenters.NearbyPresenterImp
import org.malucky.itinerary.R
import org.malucky.itinerary.Views.NearbyViews
import org.malucky.itinerary.data.ResultsItem

class TerdekatActivity : BaseActivity(), NearbyViews, NearbyAdapter.OnLocationItemClickListner {

    companion object {
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            return Intent(activity, TerdekatActivity::class.java)
        }
    }

    lateinit var presenter : NearbyPresenterImp
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun getView(): Int =
        R.layout.activity_terdekat

    override fun onActivityCreated() {
        toolbar_terdekat.setTitle("Terdekat Dengan Kamu")
        setSupportActionBar(toolbar_terdekat)

        toolbar_terdekat.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar_terdekat.setNavigationOnClickListener {
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

                    presenter.getData(lati.toString(),lng.toString())

                }
        } else {
            ActivityCompat.requestPermissions(
                this@TerdekatActivity,
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
        rv_terdekat.layoutManager = LinearLayoutManager(applicationContext)
        rv_terdekat.adapter = adapter
    }

    override fun Error(pesan: String) {
        Toast.makeText(applicationContext, ""+pesan, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: List<ResultsItem?>, position: Int) {
        val intent = Intent(this, DetailLocationActivity::class.java)
        intent.putExtra("IMAGE", item.get(position)?.icon)
        intent.putExtra("LOCATION_NAME", item.get(position)?.name)
        intent.putExtra("LOCATION_VICINITY", item.get(position)?.vicinity)
        intent.putExtra("LOCATION_RATING", item.get(position)?.rating)
        intent.putExtra("LOCATION_LAT", item.get(position)?.geometry?.location?.lat)
        intent.putExtra("LOCATION_LNG", item.get(position)?.geometry?.location?.lng)
        startActivity(intent)
    }


}
