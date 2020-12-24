package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import raymondhernandez.pocketuniv.Model.Assignment;
import raymondhernandez.pocketuniv.Model.ChatList;
import raymondhernandez.pocketuniv.Model.ChatMessage;
import raymondhernandez.pocketuniv.R;

/**
 * Created by Chachi on 4/19/2016.
 */
public class AssignmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Assignment> assignments;
    Context context;

    public AssignmentAdapter(Context mContext, ArrayList<Assignment> assignments) {
        this.assignments = assignments;
        this.context = mContext;
    }

    public static class AssignmentViewHolder extends RecyclerView.ViewHolder{

        public TextView assignName;
        public TextView dueDate;
        public AssignmentViewHolder(View itemView) {
            super(itemView);
            assignName = (TextView)itemView.findViewById(R.id.rv_assign_name);
            dueDate = (TextView)itemView.findViewById(R.id.rv_deadline);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.assignment_list, parent, false);
        return new AssignmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AssignmentViewHolder assignmentViewHolder = (AssignmentViewHolder) holder;
        configureChatListViewHolder(assignmentViewHolder,position);
    }


    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public void configureChatListViewHolder(final AssignmentViewHolder holder,int pos){
        final Assignment assignment = assignments.get(pos);
        holder.assignName.setText(assignment.getName());

        Long timestamp = assignment.getDeadline();
        if(timestamp!= null) {

            Date date = new Date(timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = formatter.format(date);

            holder.dueDate.setText("Due: " + dateString);
        }
    }
}
