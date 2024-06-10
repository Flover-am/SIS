package com.seciii.prism030.core.enums;

/**
 * 新闻修改类型
 *
 * @author wangmingsong
 * @date 2024.04.30
 */
public enum UpdateType {
    ADD("add"),
    MODIFY("modify"),
    DELETE("delete");
    UpdateType(String text){
        this.text=text;
    }
    private final String text;

    public String toString(){
        return this.text;
    }
    public UpdateType of(String text){
        for(UpdateType updateType : UpdateType.values()){
            if(updateType.text.equals(text)){
                return updateType;
            }
        }
        return null;
    }
    public UpdateType of(int index){
        for(UpdateType updateType : UpdateType.values()){
            if(updateType.ordinal()==index){
                return updateType;
            }
        }
        return null;
    }
}
