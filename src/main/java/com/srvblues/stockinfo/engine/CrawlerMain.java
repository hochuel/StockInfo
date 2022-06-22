package com.srvblues.stockinfo.engine;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

@Slf4j
public class CrawlerMain {


    public void getFnguidData(String code){

        String URL = "";
        try {


            URL = "http://comp.fnguide.com/SVO2/ASP/SVD_Main.asp?pGB=1&gicode=A"+code+"&cID=&MenuYn=Y&ReportGB=&NewMenuID=11&stkGb=701";
            // 2. Connection 생성
            Connection conn = Jsoup.connect(URL);

            // 3. HTML 파싱.
            Document html = conn.get(); // conn.post();

            Elements elements = html.getElementsByClass("us_table_ty1 table-hb thbg_g h_fix zigbg_no");
            Elements 시가총액 = elements.select("tr:eq(4) td:eq(1)");
            String str시가총액 = 시가총액.html().replaceAll(",", "");
            System.out.println("시가총액 :: " + str시가총액);

            Elements 주식수 = elements.select("tr:eq(6) td:eq(1)");
            String str주식수 = 주식수.html().replaceAll(",", "");
            str주식수 = str주식수.substring(0, str주식수.indexOf("/"));
            System.out.println("주식수 ::" + str주식수);


            Elements 재무정보 = html.select("div#highlight_D_A");
            //System.out.println("재무정보 :: " + 재무정보.html());

            Elements 지배주주지분 = 재무정보.select("tbody tr:eq(9) td:eq(3)");
            String str지배주주지분 = 지배주주지분.html().replaceAll(",", "");
            System.out.println("지배주주지분 :: " + str지배주주지분);


            Elements ROE = 재무정보.select("tbody tr:eq(17) td:gt(0):lt(4)");
            int sum = 0;
            float a = 0;
            for(int i = 0; i < ROE.size(); i++){
                Element row = ROE.get(i);
                sum += (i + 1);
                a += getConvert(row.text()) * (i + 1);
            }
            float roe = a/sum;
            roe = (float) (Math.round(roe * 100)/100.0);
            System.out.println("ROE :::" + roe);

            double 기대수익율 = 10.53;
            int 기준가격 = 100000000;
            double 초과수익 = Double.parseDouble(str지배주주지분) * ((roe - 기대수익율)/100);
            double 기업가치_현재 = Double.parseDouble(str지배주주지분) + 초과수익 * (1/(기대수익율/100));


            double 기업가치_10감소 = Double.parseDouble(str지배주주지분) + 초과수익 * (0.9 / (1 + (기대수익율/100) - 0.9));
            double 기업가치_20감소 = Double.parseDouble(str지배주주지분) + 초과수익 * (0.8 / (1 + (기대수익율/100) - 0.8));

            System.out.println("기업가치 ::" + 기업가치_현재);

            int 적정주가 = (int) (기업가치_현재 * 기준가격 / Long.parseLong(str주식수));
            int 적정주가_10 = (int) (기업가치_10감소 * 기준가격 / Long.parseLong(str주식수));
            int 적정주가_20 = (int) (기업가치_20감소 * 기준가격 / Long.parseLong(str주식수));

            System.out.println("적정주가 ::" + Math.round(적정주가));
            System.out.println("기업가치_10감소 ::" + Math.round(적정주가_10));
            System.out.println("기업가치_20감소 ::" + Math.round(적정주가_20));


            URL = "https://finance.naver.com/item/coinfo.naver?code="+code;
            // 2. Connection 생성
            conn = Jsoup.connect(URL);
            // 3. HTML 파싱.
            html = conn.get(); // conn.post();
            Elements 현재주가 = html.select("div dl dd:eq(4)");
            String str현재주가 = 현재주가.html().substring(4, 현재주가.html().indexOf("전일대비") - 1).replaceAll(",", "");
            System.out.println("현재주가 ::" + str현재주가);

            double 가격대비 =  Math.round((((double)Math.round(적정주가) - (double)Integer.parseInt(str현재주가)) / Math.round(적정주가)) * 100) ;
            System.out.println("가격대비 ::" + 가격대비);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private float getConvert(String data){
        if(data.equals("N/A(IFRS)")){
            return 0;
        }

        return Float.parseFloat(data);
    }

    public static void main(String[] args){
        CrawlerMain crawlerMain = new CrawlerMain();
        crawlerMain.getFnguidData("266870");
    }

}