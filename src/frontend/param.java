package frontend;

import java.util.ArrayList;

public class param {
    private int type;//0 i32 1 i32*
    public ArrayList<Integer> arrSize = new ArrayList<>();//存储数组每个维度的大小
    private String na = "";//参数姓名

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }
}
