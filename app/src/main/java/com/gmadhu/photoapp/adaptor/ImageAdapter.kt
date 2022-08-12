import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gmadhu.photoapp.MainActivity
import com.gmadhu.photoapp.R
import com.gmadhu.photoapp.adaptor.ImageData
import kotlinx.android.synthetic.main.image_adapter.view.*

class ImageAdapter(
    val mContext: MainActivity,
    val typelist: ArrayList<ImageData>,
    ) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return typelist.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(typelist[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(
            type: ImageData
        ) {
            if (type.isSelect) {
                itemView.cardview_payment!!.setBackgroundResource(R.drawable.img_selected)

            } else {
                itemView.cardview_payment!!.setBackgroundResource(R.drawable.bg_button_border)

            }
                Glide.with(mContext).load(type.image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .apply(RequestOptions.circleCropTransform()).into(itemView.image)

        }
    }


}