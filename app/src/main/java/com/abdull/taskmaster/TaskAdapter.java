package com.abdull.taskmaster;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.abdull.taskmaster.models.Task;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class TaskAdapter extends ListAdapter<Task, TaskAdapter.ViewHolder> {

    private OnTaskClickListener mOnTaskClickListener;
    private ImageButton mStartedImg;
    private int mCurrentSelectedPosition = -1;
    private boolean notifyIt = false;


    interface OnTaskClickListener {
        void onEditClick(@NonNull Task task);

        void onDeleteClick(@NonNull Task task);

        void onStartTaskClicked(@NonNull Task task);

        void onShowReportClicked(@NonNull Task task);

    }

    public TaskAdapter(OnTaskClickListener onTaskClickListener) {
        super(DIFF_CALLBACK);
        if (onTaskClickListener == null) {
            throw new ClassCastException("Must implement OnTaskClickListener");
        }
        mOnTaskClickListener = onTaskClickListener;
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getSortOrder() == newItem.getSortOrder();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Task task = getItem(position);

        holder.name.setText(task.getName());
        holder.description.setText(task.getDescription());


        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Inflate the popup menu
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.menu_popup);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menutask_edit:
                                if (mOnTaskClickListener != null) {
                                    mOnTaskClickListener.onEditClick(task);
                                }
                                return true;
                            case R.id.menutask_delete:
                                if (mOnTaskClickListener != null) {
                                    mOnTaskClickListener.onDeleteClick(task);
                                }
                                return true;
                            case R.id.menutask_report:
                                if (mOnTaskClickListener != null) {
                                    mOnTaskClickListener.onShowReportClicked(task);
                                }
                                return true;
                            default:
//                                Log.d(TAG, "onClick: found unexpected button id");
                                throw new IllegalArgumentException("Unexpected button is found " + id);
                        }
                    }
                });
                popupMenu.show();


            }
        });


        holder.startTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton imageButton = (ImageButton) v;
                if (mOnTaskClickListener != null) {
                    mOnTaskClickListener.onStartTaskClicked(task);
                    if(imageButton != mStartedImg){
                        if(mStartedImg != null) {
                            mStartedImg.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        }
                    }
                    if (!task.isRunning()) {
                        imageButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    } else {
                        mStartedImg = imageButton;
                        imageButton.setImageResource(R.drawable.ic_baseline_stop_red_24);
                    }
                }
            }
        });


        holder.startTiming.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        if(task.isRunning()){
            mStartedImg = holder.startTiming;
            holder.startTiming.setImageResource(R.drawable.ic_baseline_stop_red_24);
        }


    }


    public Task getTaskAt(int position) {
        return getItem(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        ImageButton moreOptions;
        ImageButton startTiming;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tli_name2);
            this.description = itemView.findViewById(R.id.tli_description2);
            this.moreOptions = itemView.findViewById(R.id.tli_more);
            this.startTiming = itemView.findViewById(R.id.tli_start);
            this.itemView = itemView;

        }


    }
}
