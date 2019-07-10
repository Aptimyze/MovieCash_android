package jonomoneta.juno.moviecash.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.VideoPickActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jonomoneta.juno.moviecash.Activity.WorldsBestActivity;
import jonomoneta.juno.moviecash.Model.Response.GetTalentCategoryListResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Utils.Utility;


public class TalentCategoryAdapter extends RecyclerView.Adapter<TalentCategoryAdapter.ViewHolder> {

    private Context mContext;
    private Dialog dialog;
    private ArrayList<GetTalentCategoryListResponse.ResponseData> categoryList;
    ArrayList<Integer> iconList;

    public TalentCategoryAdapter(Context mContext, ArrayList<GetTalentCategoryListResponse.ResponseData> categoryList,
                                 Dialog dialog) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.dialog = dialog;
        this.iconList = new ArrayList<>();
        iconList.add(R.drawable.ic_actor);
        iconList.add(R.drawable.ic_singer);
        iconList.add(R.drawable.ic_musician);
        iconList.add(R.drawable.ic_script);
        iconList.add(R.drawable.ic_host);
        iconList.add(R.drawable.ic_artist);
        iconList.add(R.drawable.ic_photographer);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.talent_category_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.nameTV.setText(categoryList.get(i).getName());
        viewHolder.iconIV.setImageResource(iconList.get(i));

        viewHolder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog(categoryList.get(i).getID(), categoryList.get(i).getName(), dialog);

            }
        });
    }

    private void showAlertDialog(final int catId, final String catName, final Dialog mDialog) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        builder1.setTitle("Confirm");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Are you sure to upload video for " + catName + " category ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDialog.dismiss();
                        WorldsBestActivity.categoryId = catId;
                        WorldsBestActivity.catName = catName;

                        Intent intent2 = new Intent(mContext, VideoPickActivity.class);
                        intent2.putExtra(Constant.MAX_NUMBER, 1);
                        ((WorldsBestActivity)mContext).startActivityForResult(intent2, 0);
                        WorldsBestActivity.currPage = 1;
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV;
        ImageView iconIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            iconIV = itemView.findViewById(R.id.iconIV);
        }
    }
}
