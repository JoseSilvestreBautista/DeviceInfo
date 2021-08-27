package com.example.deviceinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Requests those views, and binds the views to their data, by calling methods in the adapter.
 */
public class DeviceDbListAdapter extends RecyclerView.Adapter<DeviceDbListAdapter.viewHolder> {

  private Context context;
  private String[] deviceDbList;

  /**
   * Provides a reference to view used.
   */
  public class viewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;


    public viewHolder(View view) {
      super(view);
      textView = view.findViewById(R.id.textView);

    }

    public TextView getTextView() {
      return textView;
    }
  }

  /**
   * Functions like setter for the Context.
   *
   * @param context Current Context
   */
  public DeviceDbListAdapter(Context context) {
    this.context = context;
  }

  /**
   * Functions as a setter for deviceDbList.
   *
   * @param deviceDbList Holds query parameters and device info.
   */
  public void setDeviceDbList(String[] deviceDbList) {
    this.deviceDbList = deviceDbList;
    notifyDataSetChanged();
  }

  // Create new views (invoked by the layout manager)
  @NonNull
  @Override
  public DeviceDbListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);

    return new viewHolder(view);
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(@NonNull DeviceDbListAdapter.viewHolder holder, int position) {
    holder.getTextView().setText(this.deviceDbList[position]);


  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return this.deviceDbList.length;
  }

}
