package com.bearever.baselib.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import com.bearever.baselib.ui.ToastHelper;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 日历工具 方法调用前要判定是否有权限
 * created by JiangHua on 2019/3/13
 */
public class CalendarsReminderUtil {
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "kuXue";
    private static String CALENDARS_ACCOUNT_NAME = "1286526741@qq.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.kuXue";
    private static String CALENDARS_DISPLAY_NAME = "kuXue账户";

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDER_URL), null, null, null, null);
        try {
            if (userCursor == null) { //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 添加日历事件
     *
     * @param title        时间显示名称
     * @param description  时间描述
     * @param reminderTime 开始时间
     * @param previousDate 提前多久提醒 分钟
     */
    public static void addCalendarEvent(Context context, String title, String description, long reminderTime, int previousDate) {
        if (context == null) {
            return;
        }
        int calId = checkAndAddCalendarAccount(context); //获取日历账户的id
        if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
            return;
        }

        //添加日历事件
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(reminderTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(start + 10 * 60 * 1000);//设置终止时间，开始时间加10分钟
        long end = mCalendar.getTime().getTime();
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, title);//事件title
        event.put(CalendarContract.Events.DESCRIPTION, description);//事件描述
        event.put(CalendarContract.Events.CALENDAR_ID, calId); //插入账户的id
        event.put(CalendarContract.Events.DTSTART, start);//开始时间
        event.put(CalendarContract.Events.DTEND, end);//结束时间
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());//这个是时区，必须有eg:"Asia/Shanghai"
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDER_EVENT_URL), event); //添加事件
        if (newEvent == null) { //添加日历事件失败直接返回
            return;
        }

        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        values.put(CalendarContract.Reminders.MINUTES, previousDate);// 提前xx分钟有提醒  previousDate * 24 * 60
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
        if (uri == null) { //添加事件提醒失败直接返回
            ToastHelper.showToast(context, "setting fail");
            return;
        }

        ToastHelper.showToast(context, "setting success");
    }

    /**
     * 删除日历事件
     */
    public static void deleteCalendarEvent(Context context, String title) {
        if (context == null) {
            return;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null) { //查询返回空值
                return;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) { //事件删除失败
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }

    /**
     * 通过事件标题、描述查询事件
     *
     * @param context     上下文
     * @param eventName   事件标题
     * @param description 事件描述
     * @return id 事件id
     */
    public static long queryCalendarsEvents(Context context, String eventName, String description) {
        long id = -1;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(CALENDER_EVENT_URL);
            cursor = context.getContentResolver().query(uri, null, null, null, null);

            if (null == cursor) {
                return id;
            }

            while (cursor.moveToNext()) {

                //事件的标题
                String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                //事件的描述
                String ds = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));


                if (eventName.equals(title) || description.equals(ds)) {
                    id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                    break;
                }

//                //事件的ID
//                String id = cursor.getString(cursor.getColumnIndex(CalendarContract.Events._ID)); //不同于Events.CALENDAR_ID
//                //事件的结束时间 ，如果事件是每天/周,那么就没有结束时间
//                String dtend = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTEND));
                //事件的起始时间
//                String dtstart = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART));
//                //事件的重复规律
//                String rrule = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RRULE));
//                //事件的复发日期。通常RDATE要联合RRULE一起使用来定义一个重复发生的事件的合集。
//                String rdate = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RDATE));
//                //事件是否是全天的
//                String allDay = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ALL_DAY));
//                //事件的地点
//                String location = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
//                //事件持续时间，例如“PT1H”表示事件持续1小时的状态， “P2W”指明2周的持续时间。P3600S表示3600秒
//                String duration = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DURATION));
//
//                //other
//                String last_date = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.LAST_DATE));
//                String original_id = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ORIGINAL_ID));
//                String maxReminders = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.MAX_REMINDERS));
//                String allowedReminders = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ALLOWED_REMINDERS));
            }
        } catch (Exception e) {
            return id;
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return id;
    }

    /**
     * 更新日历事件
     *
     * @param eventId      事件ID
     * @param title        时间显示名称
     * @param description  时间描述
     * @param reminderTime 开始时间
     * @param previousDate 提前多久提醒 分钟
     */
    public static void updateCalendarsEvent(Context context, long eventId, String title, String description, long reminderTime, int previousDate) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(reminderTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(start + 10 * 60 * 1000);//设置终止时间，开始时间加10分钟
        long end = mCalendar.getTime().getTime();

        ContentValues updateValues = new ContentValues();
        updateValues.put(CalendarContract.Events.TITLE, title);//事件title
        updateValues.put(CalendarContract.Events.DESCRIPTION, description);//事件描述
        updateValues.put(CalendarContract.Events.DTSTART, start);//开始时间
        updateValues.put(CalendarContract.Events.DTEND, end);//结束时间
        Uri updateUri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), eventId);
        int status = context.getContentResolver().update(updateUri, updateValues, null, null);

        if (status != 1) { //添加日历事件失败直接返回
            return;
        }

        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
        values.put(CalendarContract.Reminders.MINUTES, previousDate);// 提前xx分钟有提醒  previousDate * 24 * 60
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
        if (uri == null) { //添加事件提醒失败直接返回
            ToastHelper.showToast(context, "update fail");
        }

    }
}
