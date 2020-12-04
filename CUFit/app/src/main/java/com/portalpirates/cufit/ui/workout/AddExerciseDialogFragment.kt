package com.portalpirates.cufit.ui.workout

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.workout.Exercise
import com.portalpirates.cufit.ui.FitApplication
import com.portalpirates.cufit.ui.view.FitButton


class AddExerciseDialogFragment : DialogFragment() {

    private var exerciseEntries: RecyclerView? = null
    private var adapter: AddExercisesAdapter? = null

    private var saveBtn: FitButton? = null
    private var cancelBtn: FitButton? = null

    private var onSaveClickedListener: ((List<Exercise>) -> Unit?)? = null

    private var onDialogShownListener: OnDialogShownListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_exercises_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseEntries = view.findViewById<RecyclerView>(R.id.add_exercise_entries).apply {
            layoutManager = LinearLayoutManager(context)

            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            dividerItemDecoration.setDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.divider)))
            addItemDecoration(dividerItemDecoration)
        }

        saveBtn = view.findViewById<FitButton>(R.id.save_btn).apply {
            setOnClickListener {
                val checkedExercises = adapter?.getCheckedExercises()
                checkedExercises?.let {
                    onSaveClickedListener?.invoke(it)
                }
                dismiss()
            }
        }

        cancelBtn = view.findViewById<FitButton>(R.id.cancel_btn).apply {
            setOnClickListener {
                dismiss()
            }
        }

        populateExercises()

        onDialogShownListener?.onDialogShown(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            val colorDrawable = ColorDrawable(Color.TRANSPARENT)
            val insetTop = resources.getDimensionPixelOffset(R.dimen.LU_4)
            val insetHorizontal = resources.getDimensionPixelOffset(R.dimen.LU_2)
            val insetDrawable = InsetDrawable(colorDrawable, insetHorizontal, insetTop, insetHorizontal, 0)
            setBackgroundDrawable(insetDrawable)
        }
    }

    fun populateExercises(/* exercises: List<Exercise> */) {
        // mocked
        if (adapter != null) return

        adapter = AddExercisesAdapter(FitApplication.instance.workoutManager.provider.getExercises().toMutableList())

        exerciseEntries?.adapter = adapter
    }

    fun setCheckedExercises(checkedExercises: List<Exercise>) {
        adapter?.exercises?.let { exercises ->
            for (i in 0 until exercises.size) {
                for (checkedExercise in checkedExercises) {
                    // Found checked
                    if (checkedExercise.name == exercises[i].name) {
                        exercises[i] = checkedExercise
                        adapter?.setCheckedExercise(i, true)
                    }
                }

            }
        }
    }

    fun setOnSaveClickedListener(listener: ((List<Exercise>) -> Unit?)?) {
        onSaveClickedListener = listener
    }

    fun setOnDialogShownListener(listener: OnDialogShownListener) {
        onDialogShownListener = listener
    }


    interface OnDialogShownListener {
        fun onDialogShown(dialog: AddExerciseDialogFragment)
    }

    companion object {
        const val TAG = "ExerciseDialogFragment"

        fun newInstance(b: Bundle? = null): AddExerciseDialogFragment {
            val frag = AddExerciseDialogFragment()
            frag.arguments = b
            return frag
        }
    }
}