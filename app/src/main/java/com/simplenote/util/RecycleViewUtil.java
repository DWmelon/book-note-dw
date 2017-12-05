package com.simplenote.util;

import android.content.Context;

import com.simplenote.R;
import com.simplenote.application.MyClient;
import com.simplenote.model.NoteModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by melon on 2017/1/4.
 */

public class RecycleViewUtil {

//    public static void handleNoteData(Context context,LinearLayout view){
//        view.removeAllViews();
//
//        HashMap<Date,List<NoteModel>> hashMapNotes = MyClient.getMyClient().getNoteV1Manager().getHashMapNotes();
//        if (hashMapNotes.isEmpty()){
//            return;
//        }
//
//        Set<Date> timeSet = hashMapNotes.keySet();
//        List<Date> timeArray = new ArrayList<>(timeSet);
//        Collections.sort(timeArray, new Comparator<Date>() {
//            @Override
//            public int compare(Date lhs, Date rhs) {
//                return rhs.compareTo(lhs);
//            }
//        });
//
//        for (Date date : timeArray){
//            View topLayout = LayoutInflater.from(context).inflate(R.layout.layout_home_list_header,null);
//            TextView title = (TextView)topLayout.findViewById(R.id.tv_home_list_title);
//            title.setText(date.getYear()+"-"+date.getMonth());
//            view.addView(topLayout);
//
//            List<NoteModel> noteModelList = hashMapNotes.get(date);
//            RecyclerView recyclerView = new RecyclerView(context);
//            recyclerView.setLayoutManager(new GridLayoutManager(context,2));
//            recyclerView.setAdapter(new NoteRvListAdapter(context,noteModelList));
//            int ceng = noteModelList.size()%2>0?noteModelList.size()/2+1:noteModelList.size()/2;
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ceng*context.getResources().getDimensionPixelOffset(R.dimen.home_note_list_item_real_height));
//            recyclerView.setLayoutParams(params);
//            view.addView(recyclerView);
//        }
//
//
//    }

    public static HashSet<Integer> getNoteChildHeaderIndex(){
        HashSet<Integer> indexSet = new HashSet<>();

        HashMap<Date,List<NoteModel>> hashMapNotes = MyClient.getMyClient().getNoteV1Manager().getHashMapNotes();
        int i = 0;
        if (hashMapNotes.keySet().isEmpty()){
            return indexSet;
        }

        Set<Date> timeSet = hashMapNotes.keySet();
        List<Date> timeArray = new ArrayList<>(timeSet);
        Collections.sort(timeArray, new Comparator<Date>() {
            @Override
            public int compare(Date lhs, Date rhs) {
                return rhs.compareTo(lhs);
            }
        });

        for (Date date : timeArray){
            indexSet.add(i);
            i += hashMapNotes.get(date).size()+1;
        }
        return indexSet;
    }

    public static List<Object> getNoteList(){
        List<Object> list = new ArrayList<>();
        HashMap<Date,List<NoteModel>> hashMapNotes = MyClient.getMyClient().getNoteV1Manager().getHashMapNotes();
        if (hashMapNotes.keySet().isEmpty()){
            return list;
        }

        Set<Date> timeSet = hashMapNotes.keySet();
        List<Date> timeArray = new ArrayList<>(timeSet);
        Collections.sort(timeArray, new Comparator<Date>() {
            @Override
            public int compare(Date lhs, Date rhs) {
                return rhs.compareTo(lhs);
            }
        });

        for (Date date : timeArray){
            list.add(date);
            list.addAll(hashMapNotes.get(date));
        }
        return list;
    }

    public static String getHeaderDataStr(Context context,Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String str = context.getString(R.string.note_header_date_format,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH));

        return str;

    }

}
