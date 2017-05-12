# NotePad应用
功能如下：
* 添加时间戳 
* 查询框
* 修改背景色 
* 闹钟提醒
* 文件导出
## 一.添加时间戳 
### 1.修改noteslist_item.xml中的样式，增加要显示时间戳的TextView。
```java
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
```
### 2.进入notelist.java PROJECTION 契约类的变量值加一列。
```java
private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE,// 1
            //加上时间
            NotePad.Notes.COLUMN_NAME_CREATE_DATE //2

    };
```
### 3.修改SimpleCursorAdapter的dataColumns和viewIDs的相关值。
```java
final String[] dataColumns = { NotePad.Notes.COLUMN_NAME_CREATE_DATE, NotePad.Notes.COLUMN_NAME_TITLE } ;
int[] viewIDs = { android.R.id.text1,android.R.id.text2 };
```
### 4.发现这样显示的时间戳格式不对，查看原因原来存进数据库时的所有的DATE值都是LONG，于是我增加DateUtil.java这个类。
```java
public class DateUtil
{
    private static String defaultDatePattern = "yyyy-MM-dd";

    private static String defaultTimeStampPattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 返回默认格式的当前日期
     *
     * @return String
     */
    public static String getToday()
    {
        Date today = new Date();
        return convertDateToString(today,defaultDatePattern);
    }

    /**
     * 返回默认格式的当前时间戳字符串格式
     *
     * @return String
     */
    public static String getTodayTimeStampString()
    {
        Date today = new Date();
        return convertDateToString(today, defaultTimeStampPattern);
    }

    /**
     * 使用默认格式转换Date成字符串
     *
     * @param date
     * @return String
     */
    public static String convertDateToString(Date date)
    {
        return convertDateToString(date, defaultDatePattern);
    }

    /**
     * 使用指定格式转换Date成字符串
     *
     * @param date
     * @param pattern
     * @return String
     */
    public static String convertDateToString(Date date, String pattern)
    {
        String returnValue = "";

        if (date != null)
        {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return returnValue;
    }
}
```
### 5.在NotePadProvider.java这个的存储中作出修改，改为自己格式化的字符串。
```java
if (values.containsKey(NotePad.Notes.COLUMN_NAME_CREATE_DATE) == false) {
            values.put(NotePad.Notes.COLUMN_NAME_CREATE_DATE, getTodayTimeStampString());
        }
if (values.containsKey(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE) == false) {
            values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, getTodayTimeStampString());        
```
### 6.成功显示时间戳
-------
![Alt text](https://github.com/linylx/NotePad-master/blob/master/img/1.png)
-------
## 二.查询框
### 要做搜索框我们先要了解NotesList它extends的ListActivity到底是什么？
#### ListActivity可以看做ListView和Activity的结合，主要是比较方便。但在实现时，有几点要注意。
* ListActivity可以不用setContentView(R.layout.main)，它默认是LIstView占满屏。
* 如果想在屏幕中显示其他控件，可以采用如下方法：1.代码中添加：setContentView(R.layout.main)2.在xml文件中添加一个LIstView控件和一个SearchView控件，注意LIstView控件id必须为"@id/Android:list"表示匹配的ListView  
### 1.创建一个新的search.xml，里面放上我用作搜索功能的SearchView，并在queryHint加上提示信息。
```java
        <SearchView
            android:id="@+id/searchView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:iconifiedByDefault="false"
            android:queryHint="请输入搜索内容" />
```
### 2.回到NotesList.java，创建一个SearchView成员变量：
```java
    SearchView mSearch;
```
###   在onCreate方法中，显示和获取searchview控件
```java
    setContentView(R.layout.search);
    mSearch = (SearchView)findViewById(R.id.searchView);
```
###   初始化searchview控件
```java
    if(mSearch==null){
            return;
        }else {
            //获取ImageView的id
            int imgId = mSearch.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
            //获取到TextView的ID
            int id = mSearch.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
            //获取ImageView
            ImageView searchButton = (ImageView) mSearch.findViewById(imgId);
            //获取到TextView的控件
            TextView textView = (TextView) mSearch.findViewById(id);
            //设置图片
            searchButton.setImageResource(R.drawable.search);
            //不使用默认
            mSearch.setIconifiedByDefault(false);
            textView.setTextColor(getResources().getColor(R.color.Black));
        }
```
###   设置当点击搜索按钮时触发的方法和搜索内容改变时触发的方法
```java
    mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){

                    Cursor cursor = managedQuery(
                            getIntent().getData(),            // Use the default content URI for the provider.
                            PROJECTION,                       // Return the note ID and title for each note.
                            NotePad.Notes.COLUMN_NAME_TITLE+ " LIKE '%"+newText+"%' ",                             // 相当于where语句
                            null,                             // No where clause, therefore no where column values.
                            NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
                    );
                    final String[] dataColumn = { NotePad.Notes.COLUMN_NAME_CREATE_DATE, NotePad.Notes.COLUMN_NAME_TITLE } ;

                    // The view IDs that will display the cursor columns, initialized to the TextView in
                    // noteslist_item.xml
                    int[] viewID = {android.R.id.text1,android.R.id.text2 };
                    SimpleCursorAdapter adapter1
                            = new SimpleCursorAdapter(
                            NotesList.this,                             // The Context for the ListView
                            R.layout.noteslist_item,          // Points to the XML for a list item
                            cursor,                           // The cursor to get items from
                            dataColumn,
                            viewID
                    );
                    //重新setListAdapter
                    setListAdapter(adapter1);
                }else{
                    //恢复默认setListAdapter
                    setListAdapter(adapter);
                }
                return false;
            }
        });
    }
```
###  在这其中我们对managedQuery()进行分析就比较容易了，它的参数有：
* URI:  Content Provider 需要返回的资源索引
* Projection: 用于标识有哪些columns需要包含在返回数据中。
* Selection: 作为查询符合条件的过滤参数，类似于SQL语句中Where之后的条件判断。
* SelectionArgs: 同上。
* SortOrder: 用于对返回信息进行排序。
###  在上述searchview方法中我们在managedQuery()作出以下调整：
```java
    Cursor cursor = managedQuery(
        getIntent().getData(),            
        PROJECTION,                       
        NotePad.Notes.COLUMN_NAME_TITLE+ " LIKE '%"+newText+"%' ",// 相当于where语句
        null,
        NotePad.Notes.DEFAULT_SORT_ORDER
    );
```
###  再重新setListAdapter，就可以得到一个根据SearchView中输入文字改变而实时刷新的一个搜索界面了
-------
![Alt text](https://github.com/linylx/NotePad-master/blob/master/img/2.png)
-------
-------
![Alt text](https://github.com/linylx/NotePad-master/blob/master/img/3.png)
-------

## 三.修改背景色
###  1.首先思考用户修改过的背景色应该保存，在下次打开应用时自动设置，于是我运用了SharedPreferences
#### 获取SharedPreferences的两种方式:
* 调用Context对象的getSharedPreferences()方法
* 调用Activity对象的getPreferences()方法
#### 两种方式的区别:
* 调用Context对象的getSharedPreferences()方法获得的SharedPreferences对象可以被同一应用程序下的其他组件共享.
* 调用Activity对象的getPreferences()方法获得的SharedPreferences对象只能在该Activity中使用.
###  2.创建一个SharedPreferences变量
```java
    private SharedPreferences sp;
```
###  3.设置初始的颜色，如果存在就设置为之前设置过的背景色，如果不存在就设置为浅黄色(#FFFFE0)
```java
        sp = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        preferencescolor=sp.getString("color", "");
        //设置Listview的背景色
        if(preferencescolor.equals("")){
            getListView().setBackgroundColor(Color.parseColor("#FFFFE0"));
        }
        else {
            getListView().setBackgroundColor(Color.parseColor(preferencescolor));
        }
```
###  4. 创建一个Button变量，并在onCreate()中获取控件同时设置点击事件
```java
    private Button btn_color;
```
```java
        btn_color=(Button)findViewById(R.id.background);
        btn_color.setOnClickListener(new ClickEvent());
```
###  5. 点击事件
```java
    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick (View v)  {
        }
    }
```
###  6.这个时候我想用点击事件触发一个AlertDialog，于是我先创建一个changecolor.xml
```java
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:orientation="horizontal"
        android:gravity="center"
        android:background="@color/Black">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="更 改 背 景"
            android:textSize="20dp"
            android:textColor="@color/White"/>
    </LinearLayout>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:orientation="horizontal"
        android:clickable="true">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#4169E1"
            android:id="@+id/RoyalBlue"
            android:clickable="true"
            android:layout_weight="1"/>
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#98FB98"
            android:id="@+id/PaleGreen"
            android:layout_weight="1"/>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#F0E68C"
            android:id="@+id/Khaki"
            android:layout_weight="1"/>
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#D3D3D3"
            android:id="@+id/LightGrey"
            android:layout_weight="1"/>
    </LinearLayout>
</LinearLayout>
```
###  7.在点击事件中绑定颜色按钮点击事件，设置颜色点击事件监听
```java
            final AlertDialog alertDialog = new AlertDialog.Builder(NotesList.this).create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.changecolor);

            TextView color_RoyalBlue = (TextView)alertDialog.getWindow().findViewById(R.id.RoyalBlue);
            TextView color_PaleGreen = (TextView)alertDialog.getWindow().findViewById(R.id.PaleGreen);
            TextView color_Khaki = (TextView)alertDialog.getWindow().findViewById(R.id.Khaki);
            TextView color_LightGrey = (TextView)alertDialog.getWindow().findViewById(R.id.LightGrey);

            color_RoyalBlue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferencescolor="#4169E1";
                    getListView().setBackgroundColor(Color.parseColor(preferencescolor));
                    putColor(preferencescolor);
                    alertDialog.dismiss();
                }
            });

            color_PaleGreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferencescolor="#98FB98";
                    getListView().setBackgroundColor(Color.parseColor(preferencescolor));
                    putColor(preferencescolor);
                    alertDialog.dismiss();
                }
            });

            color_Khaki.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferencescolor="#F0E68C";
                    getListView().setBackgroundColor(Color.parseColor(preferencescolor));
                    putColor(preferencescolor);
                    alertDialog.dismiss();
                }
            });

            color_LightGrey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferencescolor="#D3D3D3";
                    getListView().setBackgroundColor(Color.parseColor(preferencescolor));
                    putColor(preferencescolor);
                    alertDialog.dismiss();
                }
            });

```
###  8.其中使用SharedPreferences保存颜色的方法putColor()如下：
```java
    private void putColor(String color){
        SharedPreferences preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("color", color);
        editor.commit();
    }
```
###  9.做出来的效果图
