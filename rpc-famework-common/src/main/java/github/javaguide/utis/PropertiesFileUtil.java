package github.javaguide.utis;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

//获取资源方法
@Slf4j
public class PropertiesFileUtil {
    private PropertiesFileUtil() {
    }

    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String rpcConfigPath = "";
        if (url != null) {
            rpcConfigPath = url.getPath() + fileName;
        }
        Properties properties = null;
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(rpcConfigPath), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            log.error("occur exception when read properties file [{}]", fileName);
        }
        return properties;
    }
}
