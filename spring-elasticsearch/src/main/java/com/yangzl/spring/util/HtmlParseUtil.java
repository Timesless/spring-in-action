package com.yangzl.spring.util;

import com.yangzl.spring.domain.JDBook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author yangzl
 * @date 2020/10/3 17:14
 * @desc 获取京东搜索数据
 */
public class HtmlParseUtil {
    
    private static final String URL = "https://search.jd.com/Search?enc=utf-8&keyword=";
    
    public static List<JDBook> parseHtml(String keyword) {
        List<JDBook> list = Collections.emptyList();
        if (StringUtils.isEmpty(keyword)) {
            return list;
        }
        try {
            URL url = new URL(URL + keyword);
            Document doc = Jsoup.parse(url, 3000);
            Element goodsList = doc.getElementById("J_goodsList");
            Elements lis = goodsList.getElementsByTag("li");
            list = new ArrayList<>(lis.size());
            for (Element li : lis) {
                String name = li.getElementsByClass("p-name").eq(0).text();
                String price = li.getElementsByClass("p-price").eq(0).text();
                // 图片会lazy-load，所以不在src属性下
                String img = li.getElementsByTag("img").eq(0).attr("data-lazy-img");
                list.add(new JDBook(UUID.randomUUID().toString(), name, price, img));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("当前URL不存在");
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(parseHtml("Java"));
    }
}
