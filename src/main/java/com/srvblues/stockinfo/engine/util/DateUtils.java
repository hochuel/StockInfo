package com.srvblues.stockinfo.engine.util;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    public String getPubDate(String pubDate) throws ParseException {
        SimpleDateFormat parseDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        SimpleDateFormat formatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date parseDate = parseDateFormat.parse(pubDate);
        return formatDateFormat.format(parseDate);
    }


    public String getPubDate(Date pubDate) throws ParseException {
        SimpleDateFormat formatDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatDateFormat.format(pubDate);
    }


    public static void main(String[] args) throws ParseException, MalformedURLException, UnsupportedEncodingException {

        //String pubDate = "Tue, 12 Jul 2022 01:16:35 GMT";%98
        DateUtils dateUtils = new DateUtils();
        //System.out.println(dateUtils.getPubDate(pubDate));


        String encoderStr = URLEncoder.encode("국도화학", "UTF-8");

        try {
            URL rssUrl = new URL("https://news.google.com/rss/search?hl=ko&gl=KR&ie=UTF-8&q="+encoderStr+"&ceid=KR:ko");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed syndFeed = input.build(new XmlReader(rssUrl));














            List<SyndEntry> entries = syndFeed.getEntries();
            for (SyndEntry entry : entries)

            {
                System.out.println("제목 :: " + entry.getTitle());
                System.out.println("Link :: " + entry.getLink());
                System.out.println("시간 :: " + dateUtils.getPubDate(entry.getPublishedDate()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}
