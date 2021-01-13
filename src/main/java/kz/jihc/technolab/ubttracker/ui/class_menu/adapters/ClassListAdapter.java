package kz.jihc.technolab.ubttracker.ui.class_menu.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import kz.jihc.technolab.ubttracker.R;
import kz.jihc.technolab.ubttracker.interfaces.ItemClickListener;
import kz.jihc.technolab.ubttracker.ui.class_menu.models.ClassModel;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.MyTViewHolder> {

    private Context context;
    private List<ClassModel> groupList;
    TypedArray gradientStore;

    public class MyTViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groupName, personCount, groupPoint;
        RelativeLayout gradientLayout;
        ItemClickListener clickListener;

        public MyTViewHolder(View view) {
            super(view);
            groupName = view.findViewById(R.id.groupName);
            personCount = view.findViewById(R.id.personCount);
            groupPoint = view.findViewById(R.id.groupPoint);
            gradientLayout = view.findViewById(R.id.gradientLayout);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.clickListener.onItemClick(view, getLayoutPosition());
        }

        public void setOnClick(ItemClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }

    public ClassListAdapter(Context context, List<ClassModel> groupList) {
        this.context = context;
        this.groupList = groupList;
        gradientStore = context.getResources().obtainTypedArray(R.array.gradientStore);
    }

    @Override
    public MyTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class2, parent, false);

        return new MyTViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyTViewHolder holder, int position) {

        final ClassModel item = groupList.get(position);

        holder.groupName.setText(item.getGroup_name());
        holder.personCount.setText("" + item.getPerson_count());
        holder.groupPoint.setText("" + (item.getSum_point() / (item.getPerson_count() == 0?1:item.getPerson_count())));

        int gradientBack = gradientStore.getResourceId(position, 0);

        holder.gradientLayout.setBackground(context.getResources().getDrawable(gradientBack));

//        holder.setOnClick(new ItemClickListener() {
//            @Override
//            public void onItemClick(View v, int pos) {
//
//                Intent intent = new Intent(context, GroupUsersActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("groupName", item.getGroup_name());
//                bundle.putSerializable("groupId", item.getGroup_id());
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

}