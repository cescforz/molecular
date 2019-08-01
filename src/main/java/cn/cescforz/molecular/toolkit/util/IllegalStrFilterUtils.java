package cn.cescforz.molecular.toolkit.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>©2018 Cesc. All Rights Reserved.</p>
 * <p>Description: 特殊字符检测工具（防止传入非法字符和sql注入攻击）</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2018-12-25 14:47
 */
@Slf4j
public final class IllegalStrFilterUtils {

    private IllegalStrFilterUtils(){throw new AssertionError();}

    private static final String REGX = "!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§ ";

    /**
     * <p>Description: 对常见的sql注入攻击进行拦截</p>
     * @param sInput 请求参数
     * @return java.lang.Boolean
     * true  表示参数不存在SQL注入风险
     * false 表示参数存在SQL注入风险
     */
    public static Boolean sqlStrFilter(String sInput) {
        if (StringUtils.isBlank(sInput)) {
            return true;
        }
        sInput = sInput.toUpperCase();
        if (sInput.contains("DELETE") || sInput.contains("ASCII") || sInput.contains("UPDATE") || sInput.contains("SELECT")
                || sInput.contains("'") || sInput.contains("SUBSTR(") || sInput.contains("COUNT(") || sInput.contains(" OR ")
                || sInput.contains(" AND ") || sInput.contains("DROP") || sInput.contains("EXECUTE") || sInput.contains("EXEC")
                || sInput.contains("TRUNCATE") || sInput.contains("INTO") || sInput.contains("DECLARE") || sInput.contains("MASTER")) {
            log.warn("该参数存在SQL注入风险：sInput={}",sInput);
            return false;
        }
        log.info("通过sql检测");
        return true;
    }
    
    /**
     * <p>Description: 对非法字符进行检测</p>
     * @param sInput 请求参数
     * @return java.lang.Boolean
     * true  表示参数包含非法字符
     * false 表示参数不包含非法字符
     */
    public static Boolean isIllegalStr(String sInput) {
        if (StringUtils.isBlank(sInput)) {
            return false;
        }
        sInput = sInput.trim();
        Pattern compile = Pattern.compile(REGX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(sInput);
        boolean flag = matcher.find();
        if(flag){
            log.warn("参数包含非法字符:sInput={}",sInput);
        }else{
            log.info("通过字符串检测");
        }
        return flag;
    }

}
