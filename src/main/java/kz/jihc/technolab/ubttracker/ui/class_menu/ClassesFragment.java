package kz.jihc.technolab.ubttracker.ui.class_menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.jihc.technolab.ubttracker.R;
import kz.jihc.technolab.ubttracker.interfaces.RecyclerItemClickListener;
import kz.jihc.technolab.ubttracker.ui.class_menu.activities.OneClassActivity;
import kz.jihc.technolab.ubttracker.ui.class_menu.adapters.ClassListAdapter;
import kz.jihc.technolab.ubttracker.ui.class_menu.models.ClassModel;

import static kz.jihc.technolab.ubttracker.MainActivity.setTitle;

public class ClassesFragment extends Fragment implements View.OnClickListener {
    View view;
    ArrayList<ClassModel> classModelList;
    ClassListAdapter classListAdapter;
    RecyclerView.LayoutManager linearLayoutManager, gridLayoutManager;
    Dialog addGroupDialog;
    Button addGroupBtn;
    EditText groupName;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    SQLiteDatabase sqdb;
    Dialog d;
    int position;

    @BindView(R.id.groupRecyclerView)
    RecyclerView groupRecyclerView;
    @BindView(R.id.fabBtn)
    FloatingActionButton fabBtn;
    @BindView(R.id.llProgressBar)
    View progressLoading;

    public ClassesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classes, container, false);
        ButterKnife.bind(this, view);
        setTitle(getString(R.string.classTitle));

        initViews();

        return view;
    }

    public void initViews() {
        classModelList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        groupRecyclerView.setLayoutManager(gridLayoutManager);
        groupRecyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("class_list");
        fabBtn.setOnClickListener(this);

        classModelList.add(new ClassModel("", "11A", 20, 100));
        classModelList.add(new ClassModel("", "11B", 25, 150));
        classModelList.add(new ClassModel("", "11C", 23, 170));

        if (isOnline()) {
            addClassesListener();
            classListAdapter = new ClassListAdapter(getActivity(), classModelList);
            groupRecyclerView.setAdapter(classListAdapter);
            groupRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), groupRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int pos) {

                            Intent intent = new Intent(getActivity(), OneClassActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("className", classModelList.get(pos).getGroup_name());
                            bundle.putSerializable("classId", classModelList.get(pos).getGroup_id());
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }

                        @Override
                        public void onLongItemClick(View view, int pos) {
                            d = new Dialog(getActivity());
                            position = pos;

                            d.setContentView(R.layout.dialog_edit);
                            LinearLayout deleteLayout = d.findViewById(R.id.deleteLayout);
                            deleteLayout.setOnClickListener(ClassesFragment.this);
                            d.show();
                        }
                    })
            );
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteLayout:
                new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                        .setTitle(classModelList.get(position).getGroup_name())
                        .setMessage(getString(R.string.del_profile_subjects))

                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String groupId = classModelList.get(position).getGroup_id();
                                databaseReference.child(groupId).removeValue();

                                Toast.makeText(getActivity(), getString(R.string.profile_subjects_deleted), Toast.LENGTH_SHORT).show();
                                d.dismiss();

                                classModelList.remove(position);
                                classListAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNeutralButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;

            case R.id.fabBtn:
                addGroupDialog = new Dialog(getActivity());
                addGroupDialog.setContentView(R.layout.activity_add_class);

                groupName = addGroupDialog.findViewById(R.id.groupName);
                progressBar = addGroupDialog.findViewById(R.id.progressBar);

                addGroupBtn = addGroupDialog.findViewById(R.id.addBtn);
                addGroupBtn.setOnClickListener(this);

                addGroupDialog.show();

                break;

            case R.id.addBtn:
                String gName = groupName.getText().toString();
                String gId = getFId();

                if (!TextUtils.isEmpty(gName)) {

                    addGroupBtn.setVisibility(View.GONE);
                    ClassModel groups = new ClassModel(gId, gName, 0, 0);
                    databaseReference.child(gId).setValue(groups).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), getString(R.string.class_added), Toast.LENGTH_SHORT).show();
                            addGroupDialog.dismiss();
                        }
                    });

                } else {

                    groupName.setError(getString(R.string.enter_group_error));
                }

                break;
        }
    }

    ClassModel classModel;

    public void addClassesListener() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClassModel classModel = dataSnapshot.getValue(ClassModel.class);
                int ratingN = classModel.getSum_point() / (classModel.getPerson_count() == 0 ? 1 : classModel.getPerson_count());

                Log.d("M_GroupsFragment", "group name: " + classModel.getGroup_name());
                Log.d("M_GroupsFragment", "group sum point: " + ratingN);

                classModelList.add(classModel);
                Collections.sort(classModelList, ClassModel.groupPlace);
                classListAdapter.notifyDataSetChanged();
                progressLoading.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                classModel = dataSnapshot.getValue(ClassModel.class);
                for (int i = 0; i < classModelList.size(); i++) {
                    if (classModelList.get(i).getGroup_id().equals(classModel.getGroup_id())) {
                        classModelList.set(i, classModel);
                        Collections.sort(classModelList, ClassModel.groupPlace);
                        classListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                classModel = dataSnapshot.getValue(ClassModel.class);
                for (int i = 0; i < classModelList.size(); i++) {
                    if (classModelList.get(i).getGroup_id().equals(classModel.getGroup_id())) {
                        classModelList.remove(i);
                        Collections.sort(classModelList, ClassModel.groupPlace);
                        classListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean isOnline() {
        if (isNetworkAvailable()) {
            return true;

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.inetConnection), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getFId() {
        Date date = new Date();
        String idN = "i" + date.getTime();
        return idN;
    }
}
