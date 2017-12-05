package com.simplenote.module.add;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSONObject;
import com.simplenote.R;
import com.simplenote.application.MyClient;
import com.simplenote.model.ImageModel;
import com.simplenote.model.NoteModel;
import com.simplenote.model.UploadResultModel;
import com.simplenote.module.manager.StorageManager;
import com.simplenote.module.manager.TaskExecutor;
import com.simplenote.network.IRequest;
import com.simplenote.network.IRequestCallback;
import com.simplenote.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by melon on 2017/7/18.
 */

public class AddNoteManager {

    public static final String SUPPORT_TYPE = ".JPEG";

    public static final int TYPE_EMOTION = 1;
    public static final int TYPE_WEATHER = 2;
    public static final int TYPE_THEME = 3;

    private List<String> emotionList = new ArrayList<>();
    private List<String> weatherList = new ArrayList<>();
    private List<String> themeList = new ArrayList<>();

    private HashMap<String,ImageModel> emotionHashMap = new HashMap<>();
    private HashMap<String,ImageModel> weatherHashMap = new HashMap<>();
    private HashMap<String,ImageModel> themeHashMap = new HashMap<>();



    public AddNoteManager(){

    }

    public void init(Context context){
        emotionList = Arrays.asList(context.getResources().getStringArray(R.array.emotion));
        weatherList = Arrays.asList(context.getResources().getStringArray(R.array.weather));
        themeList = Arrays.asList(context.getResources().getStringArray(R.array.theme));

        for (int i = 0;i < weatherList.size();i++){
            ImageModel model = new ImageModel(context,0,i);
            weatherHashMap.put(weatherList.get(i),model);
        }

        for (int i = 0;i < emotionList.size();i++){
            ImageModel model = new ImageModel(context,1,i);
            emotionHashMap.put(emotionList.get(i),model);
        }

        for (int i = 0;i < themeList.size();i++){
            ImageModel model = new ImageModel(context,2,i);
            themeHashMap.put(themeList.get(i),model);
        }

    }

    private NoteModel noteModel;
    private List<String> noteImagePaths = new ArrayList<>();

    public NoteModel getNoteModel() {
        if (noteModel == null){
            noteModel = new NoteModel();
        }
        return noteModel;
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public List<String> getNoteImagePaths() {
        return noteImagePaths;
    }

    public void setNoteImagePaths(List<String> noteImagePaths) {
        this.noteImagePaths = noteImagePaths;
    }

    public void setEmotion(int index){
        if (index<0||index>=emotionList.size()){
            noteModel.setEmotion("");
            return;
        }
        setEmotion(emotionList.get(index));
    }

    public void setEmotion(String emotion){
        noteModel.setEmotion(emotion);
    }

    public int getEmotionIndex(){
        return getEmotionIndex(noteModel.getEmotion());
    }

    public int getEmotionIndex(String str){
        return emotionList.indexOf(str);
    }

    public void setWeather(int index){
        if (index<0||index>=weatherList.size()){
            noteModel.setWeather("");
            return;
        }
        setWeather(weatherList.get(index));
    }

    public void setWeather(String weather){
        noteModel.setWeather(weather);
    }

    public int getWeatherIndex(){
        return getWeatherIndex(noteModel.getWeather());
    }

    public int getWeatherIndex(String str){
        return weatherList.indexOf(str);
    }

    public void setTheme(int index){
        if (index<0||index>=themeList.size()){
            noteModel.setTheme("");
            return;
        }
        setTheme(themeList.get(index));
    }

    public void setTheme(String theme){
        noteModel.setTheme(theme);
    }

    public int getThemeIndex(){
        return getThemeIndex(noteModel.getTheme());
    }

    public int getThemeIndex(String str){
        return themeList.indexOf(str);
    }


    public int getImageResES(int type){
        return getImageResES(type,noteModel);
    }

    public int getImageResES(int type,NoteModel model){
        switch (type){
            case TYPE_EMOTION:{
                return getEmotionResES(model);
            }
            case TYPE_WEATHER:{
                return getWeatherResES(model);
            }
            case TYPE_THEME:{
                return getThemeResES(model);
            }
            default:return getEmotionResES(model);
        }
    }


    private int getEmotionResES(NoteModel noteModel){
        ImageModel model = emotionHashMap.get(noteModel.getEmotion());
        return model == null?-1:model.getExtraSmall();
    }

    private int getWeatherResES(NoteModel noteModel){
        ImageModel model = weatherHashMap.get(noteModel.getWeather());
        return model == null?-1:model.getExtraSmall();
    }

    private int getThemeResES(NoteModel noteModel){
        ImageModel model = themeHashMap.get(noteModel.getTheme());
        return model == null?-1:model.getExtraSmall();
    }

    public List<ImageModel> getImageModelList(int type){
        switch (type){
            case TYPE_EMOTION:{
                return new ArrayList<>(emotionHashMap.values());
            }
            case TYPE_WEATHER:{
                return new ArrayList<>(weatherHashMap.values());
            }
            case TYPE_THEME:{
                return new ArrayList<>(themeHashMap.values());
            }
            default:return new ArrayList<>();
        }
    }

    public boolean writeImageToFile(Bitmap bitmap, String fileName){
        String path = MyClient.getMyClient().getStorageManager().getImagePath() + fileName + SUPPORT_TYPE;
        return FileUtil.writeImageToFile(bitmap,path);
    }

    public String getImageLocalPath(String name){
        return MyClient.getMyClient().getStorageManager().getImagePath() + name + SUPPORT_TYPE;
    }

    private void handleRemoveNote(String id,boolean isUpdate){
        MyClient.getMyClient().getNoteV1Manager().removeNote(id);
        if (isUpdate){
            displayOnAddNoteListener();
        }
    }



    public void saveNoteFromFile(final NoteModel model, final boolean isUpdate){

        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                if (model != null) {
                    String path = MyClient.getMyClient().getStorageManager().getNotePath() + model.getNoteId();
                    FileUtil.writeObjectToPath(model, path);
                    if (isUpdate){
                        displayOnAddNoteListener();
                    }
                }
            }
        });
    }

    public void removeNoteFromFile(final String id, final boolean isUpdate){

        TaskExecutor.getInstance().post(new Runnable() {
            @Override
            public void run() {
                String path = MyClient.getMyClient().getStorageManager().getNotePath() + id;
                FileUtil.deleteFile(new File(path));
                handleRemoveNote(id,isUpdate);
            }
        });
    }

    private List<OnAddNoteListener> onAddNoteListenerList = new ArrayList<>();

    public void registerOnAddNoteListener(OnAddNoteListener listener){
        if (onAddNoteListenerList == null){
            onAddNoteListenerList = new ArrayList<>();
        }
        onAddNoteListenerList.add(listener);
    }

    public void unRegisterOnAddNoteListener(OnAddNoteListener listener){
        if (onAddNoteListenerList == null){
            onAddNoteListenerList = new ArrayList<>();
            return;
        }
        onAddNoteListenerList.remove(listener);
    }

    public void displayOnAddNoteListener(){
        if (onAddNoteListenerList == null){
            onAddNoteListenerList = new ArrayList<>();
            return;
        }
        for (OnAddNoteListener listener : onAddNoteListenerList){
            listener.onAddNoteFinish();
        }
    }

    public void clean(){
        noteModel = new NoteModel();
        noteImagePaths.clear();
    }

}
