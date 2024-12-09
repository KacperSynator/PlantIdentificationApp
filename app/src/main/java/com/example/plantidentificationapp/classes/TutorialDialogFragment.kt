import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.plantidentificationapp.R


class TutorialDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.tutorial_dialog)

        val viewPager: ViewPager2 = dialog.findViewById(R.id.tutorial_viewpager)
        val doneButton: Button = dialog.findViewById(R.id.tutorial_done_button)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,  // Full screen width
            WindowManager.LayoutParams.WRAP_CONTENT   // Auto adjust height based on content
        )

        val slides = listOf(
            TutorialAdapter.TutorialSlide(R.drawable.add_plant_0, "1. Add new plant"),
            TutorialAdapter.TutorialSlide(R.drawable.add_plant_1, "2. Take a picture of plant and click OK"),
            TutorialAdapter.TutorialSlide(R.drawable.add_plant_2, "3. Choose identified plant"),
            TutorialAdapter.TutorialSlide(R.drawable.add_plant_3, "4. Add plant to your collection")
        )

        val tutorialAdapter = TutorialAdapter(slides)
        viewPager.adapter = tutorialAdapter

        doneButton.setOnClickListener {
            dismiss()
        }

        return dialog
    }
}
