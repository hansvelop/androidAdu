package com.example.pjt_student;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//findViewById의 성능이슈...한번은 find 한단.. find된 view가 다음데 다시 사용
//해도 된다면.. 저장.. 획득해서 매번 find 안되게..
//한 항목에 find 대상이 되는 view가 여러개라면 하나하나 따로 저장 획득이 힘들어서
//wrapper 로 묶어서 퉁으로 저장 획득하려고..
public class MainListWrapper {
    //항목에 나오는 모든 view를 선언할 필요 없다.. find 대상이 되는 애들만..
    ImageView studentImageView;
    TextView nameView;
    ImageView phoneView;

    //한번은 find 해야 한다.. find 하려면 view 계층의 root가 있어야 한다..
    //adapter가 전달할거다..
    //가정 1 : adapter에서 직접 find 하지 않고.. wrapper의 view를 획득해서 이용
    //가정 2 : 이 wrapper 객체가 메모리에 지속된다는 가정..
    public MainListWrapper(View root){
        studentImageView=root.findViewById(R.id.main_item_student_image);
        nameView=root.findViewById(R.id.main_item_name);
        phoneView=root.findViewById(R.id.main_item_contact);
    }
}
