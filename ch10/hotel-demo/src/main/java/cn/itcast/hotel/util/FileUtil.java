package cn.itcast.hotel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : hotel-demo
 * @Package : cn.itcast.hotel.util
 * @ClassName : .java
 * @createTime : 2023/8/5 10:35
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description : 文件相关工具类
 */
public class FileUtil {
    public static String getFileContent(File file) throws IOException {
        FileReader fr;
        fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        do {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
        }
        while (true);
        br.close();
        return sb.toString();
    }
}
