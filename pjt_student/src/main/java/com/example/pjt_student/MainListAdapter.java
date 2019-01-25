package com.example.pjt_student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
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
    public MainListAdapter(Context context, int resId,
                           ArrayList<StudentVO> datas){
        super(context, resId);
        this.context=context;
        this.resId=resId;
        this.datas=datas;
    }
    //항목 갯수 판단위해 자동 호출..
    @Override
    public int getCount() {
        return datas.size();
    }

    //항목하나가 화면에 나올때 마다 호출..
    //==>항목 하나를 구성하기 위한 알로리즘..
    //==>성능 고려..
    //position : 항목 index...
    //View : 리턴되는 view 객체부터 그 하위 객체들이 해당 항목에 찍힌다..
    //convertView : 항목을 위해 재사용될 view가 없다면 null
    //항목 구성을 위해 return 시켰던 view대로 화면 출력하고 내부적으로 유지했다가
    //그 다음 이용시 전달해 준다..
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(resId, null);

            MainListWrapper wrapper=new MainListWrapper(convertView);

            //모든 view 객체에는 개발자 임의데이터(non-visible) 저장 가능..
            //그 객체가 메모리에 지속만 된다면 언제든지 획득 가능..
            convertView.setTag(wrapper);//setTag(key, value)
        }

        MainListWrapper wrapper=(MainListWrapper)convertView.getTag();

        ImageView studentImageView=wrapper.studentImageView;
        TextView nameView=wrapper.nameView;
        final ImageView phoneView=wrapper.phoneView;

        final StudentVO vo=datas.get(position);

        nameView.setText(vo.name);

        //OOM(OutOfMemoryException) 고려해서 작성..
        //이미지 데이터 사이즈를 줄여서 로딩..
        if(vo.photo != null && !vo.photo.equals("")){
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=10;//10분의 1로 줄여서 로딩..
            Bitmap bitmap=BitmapFactory.decodeFile(vo.photo, options);
            if(bitmap != null){
                studentImageView.setImageBitmap(bitmap);
            }
        }else {
            studentImageView.setImageDrawable(
                    ResourcesCompat.getDrawable(context.getResources(),
                            R.drawable.ic_student_small, null));
        }

        studentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=(LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View root=inflater.inflate(R.layout.dialog_student_image, null);
                ImageView dialogImageView=root.findViewById(R.id.dialog_image);

                if(vo.photo != null && !vo.photo.equals("")){
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inSampleSize=10;//10분의 1로 줄여서 로딩..
                    Bitmap bitmap=BitmapFactory.decodeFile(vo.photo, options);
                    if(bitmap != null){
                        dialogImageView.setImageBitmap(bitmap);
                    }
                }else {
                    dialogImageView.setImageDrawable(
                            ResourcesCompat.getDrawable(context.getResources(),
                                    R.drawable.ic_student_large, null));
                }

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setView(root);

                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        phoneView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(vo.phone != null && !vo.phone.equals("")){
                    //call app을 intent로...
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+vo.phone));
                    context.startActivity(intent);
                }
            }
        });

        return convertView;

    }
}
