package com.mojilab.moji.data;

public class TagData {
    public int id;
    public String nick_name;
    public String email;
    public Boolean isChecked;

    public TagData(int id, String nick_name, String email, Boolean isChecked){
        this.id = id;
        this.nick_name = nick_name;
        this.email = email;
        this.isChecked = isChecked;
    }
}
