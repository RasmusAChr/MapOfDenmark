package com.telos.mapofdenmark;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.List;

public class Relation implements Serializable{
    private String type;
    private List<Member>memberRefs;



    public Relation(String type,List<Member> memberRefs){
        ColorScheme cs = new ColorScheme();
        this.type=type;
        this.memberRefs = memberRefs;
        for(Member m : memberRefs){
        }
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }
    public List<Member>getMemberRefs(){
        return memberRefs;
    }
    public void setMemberRefs(List<Member>memberRefs){
        this.memberRefs=memberRefs;
    }

    public void Draw(GraphicsContext gc,double zoom,boolean darkMode){
        gc.setStroke(Color.RED);
        for(Member m : memberRefs){
            if(m.getType().equals("outer")){
                m.way.draw(gc,zoom,darkMode);
            }
        }
    }
}
