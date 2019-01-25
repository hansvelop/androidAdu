package com.example.pjt_student1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainListAdapter extends ArrayAdapter<StudentVO> {
    Context context;
    ArrayList<StudentVO> datas;
    int resId;

    public MainListAdapter(Context context, int resId, ArrayList<StudentVO> datas) {
        super(context, resId);
        this.context = context;
        this.resId = resId;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            MainListWrapper wrapper = new MainListWrapper(convertView);

            convertView.setTag(wrapper);
        }
        MainListWrapper wrapper = (MainListWrapper) convertView.getTag();

        ImageView studentImageView = wrapper.studentImageView;
        TextView nameView = wrapper.nameView;
        final ImageView phoneView = wrapper.phoneView;

        final StudentVO vo = datas.get(position);

        nameView.setText(vo.name);

        if(vo.photo != null && !vo.photo.equals("")){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=10;
            Bitmap bitmap = BitmapFactory.decodeFile(vo.phone,options);
            if(bitmap != null){
                studentImageView.setImageBitmap(bitmap);
            }
        }else{
            studentImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_student_small, null));
        }

        studentImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View root = inflater.inflate(R.layout.dialog_student_image, null);
                ImageView dialogImageView = root.findViewById(R.id.dialog_image);


                if(vo.photo != null && !vo.photo.equals("")){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize=10;
                    Bitmap bitmap = BitmapFactory.decodeFile(vo.phone,options);
                    if(bitmap != null){
                        dialogImageView.setImageBitmap(bitmap);
                    }
                }else{
                    dialogImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_student_large, null));
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(root);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        phoneView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(vo.phone != null && !vo.phone.equals("")){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    Log.d("phone ", vo.phone);
                    intent.setData(Uri.parse("tel:"+vo.phone));
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

}
