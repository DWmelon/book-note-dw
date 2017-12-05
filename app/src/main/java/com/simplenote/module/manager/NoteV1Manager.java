package com.simplenote.module.manager;

import android.text.TextUtils;

import com.simplenote.application.MyClient;
import com.simplenote.module.listener.OnDataLoadFinishListener;
import com.simplenote.model.NoteModel;
import com.simplenote.util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by melon on 2017/1/3.
 */

public class NoteV1Manager {

    private List<NoteModel> noteModels = new ArrayList<>();

    private List<NoteModel> deleteNoteModels = new ArrayList<>();

    private HashMap<Date,List<NoteModel>> hashMapNotes = new HashMap<>();

    private AtomicBoolean isLoadFinish = new AtomicBoolean(false);

    public void init(){
        getNoteFormFile();
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

    public List<NoteModel> getNoteModels() {
        return noteModels;

    }

    public NoteModel getNoteModels(String noteId) {
        if (TextUtils.isEmpty(noteId)){
            return null;
        }
        for (NoteModel model : noteModels){
            if (model.getNoteId().equals(noteId)){
                return model.clone();
            }
        }
        for (NoteModel model : deleteNoteModels){
            if (model.getNoteId().equals(noteId)){
                return model.clone();
            }
        }
        return null;
    }

    public List<NoteModel> getDeleteNoteModels() {
        return deleteNoteModels;
    }

    public boolean isExistNote(String noteId){
        if (TextUtils.isEmpty(noteId)){
            return false;
        }
        for (NoteModel model : noteModels){
            if (model.getNoteId().equals(noteId)){
                return true;
            }
        }
        return false;
    }

    public HashMap<Date,List<NoteModel>> getHashMapNotes(){
        if (!hashMapNotes.isEmpty()){
            return hashMapNotes;
        }

        List<NoteModel> noteModelList = getNoteModels();
        if (noteModelList.isEmpty()){
            return new HashMap<>();
        }

        Collections.sort(noteModelList, new Comparator<NoteModel>() {
            @Override
            public int compare(NoteModel lhs, NoteModel rhs) {
                return lhs.getCreateDate().compareTo(rhs.getCreateDate());
            }
        });

        List<NoteModel> noteTempList = new ArrayList<>();
        Date timeTemp = null;

        for (NoteModel model : noteModelList){
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

    public void insertNewNote(NoteModel model){
        if (model.getStatus() == -1){
            return;
        }
        noteModels.add(0,model);
        hashMapNotes = new HashMap<>();

        //这里 new 是为了置空，在MyMainFragment的adapter，会重新计算hashMapNotes
    }

    public void insertOldNote(NoteModel model){
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
        for (NoteModel model : noteModels){
            if (model.getNoteId().equals(id)){
                noteModels.remove(model);
                break;
            }
        }
        for (NoteModel model : deleteNoteModels){
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
    public void handleDeleteNote(NoteModel model){
        if (!noteModels.contains(model)){
            return;
        }
        model.setStatus(-1);
        noteModels.remove(model);
        deleteNoteModels.add(model);
        hashMapNotes.clear();
        MyClient.getMyClient().getAddNoteManager().saveNoteFromFile(model,true);
    }

    public boolean isLoadFinish(){
        return isLoadFinish.get();
    }

    public void getNoteFormFile() {

        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                synchronized (NoteV1Manager.class) {

                    List<NoteModel> noteModelList = new ArrayList<>();
                    List<NoteModel> deleteNotelList = new ArrayList<>();

                    String path = MyClient.getMyClient().getStorageManager().getNotePath();
                    List<String> allNotePath = FileUtil.searchFile(path);
                    for (String notePath : allNotePath){
                        Object object = FileUtil.readObjectFromPath(notePath);

                        if (object != null && object instanceof NoteModel) {
                            NoteModel model = (NoteModel)object;
                            if (model.getStatus()==0){
                                noteModelList.add(model);
                            }else{
                                deleteNotelList.add(model);
                            }

                        }
                    }
                    noteModels = noteModelList;
                    deleteNoteModels = deleteNotelList;
                    hashMapNotes = new HashMap<>();
                    isLoadFinish.set(true);

                    dispatDataLoadListener();
                }
            }
        });


    }
}
