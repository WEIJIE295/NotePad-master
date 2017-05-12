# NotePad应用
功能如下：
* 添加时间戳 
* 查询框
* 修改背景色 
* 闹钟提醒
* 文件导出
## 1.添加时间戳 
修改noteslist_item.xml中的样式，增加要显示时间戳的TextView
` ``java
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:background="@color/Null">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:background="#FFFFE0">
        <TextView xmlns:android="http://schemas.android.com/apk/res/android"
            android:id="@android:id/text2"
            android:layout_marginLeft="20dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textAppearance="?android:attr/textAppearanceLarge"
            android:gravity="center_vertical"
            android:paddingLeft="5dip"
            android:textColor="#000000"
            android:textSize="22dp"
            android:singleLine="true"
            />
        <TextView xmlns:android="http://schemas.android.com/apk/res/android"
            android:id="@android:id/text1"
            android:layout_marginLeft="20dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textAppearance="?android:attr/textAppearanceLarge"
            android:gravity="center_vertical"
            android:paddingLeft="5dip"
            android:textColor="#708090"
            android:textSize="15dp"
            android:singleLine="true"
            />
    </LinearLayout>
    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/Null"/>
</LinearLayout>
` ``
