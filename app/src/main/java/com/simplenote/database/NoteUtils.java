package com.simplenote.database;

import com.simplenote.application.MyClient;
import com.simplenote.database.model.Note;

import org.greenrobot.greendao.rx.RxDao;

import java.util.List;

import database.com.simplenote.NoteDao;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by melon on 2017/12/26.
 */

public class NoteUtils {

    public static final int OPERATE_INSERT = 1;
    public static final int OPERATE_UPDATE = 2;
    public static final int OPERATE_REMOVE = 3;

    private static NoteDao dao;
    private static RxDao<Note,String> rx;

    private static void checkRx(){
        if (rx == null){
            dao = MyClient.getMyClient().getDatabaseManager().getDaoSession().getNoteDao();
            rx = dao.rx();
        }
    }

    public static boolean isExist(String noteId){
        checkRx();
        Note note = dao.load(noteId);
        return note != null;
    }

    public static void handleData(int operate, Note model){
        handleData(operate,model,true,null);
    }

    public static void handleData(int operate, Note model,OnHandleDataFinishListener listener){
        handleData(operate,model,true,listener);
    }

    public static void handleData(int operate, final Note model, final boolean isUpdate){
        handleData(operate,model,isUpdate,null);
    }

    public static void handleData(int operate, final Note model, final boolean isUpdate, final OnHandleDataFinishListener listener){
        checkRx();
        dealOperate(operate,model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Note>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Note note) {
                        if (listener != null){
                            listener.onDataFinish();
                        }
                        if (isUpdate){
                            MyClient.getMyClient().getAddNoteManager().displayOnAddNoteListener();
                        }
                    }
                });

    }

    private static Observable<Note> dealOperate(int operate, Note note){
        switch (operate){
            case OPERATE_INSERT:{
                return insertNote(note);
            }
            case OPERATE_UPDATE:{
                return updateNote(note);
            }
            case OPERATE_REMOVE:{
                return removeNote(note);
            }
        }
        return null;
    }

    private static Observable<Note> insertNote(final Note model){
        return rx.insert(model);
    }

    private static Observable<Note> updateNote(final Note model){
        return rx.update(model);
    }

    private static Observable<Note> removeNote(final Note model){
        return rx.delete(model).map(new Func1<Void, Note>() {
            @Override
            public Note call(Void aVoid) {
                return new Note();
            }
        });
    }

    public static List<Note> getAllNote(Long userId){
        checkRx();
        return dao.queryBuilder().where(NoteDao.Properties.UserId.eq(userId)).list();
    }

    public static List<Note> getNoteByState(Long userId,int state){
        checkRx();
        return dao.queryBuilder().where(NoteDao.Properties.UserId.eq(userId)).where(NoteDao.Properties.Status.eq(state)).list();
    }

}
