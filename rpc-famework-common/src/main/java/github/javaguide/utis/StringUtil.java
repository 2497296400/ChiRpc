package github.javaguide.utis;

//判断字符串是否为空
public class StringUtil {
    public static boolean isBlank(String cur) {
        if (cur == null || cur.length() == 0) {
            return true;
        }
        for (int i = 0; i < cur.length(); i++) {
            if(!Character.isWhitespace(cur.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
