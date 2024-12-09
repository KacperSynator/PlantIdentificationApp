import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantidentificationapp.R

class TutorialAdapter(private val slides: List<TutorialSlide>) :
    RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder>() {

    data class TutorialSlide(val imageRes: Int, val description: String)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tutorial_slide, parent, false)
        return TutorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: TutorialViewHolder, position: Int) {
        val slide = slides[position]
        holder.bind(slide)
    }

    override fun getItemCount() = slides.size

    class TutorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.tutorial_image)
        private val textView: TextView = itemView.findViewById(R.id.tutorial_text)

        fun bind(slide: TutorialSlide) {
            imageView.setImageResource(slide.imageRes)
            textView.text = slide.description
        }
    }
}
