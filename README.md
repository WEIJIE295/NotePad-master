# NotePad应用
功能如下：
* 添加时间戳 
* 查询框
* 修改背景色 
* 闹钟提醒
* 文件导出
## 一.添加时间戳 
### 1.修改noteslist_item.xml中的样式，增加要显示时间戳的TextView
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
### 3.修改SimpleCursorAdapter的dataColumns和viewIDs的相关值
```java
final String[] dataColumns = { NotePad.Notes.COLUMN_NAME_CREATE_DATE, NotePad.Notes.COLUMN_NAME_TITLE } ;
int[] viewIDs = { android.R.id.text1,android.R.id.text2 };
```
### 4.发现这样显示的时间戳格式不对，查看原因原来存进数据库时的所有的DATE值都是LONG，于是我增加DateUtil.java这个类
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
### 5.在NotePadProvider.java这个的存储中作出修改，改为自己格式化的字符串
```java
if (values.containsKey(NotePad.Notes.COLUMN_NAME_CREATE_DATE) == false) {
            values.put(NotePad.Notes.COLUMN_NAME_CREATE_DATE, getTodayTimeStampString());
        }
if (values.containsKey(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE) == false) {
            values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, getTodayTimeStampString());        
```
### 6.成功显示时间戳
