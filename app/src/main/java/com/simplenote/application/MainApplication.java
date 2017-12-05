package com.simplenote.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.simplenote.debug.DebugConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class MainApplication extends Application{

    public static String PACKAGE_NAME = "";

  @Override
  public void onCreate() {
    super.onCreate();
      PACKAGE_NAME = getPackageName();

    MyClient.getMyClient().init(this);

    ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
          .setDownsampleEnabled(true)
          .build();
    Fresco.initialize(this,config);

    initPush();

      initUmeng();

  }

  private void initPush(){
      final PushAgent mPushAgent = PushAgent.getInstance(this);
      //注册推送服务，每次调用register方法都会回调该接口
      mPushAgent.register(new IUmengRegisterCallback() {

          @Override
          public void onSuccess(String deviceToken) {
            //注册成功会返回device token
              String id = mPushAgent.getRegistrationId();
          }

          @Override
          public void onFailure(String s, String s1) {

          }
      });
      //为免过度打扰用户，SDK默认在“23:00”到“7:00”之间收到通知消息时不响铃，不振动，不闪灯。
      //这里设置禁用免打扰模式
      mPushAgent.setNoDisturbMode(0, 0, 0, 0);
      //默认情况下，同一台设备在1分钟内收到同一个应用的多条通知时，不会重复提醒，同时在通知栏里新的通知会替换掉旧的通知。
      //这里设置不会覆盖信息
      mPushAgent.setMuteDurationSeconds(0);
      //通知栏可以设置最多显示通知的条数，当有新通知到达时，会把旧的通知隐藏。
      //这里设置不合并消息
      mPushAgent.setDisplayNotificationNumber(0);

      //这里可以设置推送到达时手机的反馈
      mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
      mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
//      mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
  }

  private void initUmeng(){
      Config.DEBUG = DebugConfig.isDebug;
      UMShareAPI.get(this);
      PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
      PlatformConfig.setQQZone("1106429345", "rbkQeocuoGIwCJTe");
      PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
  }

}
