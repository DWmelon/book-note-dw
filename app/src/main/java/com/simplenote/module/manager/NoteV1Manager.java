package com.simplenote.module.manager;

import android.text.TextUtils;

import com.simplenote.application.MyClient;
import com.simplenote.database.NoteUtils;
import com.simplenote.database.model.Note;
import com.simplenote.module.listener.OnDataLoadFinishListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by melon on 2017/1/3.
 */

public class NoteV1Manager {

    private List<Note> noteModels = new ArrayList<>();

    private List<Note> deleteNoteModels = new ArrayList<>();

    private HashMap<Date,List<Note>> hashMapNotes = new HashMap<>();

    private AtomicBoolean isLoadFinish = new AtomicBoolean(false);

    public void init(){
        getNoteFromDatabase();
    }

    private List<OnDataLoadFinishListener> onDataLoadFinishListenerList = new ArrayList<>();

    public void registerDataLoadListener(OnDataLoadFinishListener listener){
        onDataLoadFinishListenerList.add(listener);
    }

    public void unregisterDataLoadListener(OnDataLoadFinishListener listener){
        onDataLoadFinishListenerList.remove(listener);
    }

    public void dispatDataLoadListener(){
        for (OnDataLoadFinishListener listener : onDataLoadFinishListenerList){
            listener.loadDataFinish();
        }
    }

    public List<Note> getNoteModels() {
        return noteModels;

    }

    public Note getNoteModels(String noteId) {
        if (TextUtils.isEmpty(noteId)){
            return null;
        }
        for (Note model : noteModels){
            if (model.getNoteId().equals(noteId)){
                return model.clone();
            }
        }
        for (Note model : deleteNoteModels){
            if (model.getNoteId().equals(noteId)){
                return model.clone();
            }
        }
        return null;
    }

    public List<Note> getDeleteNoteModels() {
        return deleteNoteModels;
    }

    public HashMap<Date,List<Note>> getHashMapNotes(){
        if (!hashMapNotes.isEmpty()){
            return hashMapNotes;
        }

        List<Note> noteModelList = getNoteModels();
        if (noteModelList.isEmpty()){
            return new HashMap<>();
        }

        Collections.sort(noteModelList, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                return lhs.getCreateDate().compareTo(rhs.getCreateDate());
            }
        });

        List<Note> noteTempList = new ArrayList<>();
        Date timeTemp = null;

        for (Note model : noteModelList){
            if (timeTemp == null){
                timeTemp = new Date(model.getCreateDate());
            }
            Date nowDate = new Date(model.getCreateDate());
            if (
                timeTemp.getYear()==nowDate.getYear()&&
                timeTemp.getMonth()==nowDate.getMonth()&&
                timeTemp.getDay()==nowDate.getDay()
            ){
                noteTempList.add(model);
            }else{
                hashMapNotes.put(timeTemp,noteTempList);
                timeTemp = new Date(model.getCreateDate());
                noteTempList = new ArrayList<>();
                noteTempList.add(model);
            }
        }
        if (!noteModelList.isEmpty()){
            hashMapNotes.put(timeTemp,noteTempList);
        }

        return hashMapNotes;
    }

    public void insertNewNote(Note model){
        if (model.getStatus() == -1){
            return;
        }
        noteModels.add(0,model);
        hashMapNotes = new HashMap<>();

        //这里 new 是为了置空，在MyMainFragment的adapter，会重新计算hashMapNotes
    }

    public void insertOldNote(Note model){
        if (model.getStatus() == -1){
            return;
        }

        for (int i = 0; i < noteModels.size();i++){
            if (noteModels.get(i).getNoteId().equals(model.getNoteId())){
                noteModels.remove(i);
                noteModels.add(i,model);
            }
        }

        hashMapNotes = new HashMap<>();

        //这里 new 是为了置空，在MyMainFragment的adapter，会重新计算hashMapNotes
    }

    public void removeNote(String id){
        // TODO: 2017/10/20 该换数据库后，先判断有没有存在该日记
        for (Note model : noteModels){
            if (model.getNoteId().equals(id)){
                noteModels.remove(model);
                break;
            }
        }
        for (Note model : deleteNoteModels){
            if (model.getNoteId().equals(id)){
                deleteNoteModels.remove(model);
                break;
            }
        }
        hashMapNotes = new HashMap<>();

        //这里 new 是为了置空，在MyMainFragment的adapter，会重新计算hashMapNotes
    }

    /**
     * 非物理删除，属于逻辑删除
     * @param model
     */
    public void handleDeleteNote(Note model){
        if (!noteModels.contains(model)){
            return;
        }
        model.setStatus(-1);
        noteModels.remove(model);
        deleteNoteModels.add(model);
        hashMapNotes.clear();
        NoteUtils.handleData(NoteUtils.OPERATE_UPDATE,model,true);

    }

    public boolean isLoadFinish(){
        return isLoadFinish.get();
    }

    public void getNoteFromDatabase() {
        Observable
                .just(MyClient.getMyClient().getAccountManager().getUserId())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<Long>() {
                    @Override
                    public void call(Long value) {
                        noteModels = NoteUtils.getNoteByState(value,0);
                        deleteNoteModels = NoteUtils.getNoteByState(value,-1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        hashMapNotes = new HashMap<>();
                        isLoadFinish.set(true);

                        dispatDataLoadListener();
                    }
                });
//        TaskExecutor.getInstance().post(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (NoteV1Manager.class) {
//
//                    List<Note> noteModelList = NoteUtils.getNoteByState(MyClient.getMyClient().getAccountManager().getUserId(),0);
//                    List<Note> deleteNotelList  = NoteUtils.getNoteByState(MyClient.getMyClient().getAccountManager().getUserId(),-1);
//
//
//                    noteModels = noteModelList;
//                    deleteNoteModels = deleteNotelList;
//                    hashMapNotes = new HashMap<>();
//                    isLoadFinish.set(true);
//
//                    dispatDataLoadListener();
//                }
//            }
//        });


    }
}
