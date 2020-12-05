package com.bjtu.redis;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

final class FileListener implements FileAlterationListener {

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
        //System.out.println(fileAlterationObserver.getDirectory().getName()+" monitor start scan files..");
    }

    @Override
    public void onDirectoryCreate(File file) {
        System.out.println(file.getName()+"文件夹已创建..");
    }

    @Override
    public void onDirectoryChange(File file) {
        System.out.println(file.getName()+"文件夹已更改..");
    }

    @Override
    public void onDirectoryDelete(File file) {
        System.out.println(file.getName()+"文件夹已删除..");
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println(file.getName()+"文件已创建..");
    }

    @Override
    public void onFileChange(File file) {
        System.out.println(file.getName()+"文件已更改..");
        RedisDemoApplication.LoadJson();
        System.out.println("JSON文件已重载..");
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println(file.getName()+"文件已删除..");
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
        //System.out.println(fileAlterationObserver.getDirectory().getName()+" monitor stop scanning..");
    }
}
