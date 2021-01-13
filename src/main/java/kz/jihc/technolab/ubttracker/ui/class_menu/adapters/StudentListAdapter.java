package kz.jihc.technolab.ubttracker.ui.class_menu.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kz.jihc.technolab.ubttracker.R;
import kz.jihc.technolab.ubttracker.ui.class_menu.models.Student;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyTViewHolder>{
    private Context context;
    public ArrayList<Student> userList;
    String[] monthStore;

    public static class MyTViewHolder extends RecyclerView.ViewHolder{
        CircleImageView person_photo;
        ImageView userTypeIcon;
        TextView userPoint, info, tv_enter_date, entAve;

        public MyTViewHolder(View view) {
            super(view);
            person_photo = view.findViewById(R.id.person_photo);
            userTypeIcon = view.findViewById(R.id.userTypeIcon);
            info = view.findViewById(R.id.info);
            userPoint = view.findViewById(R.id.userPoint);
            entAve = view.findViewById(R.id.entAve);
            tv_enter_date = view.findViewById(R.id.tv_enter_date);
        }
    }

    public StudentListAdapter(Context context, ArrayList<Student> userList) {
        this.context = context;
        this.userList = userList;
        monthStore = context.getResources().getStringArray(R.array.monthStore);
    }

    @Override
    public MyTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new MyTViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyTViewHolder holder, int position){
        Student item = userList.get(position);

        Glide.with(context.getApplicationContext())
                .load(item.getPhoto())
                .placeholder(R.drawable.user_def)
                .dontAnimate()
                .into(holder.person_photo);


        holder.userPoint.setText(""+item.getPoint());
        holder.info.setText(item.getInfo());
        holder.entAve.setText(""+item.getEntAve());

        if(item.getEnterDate().equals("not")){
            holder.tv_enter_date.setTextColor(context.getResources().getColor(R.color.gradientLightOrange2));
            holder.tv_enter_date.setText(context.getString(R.string.not_logged_in_yet));
        }else{
            String[] dateStr = item.getEnterDate().split("\\.");
            String monthStr = monthStore[Integer.parseInt(dateStr[1])-1];

            holder.tv_enter_date.setText(String.format("%s %s, %s", dateStr[0], monthStr,  dateStr[2]));
            holder.tv_enter_date.setTextColor(context.getResources().getColor(R.color.bronze2));
        }


        int uTypeIcon = R.color.transparent;

        switch (item.getUserType()) {
            case "gold":
                uTypeIcon = R.drawable.ic_gold;
                break;
            case "silver":
                uTypeIcon = R.drawable.ic_silver;
                break;
            case "bronze":
                uTypeIcon = R.drawable.ic_bronze;
                break;
        }

        holder.userTypeIcon.setImageResource(uTypeIcon);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}