package com.bjtu.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.*;
import java.util.*;

public class RedisDemoApplication {
    private static List<CounterSpec> counters;
    private static JSONObject actions;
    private static RedisUtil redisUtil;

    public static void main(String[] args) {
        try {
            redisUtil = new RedisUtil(JedisInstance.getInstance().getResource());
            System.out.println("Redis服务连接成功..");
        } catch (JedisConnectionException e) {
            System.out.println("Redis服务连接失败..");
            return;
        }

        Monitor("json/", new FileListener());
        System.out.println("文件监听已开启..");
        LoadJson();
        System.out.println("JSON文件已加载..");

        while (true) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("请输入指令：");
                String input = br.readLine();
                ActionSpec action = new ActionSpec();
                action.setType(input);
                action.setFeature_retrieve(JSON.parseArray(actions.getJSONObject(input)
                        .getJSONArray("feature_retrieve").toJSONString(), String.class));
                action.setSave_counter(JSON.parseArray(actions.getJSONObject(input)
                        .getJSONArray("save_counter").toJSONString(), String.class));
                Execute(action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载JSON文件并更新counter/action对象
     */
    public static void LoadJson() {
        String counterStr = ReadFile("json/counter.json");
        String actionStr = ReadFile("json/action.json");
        counters = JSON.parseArray(JSON.parseObject(counterStr, JSONObject.class)
                .getJSONArray("counters").toJSONString(), CounterSpec.class);
        actions = JSON.parseObject(actionStr, JSONObject.class).getJSONObject("actions");
        //System.out.println(counters.get(0).getCountername());
    }

    /**
     * 读取JSON文件
     * @param path 文件路径
     * @return JSON字符串
     */
    public static String ReadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            for (int line = 1; (tempString = reader.readLine()) != null; line++) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    /**
     * 写入JSON文件
     * @param filePath 文件路径
     * @param sets JSON字符串
     * @throws IOException 输入输出
     */
    public static void WriteFile(String filePath, String sets) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        PrintWriter out = new PrintWriter(fw);
        out.write(sets);
        out.println();
        fw.close();
        out.close();
    }

    /**
     * 启动文件监视
     * @param path 文件路径
     * @param listener 监听器
     */
    public static void Monitor(String path, FileListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000, observer);
        observer.addListener(listener);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行action，调用redis服务更新数据
     * @param act 由用户输入的指令生成的action
     */
    public static void Execute(ActionSpec act) {
        List<CounterSpec> retrieve = new ArrayList<CounterSpec>();
        List<CounterSpec> save = new ArrayList<CounterSpec>();
        for (CounterSpec ctr : counters) {
            if (act.getFeature_retrieve().contains(ctr.getCountername())) {
                retrieve.add(ctr);
            }
            if (act.getSave_counter().contains(ctr.getCountername())) {
                save.add(ctr);
            }
        }
        switch (act.getType()) {
            case "USER_LIKE_INCR":
                for (CounterSpec ctr : retrieve) {
                    System.out.println("key：" + ctr.getKeyfields());
                    System.out.println("操作前value：" + redisUtil.get(ctr.getKeyfields()));
                }
                for (CounterSpec ctr : save) {
                    redisUtil.incr(ctr.getKeyfields(), Long.parseLong(ctr.getValuefields()));
                    System.out.println("操作后value：" + redisUtil.get(ctr.getKeyfields()));
                }
                break;
            case "USER_LIKE_DECR":
                for (CounterSpec ctr : retrieve) {
                    System.out.println("key：" + ctr.getKeyfields());
                    System.out.println("操作前value：" + redisUtil.get(ctr.getKeyfields()));
                }
                for (CounterSpec ctr : save) {
                    redisUtil.decr(ctr.getKeyfields(), Long.parseLong(ctr.getValuefields()));
                    System.out.println("操作后value：" + redisUtil.get(ctr.getKeyfields()));
                }
                break;
            case "USER_LIKE_FREQ_INCR":
                for (CounterSpec ctr : retrieve) {
                    System.out.println("key：" + ctr.getKeyfields());
                    System.out.println("field：" + ctr.getField());
                    System.out.println("操作前value：" + redisUtil.hget(ctr.getKeyfields(), ctr.getField()));
                }
                for (CounterSpec ctr : save) {
                    redisUtil.hincr(ctr.getKeyfields(), ctr.getField(), Long.parseLong(ctr.getValuefields()));
                    System.out.println("操作后value：" + redisUtil.hget(ctr.getKeyfields(), ctr.getField()));
                }
                break;
            case "USER_LIKE_FREQ_DECR":
                for (CounterSpec ctr : retrieve) {
                    System.out.println("key：" + ctr.getKeyfields());
                    System.out.println("field：" + ctr.getField());
                    System.out.println("操作前value：" + redisUtil.hget(ctr.getKeyfields(), ctr.getField()));
                }
                for (CounterSpec ctr : save) {
                    redisUtil.hdecr(ctr.getKeyfields(), ctr.getField(), Long.parseLong(ctr.getValuefields()));
                    System.out.println("操作后value：" + redisUtil.hget(ctr.getKeyfields(), ctr.getField()));
                }
                break;
            default:
                System.out.println("指令类别无效！");
                break;
        }
        System.out.println();
    }

}
