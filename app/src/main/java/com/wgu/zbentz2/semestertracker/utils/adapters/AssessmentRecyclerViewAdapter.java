package com.wgu.zbentz2.semestertracker.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Assessment;
import com.wgu.zbentz2.semestertracker.utils.listeners.AssessmentClickListener;

import java.util.List;

public class AssessmentRecyclerViewAdapter extends RecyclerView.Adapter<AssessmentRecyclerViewAdapter.AssessmentViewHolder> {

    private AssessmentClickListener adapterListener;
    private final LayoutInflater layoutInflater;
    private List<Assessment> assessments;

    public AssessmentRecyclerViewAdapter(Context context, AssessmentClickListener assessmentClickListener) {

        layoutInflater = LayoutInflater.from(context);
        this.adapterListener = assessmentClickListener;

    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AssessmentClickListener holderListener;
        private final TextView assessment_card_header;
        private final TextView assessment_card_sub_header;
        private final TextView assessment_card_footer;

        private AssessmentViewHolder(View itemView, AssessmentClickListener assessmentClickListener) {

            super(itemView);
            this.holderListener = assessmentClickListener;
            itemView.setOnClickListener(this);

            assessment_card_header = itemView.findViewById(R.id.assessment_card_header);
            assessment_card_sub_header = itemView.findViewById(R.id.assessment_card_sub_header);
            assessment_card_footer = itemView.findViewById(R.id.assessment_card_footer);

        }

        @Override public void onClick(View v) {

            if (holderListener != null) {

                holderListener.onAssessmentClick(getAssessment(getAdapterPosition()));

            }

        }

    }

    @NonNull
    @Override public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(
            R.layout.assessment_card,
            parent,
            false
        );

        return new AssessmentViewHolder(itemView, adapterListener);

    }

    @Override public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {

        if (assessments != null) {

            final Assessment current = getAssessment(position);

            holder.assessment_card_header.setText(current.getName());
            holder.assessment_card_sub_header.setText(current.getType());
            holder.assessment_card_footer.setText(current.getDue_date());

        }

    }

    @Override public int getItemCount() {

        if (assessments != null) {

            return this.assessments.size();

        } else {

            return 0;

        }
    }

    private Assessment getAssessment(int adapterPosition) {

        return assessments.get(adapterPosition);

    }

    public void setAssessments(List<Assessment> assessments) {

        this.assessments = assessments;
        notifyDataSetChanged();

    }

}
