package org.malucky.itinerary.Presenters

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Data
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.atur_list_item.view.*
import org.malucky.itinerary.R
import org.malucky.itinerary.db.CartLocation
import org.malucky.itinerary.util.NotifyWork
import org.malucky.itinerary.util.formatDateTime


class TimeLineAdapter(private val mContext: Context, private val cartLocation: List<CartLocation>, var onClickListener: OnItemClickListner) : RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    companion object {
        val ITEM_A = 1
        val ITEM_B = 2
    }

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if(!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }
//
//        val view = if (mAttributes.orientation == Orientation.HORIZONTAL) {
//            mLayoutInflater.inflate(R.layout.item_timeline_horizontal, parent, false)
//        } else {
//            mLayoutInflater.inflate(R.layout.item_timeline, parent, false)
//        }
        val view  = mLayoutInflater.inflate(R.layout.atur_list_item, parent, false)
        return TimeLineViewHolder(view, viewType, onClickListener)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = cartLocation[position]

        holder.lokasi.text = timeLineModel.namaLokasi
        holder.jarak.text = timeLineModel.jarak
        val latitude = timeLineModel.latitude
        val longitude = timeLineModel.longitude

        holder.navigasi.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?f=d&daddr=$latitude,$longitude")
            )
            mContext.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val dialog = MaterialDialog(mContext).title(null,"Pengingat")
                .message(null, "Buat Pengingat Untuk Berkunjung Ke " + timeLineModel.namaLokasi + " ?")
                .negativeButton(null,"Tidak") {
                    it.dismiss()
                }
                .positiveButton(null,"Iya",{
                    val calintent = Intent(Intent.ACTION_INSERT)
                    calintent.type = "vnd.android.cursor.item/event"
                    calintent.putExtra("eventLocation", timeLineModel.namaLokasi)
                    calintent.putExtra("title", "Liburan - iTraveller");
                    mContext.startActivity(calintent)
                    it.dismiss()
                }).noAutoDismiss().cancelable(false)
            dialog.show()
        }

//        when {
//            timeLineModel.status == OrderStatus.INACTIVE -> {
//                holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_inactive, mAttributes.markerColor)
//            }
//            timeLineModel.status == OrderStatus.ACTIVE -> {
//                holder.timeline.marker = VectorDrawableUtils.getDrawable(holder.itemView.context, R.drawable.ic_marker_active,  mAttributes.markerColor)
//            }
//            else -> {
//                holder.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker), mAttributes.markerColor)
//            }
//        }

//        if (timeLineModel.date.isNotEmpty()) {
//            holder.date.setVisible()
//            holder.date.text = timeLineModel.date.formatDateTime("yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy")
//        } else
//            holder.date.setGone()
//
//        holder.message.text = timeLineModel.message
    }

    override fun getItemCount() = cartLocation.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int, action: OnItemClickListner) : RecyclerView.ViewHolder(itemView) {

        val lokasi = itemView.txt_namaTempatAtur
        val jarak = itemView.txt_jarakAtur
        val navigasi = itemView.imageButton2
        val timeline = itemView.timelineView
        val tanggal = itemView.tanggal


        init {
            timeline.initLine(viewType)
//            timeline.markerSize = mAttributes.markerSize
//            timeline.setMarkerColor(mAttributes.markerColor)
//            timeline.isMarkerInCenter = mAttributes.markerInCenter
//            timeline.markerPaddingLeft = mAttributes.markerLeftPadding
//            timeline.markerPaddingTop = mAttributes.markerTopPadding
//            timeline.markerPaddingRight = mAttributes.markerRightPadding
//            timeline.markerPaddingBottom = mAttributes.markerBottomPadding
//            timeline.linePadding = mAttributes.linePadding
//
//            timeline.lineWidth = mAttributes.lineWidth
//            timeline.setStartLineColor(mAttributes.startLineColor, viewType)
//            timeline.setEndLineColor(mAttributes.endLineColor, viewType)
//            timeline.lineStyle = mAttributes.lineStyle
//            timeline.lineStyleDashLength = mAttributes.lineDashWidth
//            timeline.lineStyleDashGap = mAttributes.lineDashGap
            itemView.setOnClickListener {
                action.onItemClick(cartLocation, adapterPosition)
            }
        }
    }

    interface OnItemClickListner {
        fun onItemClick(item: List<CartLocation?>, position: Int)
    }
}


