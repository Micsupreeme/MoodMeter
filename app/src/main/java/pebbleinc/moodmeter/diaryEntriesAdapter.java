package pebbleinc.moodmeter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class diaryEntriesAdapter extends RecyclerView.Adapter<diaryEntriesAdapter.ViewHolder> {
    private ArrayList<String> allTimes, allBodies;

     diaryEntriesAdapter(ArrayList<String> times, ArrayList<String> bodies) {
         allTimes = times;
         allBodies = bodies;
     }

    /**
     * Initialises a new diary entry
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View diaryEntry = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_diary_entry, parent, false);
        return new ViewHolder(diaryEntry);
    }

    /**
     * Sets the text for the GUI elements for a diary entry
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("position: " + position);

        String displayTime = (allTimes.get(position));
        String displayBody = (allBodies.get(position));

        holder.lblTime.setText(displayTime);
        holder.lblBody.setText(displayBody);
    }

    /**
     * @return The number of diary entries for today
     */
    @Override
    public int getItemCount() {
        return allTimes.size();
    }

    /**
     * This class extends RecyclerView.ViewHolder to define diary entry-specific
     * GUI elements for each diary entry. This enables all of the GUI elements that makes up
     * each diary entry to be accessed.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView lblTime, lblBody;

        ViewHolder(View itemView) {
            super(itemView);
            //Provides access to the GUI elements for each high score entry
            lblTime = itemView.findViewById(R.id.lblTime);
            lblBody = itemView.findViewById(R.id.lblBody);
        }
    }

}
