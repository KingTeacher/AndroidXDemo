package com.example.myandroidxdemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addition_is() {
        List<Basedata> lista = new ArrayList<>();
        lista.add(new Basedata("a1",false,new ArrayList<Basedata>()));
        lista.add(new Basedata("a2",true,new ArrayList<Basedata>()));
        lista.add(new Basedata("a3",false,new ArrayList<Basedata>()));

        removeDeleteItem(lista);

    }

    class Basedata {
        public String aaa;
        public boolean bbb;
        public List<Basedata> sublist;


        public Basedata(String aaa, boolean bbb, List<Basedata> sublist) {
            this.aaa = aaa;
            this.bbb = bbb;
            this.sublist = sublist;
        }

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        public boolean isBbb() {
            return bbb;
        }

        public void setBbb(boolean bbb) {
            this.bbb = bbb;
        }

        public List<Basedata> getSublist() {
            return sublist;
        }

        public void setSublist(List<Basedata> sublist) {
            this.sublist = sublist;
        }
    }

    /**
     * 将delete为true项数据删除
     */
    private static void removeDeleteItem(List<Basedata> list){
        if(list == null || list.size() <= 0){
            return;
        }
        Iterator<Basedata> iterator = list.iterator();
        while (iterator.hasNext()) {
            Basedata item = iterator.next();
            if (item != null) {
                if(item.isBbb()){
                    iterator.remove();
                }
                if(item.getSublist() != null && item.getSublist().size() > 0){
                    removeDeleteItem(item.getSublist());
                }
            }
        }
    }

}