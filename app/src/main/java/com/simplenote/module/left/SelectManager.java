package com.simplenote.module.left;

import com.simplenote.application.MyClient;
import com.simplenote.database.model.Note;
import com.simplenote.model.ImageModel;
import com.simplenote.module.add.AddNoteManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melon on 2017/7/20.
 */

public class SelectManager {

    private List<String> selectListEmotion = new ArrayList<>();
    private List<String> selectListWeather = new ArrayList<>();
    private List<String> selectListTheme = new ArrayList<>();

    public SelectManager(){

    }

    public void addSelect(int type,String info){
        switch (type){
            case AddNoteManager.TYPE_EMOTION:{
                selectListEmotion.add(info);
                break;
            }
            case AddNoteManager.TYPE_WEATHER:{
                selectListWeather.add(info);
                break;
            }
            case AddNoteManager.TYPE_THEME:{
                selectListTheme.add(info);
                break;
            }
        }
    }

    public void removeSelect(int type,String info){
        switch (type){
            case AddNoteManager.TYPE_EMOTION:{
                selectListEmotion.remove(info);
                break;
            }
            case AddNoteManager.TYPE_WEATHER:{
                selectListWeather.remove(info);
                break;
            }
            case AddNoteManager.TYPE_THEME:{
                selectListTheme.remove(info);
                break;
            }
        }
    }

    public List<Note> filterNoteModel(List<Note> modelList){
        List<Note> noteList = new ArrayList<>();
        for (Note model : modelList){
            if (selectListEmotion.contains(model.getEmotion())){
                noteList.add(model);
                continue;
            }

            if (selectListWeather.contains(model.getWeather())){
                noteList.add(model);
                continue;
            }

            if (selectListTheme.contains(model.getTheme())){
                noteList.add(model);
                continue;
            }
        }
        return noteList;
    }

    private List<Integer> getSelectIndexList(int type){
        List<Integer> indexList = new ArrayList<>();
        AddNoteManager manager = MyClient.getMyClient().getAddNoteManager();
        switch (type){
            case AddNoteManager.TYPE_EMOTION:{
                for (String str : selectListEmotion){
                    indexList.add(manager.getEmotionIndex(str));
                }
                break;
            }
            case AddNoteManager.TYPE_WEATHER:{
                for (String str : selectListWeather){
                    indexList.add(manager.getWeatherIndex(str));
                }
                break;
            }
            case AddNoteManager.TYPE_THEME:{
                for (String str : selectListTheme){
                    indexList.add(manager.getThemeIndex(str));
                }
                break;
            }
        }
        return indexList;
    }

    public List<String> getLeftImageResList(int type){
        List<ImageModel> modelList = MyClient.getMyClient().getAddNoteManager().getImageModelList(type);
        List<String> imageResList = new ArrayList<>();

        for (ImageModel model : modelList){
            imageResList.add(model.getSelectBigStr());
        }

        return imageResList;
    }

}
