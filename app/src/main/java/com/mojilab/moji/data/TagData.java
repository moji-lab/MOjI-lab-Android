package com.mojilab.moji.data;

public class TagData {
    public String email;
    public String nickname;
    public int userIdx;
    public String photoUrl;
    public boolean isChecked;

    public TagData( String email, String nick_name, int id,String photoUri, boolean isChecked){
        this.email = email;
        this.nickname = nick_name;
        this.userIdx = id;
        this.photoUrl = photoUri;
        this.isChecked = isChecked;
    }
}
