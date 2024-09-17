package com.schdri;

import javafx.application.Application;

/**
 * @Description: 非模块化程序，得采用这种方式去启动 主程序。 可以正常打出exe启动。
 * @since: 2021/10/23 10:38
 */
public class Launch {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}


