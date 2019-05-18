package com.wgu.zbentz2.semestertracker.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.database.entities.Course;
import com.wgu.zbentz2.semestertracker.utils.listeners.CourseClickListener;

import java.util.List;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.CourseViewHolder> {

    private CourseClickListener adapterListener;
    private final LayoutInflater layoutInflater;
    private List<Course> courses;

    public CourseRecyclerViewAdapter(Context context, CourseClickListener courseClickListener) {

        layoutInflater = LayoutInflater.from(context);
        this.adapterListener = courseClickListener;

    }

    class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CourseClickListener holderListener;
        private final TextView course_card_header;
        private final TextView course_card_sub_header;
        private final TextView course_card_footer;

        private CourseViewHolder(View itemView, CourseClickListener courseClickListener) {

            super(itemView);
            this.holderListener = courseClickListener;
            itemView.setOnClickListener(this);

            course_card_header = itemView.findViewById(R.id.course_card_header);
            course_card_sub_header = itemView.findViewById(R.id.course_card_sub_header);
            course_card_footer = itemView.findViewById(R.id.course_card_footer);

        }

        @Override public void onClick(View v) {

            if (holderListener != null) {

                holderListener.onCourseClick(getCourse(getAdapterPosition()));

            }
        }
    }

    @NonNull
    @Override public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = layoutInflater.inflate(
            R.layout.course_card,
            parent,
            false
        );

        return new CourseViewHolder(itemView, adapterListener);

    }

    @Override public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        if (courses != null) {

            final Course current = getCourse(position);

            holder.course_card_header.setText(current.getName());
            holder.course_card_sub_header.setText(current.getStatus());
            holder.course_card_footer.setText(current.getStart_date() + " - " + current.getEnd_date());

        }
    }

    @Override public int getItemCount() {

        if (courses != null) {

            return this.courses.size();

        } else {

            return 0;

        }
    }

    private Course getCourse(int adapterPosition) {

        return courses.get(adapterPosition);

    }

    public void setCourses(List<Course> courses) {

        this.courses = courses;
        notifyDataSetChanged();

    }
}
