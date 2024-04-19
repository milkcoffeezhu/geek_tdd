package com.geek.tdd.args;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxs2
 * @date 2024/4/18 22:01:07
 */
public class ArgsTest {
    /**
     * -l -p 8080 -d /usr/logs
     * 数据切分 [-l] [-p, 8080] [-d, /usr/logs]
     * 单个参数解析
     *TODO    -Bool -l
     *TODO    -Int -p 8080
     *TODO    -String -d /usr/logs
     *TODO 多个参数 -l -p 8080 -d /usr/logs
     * 边界条件
     *TODO    -Bool -l 后面没有参数
     *TODO    -Int -p 只有一个参数并且只能是整形并且大于0
     *TODO    -String -d 只能是一个String并且只能是完整的路径
     *默认值
     *TODO    -Bool -l 默认值是false
     *TODO    -Int -p 默认值是0
     *TODO    -String -d 默认值是空字符串
     *
     *
     */

    /**
     *  -Bool -l
     */
    @Test
    public void should_set_boolean_option_to_true_if_flag_present(){
        BooleanOptions options = Args.parse(BooleanOptions.class, "-l");
        assertTrue(options.logging);
    }
    @Test
    public void should_set_boolean_option_to_false_if_flag_not_present(){
        BooleanOptions options = Args.parse(BooleanOptions.class);
        assertFalse(options.logging);
    }
    static record BooleanOptions(@Option("l") boolean logging) {}

    /**
     *  -Int -p 8080
     */
    @Test
    public void should_parse_int_as_option_value(){
        IntOptions options = Args.parse(IntOptions.class, "-p", "8080");
        assertEquals(8080,options.port());
    }

    @Test
    public void should_parse_into_as_zero_if_not_present(){
        IntOptions options = Args.parse(IntOptions.class);
        assertEquals(0,options.port());
    }

    static record IntOptions(@Option("p") int port) {}

    /**
     *  -String -d /usr/logs
     */
    @Test
    public void should_parse_string_as_option_value(){
        StringOptions options = Args.parse(StringOptions.class, "-d", "/usr/logs");
        assertEquals("/usr/logs",options.directory());
    }

    @Test
    public void should_parse_string_as_empty_string_if_not_present(){
        StringOptions options = Args.parse(StringOptions.class);
        assertEquals("",options.directory());
    }

    static record StringOptions(@Option("d") String directory) {}

    /**
     *  -Bool -l -p 8080 -d /usr/logs
     */
    @Test
    public void should_parse_multi_options(){
        // 优化使用
        MultiOptions options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging);
        assertEquals(8080,options.port());
        assertEquals("/usr/logs",options.directory());
    }

    static record MultiOptions(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {}



    @Test
    @Disabled
    public void should_example_2(){
        ListOptions options = Args.parse(ListOptions.class, "-g", "this","is","a","list", "-g", "1","2","3");
        assertEquals(options.groups(), new String[]{"this","is","a","list"});
        assertEquals(options.decimals(), new int[]{1,2,3});
    }

    static record ListOptions(@Option("g") String[] groups,@Option("g") int[] decimals) {}
}
