package com.simplenote.constants;

/**
 * Created by melon on 2017/7/17.
 */

public class Constant {

    public static final String URL_MAIN = "http://192.168.1.225:12345";
//    public static final String URL_MAIN = "http://119.23.222.106/simplenote";

    public static class NUMBER{

        public static int SOFT_KEY_WORD_HEIGHT;

    }

    public static class PARAM{
        public static final String TOKEN = "token";
        public static final String PHONE = "phone";
        public static final String IP = "ip";
        public static final String REQ_TYPE = "req_type";

        public static final String NOTE_ID = "noteId";

        public static final String PWD = "pwd";
        public static final String AUTH_CODE = "auth_code";
        public static final String LOGIN_TYPE = "login_type";
        public static final String NICK_NAME = "nickName";

        public static final String START = "start";
        public static final String LENGTH = "length";

        public static final String PASTER_ID = "paster_id";
        public static final String USER_ID = "user_id";

    }

    public static class KEY{

        public static final String CODE = "code";
        public static final String DATA = "data";
        public static final String MSG = "msg";

        public static final String THEME_TITLE = "theme_title";
        public static final String THEME_CONTENT = "theme_content";

        public static final String NOTE_MODEL = "note_model";

        public static final String PAGE_TYPE = "note_model";

    }

    public static class VALUE{
        public static final String VALUE_PIC_CHOOSE_TYPE_OWN = "photo_own";
        public static final String VALUE_PIC_CHOOSE_TYPE_TAKE = "photo_take";

        public static final String AUTH_CODE_REQ_TYPE_REGISTER = "register";
        public static final String AUTH_CODE_REQ_TYPE_RESET_PWD= "reset_pwd";

        public static final int PIC_PATH_TYPE_RES = 3;
        public static final int PIC_PATH_TYPE_FILE = 4;
        public static final int PIC_PATH_TYPE_URL = 5;

        public static final String AVATAR_PAGE_TYPE_LOGO = "avatar_img";
        public static final String AVATAR_PAGE_TYPE_BACKDROP = "backdrop_img";

        public static final String LOGIN_TYPE_PWD = "login_pwd";
        public static final String LOGIN_TYPE_AUTO_CODE = "login_authcode";

        public static final String OSS_CONFIG_TYPE_UPLOAD = "oss_upload";
        public static final String OSS_CONFIG_TYPE_DOWNLOAD = "oss_download";

        public static final int TYPE_NOTE_DETAIL_ADD = 1;
        public static final int TYPE_NOTE_DETAIL_MODIFY = 2;

    }

    public static class REQUEST{

        public static final int ADD_NOTE = 1;
        public static final int SELECT_EMOTION = 2;

        public static final int VALUE_INTENT_TO_OWN = 3;
        public static final int VALUE_INTENT_TO_TAKE = 4;

        public static final int SELECT_IMAGE = 5;

        public static final int INTENT_SYSTEM_SETTING= 6;

        public static final int INTENT_SELECT_PIC = 7;

        public static final int INTENT_SELECT_AVATAR = 8;

        public static final int INTENT_NOTE_DETAIL_MODIFY = 9;

    }

    public static class BUNDEL{
        public static final String PIC_RES = "pic_res";
        public static final String PIC_PATH = "pic_path";
        public static final String PIC_PATH_TYPE = "pic_path_type";
        public static final String NOTE_MOEDEL = "note_model";
        public static final String PIC_CHOOSE_TYPE = "pic_choose_type";
        public static final String PIC_CHOOSE_DATA = "pic_choose_data";
        public static final String PIC_FILE_PATHS = "files";
        public static final String POSITION = "position";
        public static final String NOTE_ID = "note_id";
        public static final String AVATAR_TYPE = "avatar_type";



    }

    public static class RESULT_CODE{
        public static final int CODE_LOCAL_NOT_SERVER_HAD = 11001;//本地没有，服务器端有
        public static final int CODE_LOCAL_HAD_SERVER_HAD = 11002;//服务器端有，本地有
        public static final int CODE_LOCAL_HAD_SERVER_NOT = 11003;//服务器端没有，本地有
    }

}
