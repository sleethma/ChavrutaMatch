package com.example.micha.chavrutamatch;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micha.chavrutamatch.Data.ChavrutaContract;
import com.example.micha.chavrutamatch.OpenChavrutaAdapter;

/**
 * Created by micha on 7/22/2017.
 */

public class OpenChavrutaAdapter extends RecyclerView.Adapter<OpenChavrutaAdapter.OpenChavrutaViewHolder> {


    // Holds on to the cursor to display the waitlist
    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor using the context and the db cursor
     *
     * @param context the calling context/activity
     * @param cursor  the db cursor with waitlist data to display
     */
    public OpenChavrutaAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public OpenChavrutaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.open_host_list_item, parent, false);
        return new OpenChavrutaViewHolder(view);
    }


    @Override
    public void onBindViewHolder(OpenChavrutaViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position)) {
            return; // bail if returned null
        }

        // Update the view holder with the information needed to display
        String name = mCursor.getString(mCursor.getColumnIndex(ChavrutaContract.ChavrutaHostEntry.HOST_FIRST_NAME));
        //int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));

        // Display the guest name
        holder.hostNameTextView.setText(name);
        // Display the party count
        //holder.partySizeTextView.setText(String.valueOf(partySize));
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class OpenChavrutaViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView hostNameTextView;
        // Will display the party size number
        //TextView partySizeTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link OpenChavrutaViewHolder#onCreateViewHolder(ViewGroup, int)} (ViewGroup, int)}
         */
        public OpenChavrutaViewHolder(View itemView) {
            super(itemView);
            hostNameTextView = (TextView) itemView.findViewById(R.id.host_user_name);
            //partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }
}
