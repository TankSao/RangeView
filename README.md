# RangeView
范围选择demo，可用于薪资范围选择等
</br>
效果图：</br>
![image](https://github.com/TankSao/RangeView/blob/master/image/img1.png)</br>
![image](https://github.com/TankSao/RangeView/blob/master/image/img2.png)</br>
![image](https://github.com/TankSao/RangeView/blob/master/image/img3.png)</br>
![image](https://github.com/TankSao/RangeView/blob/master/image/img4.png)</br>
相关代码:</br>
``` Android
界面布局
<com.example.administrator.rangeview.RangeView
            android:layout_marginRight="30dp"
            android:layout_marginLeft="30dp"
            android:layout_width="match_parent"
            android:id="@+id/range"
            android:layout_height="50dp"
            android:background="@color/white"/>
定义监听以及注册
public interface RangeChangeListener {
    void onRangeChange(float start,float end);
}
range.setOnRangeChangeListener(this);
@Override
    public void onRangeChange(float start, float end) {
        startNum.setText(start+"元");
        endNum.setText(end+"元");
    }
