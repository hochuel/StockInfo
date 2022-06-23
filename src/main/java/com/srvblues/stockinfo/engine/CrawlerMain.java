package com.srvblues.stockinfo.engine;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.srv.utils.excel.Excel;
import org.srv.vo.HMap;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

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


    public void getStockInfo(String threadName, BlockingQueue blockingQueue, List resultListData) throws InterruptedException {

        HMap stockVo = (HMap)blockingQueue.take();

        //log.info(threadName +":::::::"+stockVo.getString("단축코드") +":"+ stockVo.getString("한글 종목명") + ":" + stockVo.getString("시장구분") );
        
        
        String URL = "";
        try {
            URL = "http://comp.fnguide.com/SVO2/ASP/SVD_Main.asp?pGB=1&gicode=A"+stockVo.getString("단축코드")+"&cID=&MenuYn=Y&ReportGB=&NewMenuID=11&stkGb=701";
            // 2. Connection 생성
            Connection conn = Jsoup.connect(URL);

            // 3. HTML 파싱.
            Document html = conn.get(); // conn.post();

            Elements elements = html.getElementsByClass("us_table_ty1 table-hb thbg_g h_fix zigbg_no");
            Elements 시가총액 = elements.select("tr:eq(4) td:eq(1)");
            String str시가총액 = 시가총액.html().replaceAll(",", "");
            
            Elements 주식수 = elements.select("tr:eq(6) td:eq(1)");
            String str주식수 = 주식수.html().replaceAll(",", "");
            str주식수 = (str주식수.indexOf("/") > -1)?str주식수.substring(0, str주식수.indexOf("/")):"0";
            str주식수 = str주식수.equals("")?"0":str주식수;



            Elements 재무정보 = html.select("div#highlight_D_A");
            //System.out.println("재무정보 :: " + 재무정보.html());

            Elements 지배주주지분 = 재무정보.select("tbody tr:eq(9) td:eq(3)");
            String str지배주주지분 = 지배주주지분.html().replaceAll(",", "");

            str지배주주지분 = str지배주주지분 == null || str지배주주지분.equals("")?"0":str지배주주지분;
            str지배주주지분 = str지배주주지분.indexOf("<span") > -1?"0":str지배주주지분;
            str지배주주지분 = str지배주주지분.indexOf("&nbsp;") > -1?"0":str지배주주지분;

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


            double 기대수익율 = 10.53;
            int 기준가격 = 100000000;
            double 초과수익 = Double.parseDouble(str지배주주지분) * ((roe - 기대수익율)/100);
            double 기업가치_현재 = Double.parseDouble(str지배주주지분) + 초과수익 * (1/(기대수익율/100));


            double 기업가치_10감소 = Double.parseDouble(str지배주주지분) + 초과수익 * (0.9 / (1 + (기대수익율/100) - 0.9));
            double 기업가치_20감소 = Double.parseDouble(str지배주주지분) + 초과수익 * (0.8 / (1 + (기대수익율/100) - 0.8));



            int 적정주가 = (int) (기업가치_현재 * 기준가격 / Long.parseLong(str주식수));
            int 적정주가_10 = (int) (기업가치_10감소 * 기준가격 / Long.parseLong(str주식수));
            int 적정주가_20 = (int) (기업가치_20감소 * 기준가격 / Long.parseLong(str주식수));




            URL = "https://finance.naver.com/item/coinfo.naver?code="+stockVo.getString("단축코드");
            // 2. Connection 생성
            conn = Jsoup.connect(URL);
            // 3. HTML 파싱.
            html = conn.get(); // conn.post();
            Elements 현재주가 = html.select("div dl dd:eq(4)");
            String str현재주가 = 현재주가.html().substring(4, 현재주가.html().indexOf("전일대비") - 1).replaceAll(",", "");


            double 가격대비 =  Math.round((((double)Math.round(적정주가) - (double)Integer.parseInt(str현재주가)) / Math.round(적정주가)) * 100) ;



            HMap result = new HMap();
            result.put("code", stockVo.getString("단축코드"));
            result.put("name", stockVo.getString("한글 종목명"));
            result.put("시장구분", stockVo.getString("시장구분"));
            result.put("시가총액",String.valueOf(str시가총액));
            result.put("주식수",String.valueOf(str주식수));
            result.put("지배주주지분",String.valueOf(str지배주주지분));
            result.put("ROE",String.valueOf(roe));
            result.put("기업가치", String.valueOf(기업가치_현재));
            result.put("적정주가", String.valueOf(Math.round(적정주가)));
            result.put("기업가치_10감소", String.valueOf(Math.round(적정주가_10)));
            result.put("기업가치_20감소", String.valueOf(Math.round(적정주가_20)));
            result.put("현재주가", String.valueOf(str현재주가));
            result.put("가격대비",String.valueOf(가격대비));

            resultListData.add(result);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<HMap> ExcelRead() throws Exception {
        Excel excel = Excel.makeSec(new File("C:\\stockInfo\\data_2245_20220623.xlsx"), "data_2245_20220623.xlsx");
        List<List<HMap>> dataList = excel.toHMapList();
        List<HMap> listSheet = dataList.get(0);
        log.info("listSheet size:" + listSheet.size());
        //log.info("listSheet:" + listSheet);
        //단축코드=098120, 한글 종목명=(주)마이크로컨텍솔루션, 시장구분=KOSDAQ

        return listSheet;
    }


    private float getConvert(String data){

        data = data.replaceAll(",", "");

        if(data == null || data.equals("") || data.equals("N/A(IFRS)") || data.equals("완전잠식")){
            return 0;
        }

        return Float.parseFloat(data);
    }

    public static void main(String[] args) throws Exception {
        CrawlerMain crawlerMain = new CrawlerMain();
        //crawlerMain.getFnguidData("266870");
        List list = crawlerMain.ExcelRead();
        
        
    }
}