package com.neutronconsolidate.interpreter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ConstantTable {
    HashMap<String, Constant> constants;

    public ConstantTable() {
        this.constants = new HashMap<>();
        this.initializeConstants();
    }

    public void initializeConstants() {
        this.add(new Constant<>("currentDate", (new SimpleDateFormat("yyyy/MM/dd")).format(new Date())));
        this.add(new Constant<>("currentTime", (new SimpleDateFormat("HH:mm:ss")).format(new Date())));
        this.add(new Constant<>("currentHour", Double.parseDouble((new SimpleDateFormat("HH")).format(new Date()))));
        this.add(new Constant<>("currentMinute", Double.parseDouble((new SimpleDateFormat("mm")).format(new Date()))));
        this.add(new Constant<>("currentSecond", Double.parseDouble((new SimpleDateFormat("ss")).format(new Date()))));
        this.add(new Constant<>("currentYear", Double.parseDouble((new SimpleDateFormat("yyyy")).format(new Date()))));
        this.add(new Constant<>("currentMonth", Double.parseDouble((new SimpleDateFormat("MM")).format(new Date()))));
        this.add(new Constant<>("currentDay", Double.parseDouble((new SimpleDateFormat("dd")).format(new Date()))));
    }

    public void add(Constant constant) {
        this.constants.put(constant.name, constant);
    }

    public Constant get(String key) {
        return this.constants.get(key);
    }
}
