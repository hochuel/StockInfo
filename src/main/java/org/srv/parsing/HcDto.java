package org.srv.parsing;


@ClassData(length = 800)
public class HcDto {
    @basic(start=0, length=4)
    private String 전문길이;

    @basic(start=4, length=4)
    private String Text_ID;

    @basic(start=8, length=2)
    private String 전문구분코드;

    @basic(start=10, length=4)
    private String 회원사_코드;

    @basic(start=14, length=8)
    private String 거래일자;

    @basic(start=22, length=6)
    private String 거래시간;

    @basic(start=26, length=60)
    private String 회원사영역;

    @basic(start=88, length=2)
    private String 업무_구분;

    @basic(start=90, length=20)
    private String 고객_번호;

    @basic(start=110, length=88)
    private String CI;

    @basic(start=198, length=2)
    private String 상품_구분_코드;

    @basic(start=200, length=100)
    private String 고객성명;

    @basic(start=300, length=50)
    private String 핸드폰_번호;

    @basic(start=350, length=2)
    private String 가입_해지_경로;

    @basic(start=352, length=8)
    private String 가입_해지_일자;

    @basic(start=360, length=6)
    private String 가입_해지_시간;

    @basic(start=366, length=4)
    private String 해지상세코드;

    @basic(start=370, length=200)
    private String 고객_email;

    @basic(start=570, length=2)
    private String 제신고_구분_코드;

    @basic(start=572, length=200)
    private String 제신고_내용;

    @basic(start=772, length=2)
    private String 결과코드;

    @basic(start=774, length=26)
    private String 예비_필드;

    /**
     * @return the 전문길이
     */
    public String get전문길이() {
        return 전문길이;
    }

    /**
     * @param 전문길이 the 전문길이 to set
     */
    public void set전문길이(String 전문길이) {
        this.전문길이 = 전문길이;
    }

    /**
     * @return the text_ID
     */
    public String getText_ID() {
        return Text_ID;
    }

    /**
     * @param text_ID the text_ID to set
     */
    public void setText_ID(String text_ID) {
        Text_ID = text_ID;
    }

    /**
     * @return the 전문구분코드
     */
    public String get전문구분코드() {
        return 전문구분코드;
    }

    /**
     * @param 전문구분코드 the 전문구분코드 to set
     */
    public void set전문구분코드(String 전문구분코드) {
        this.전문구분코드 = 전문구분코드;
    }

    /**
     * @return the 회원사_코드
     */
    public String get회원사_코드() {
        return 회원사_코드;
    }

    /**
     * @param 회원사_코드 the 회원사_코드 to set
     */
    public void set회원사_코드(String 회원사_코드) {
        this.회원사_코드 = 회원사_코드;
    }

    /**
     * @return the 거래일자
     */
    public String get거래일자() {
        return 거래일자;
    }

    /**
     * @param 거래일자 the 거래일자 to set
     */
    public void set거래일자(String 거래일자) {
        this.거래일자 = 거래일자;
    }

    /**
     * @return the 거래시간
     */
    public String get거래시간() {
        return 거래시간;
    }

    /**
     * @param 거래시간 the 거래시간 to set
     */
    public void set거래시간(String 거래시간) {
        this.거래시간 = 거래시간;
    }

    /**
     * @return the 회원사영역
     */
    public String get회원사영역() {
        return 회원사영역;
    }

    /**
     * @param 회원사영역 the 회원사영역 to set
     */
    public void set회원사영역(String 회원사영역) {
        this.회원사영역 = 회원사영역;
    }

    /**
     * @return the 업무_구분
     */
    public String get업무_구분() {
        return 업무_구분;
    }

    /**
     * @param 업무_구분 the 업무_구분 to set
     */
    public void set업무_구분(String 업무_구분) {
        this.업무_구분 = 업무_구분;
    }

    /**
     * @return the 고객_번호
     */
    public String get고객_번호() {
        return 고객_번호;
    }

    /**
     * @param 고객_번호 the 고객_번호 to set
     */
    public void set고객_번호(String 고객_번호) {
        this.고객_번호 = 고객_번호;
    }

    /**
     * @return the cI
     */
    public String getCI() {
        return CI;
    }

    /**
     * @param cI the cI to set
     */
    public void setCI(String cI) {
        CI = cI;
    }

    /**
     * @return the 상품_구분_코드
     */
    public String get상품_구분_코드() {
        return 상품_구분_코드;
    }

    /**
     * @param 상품_구분_코드 the 상품_구분_코드 to set
     */
    public void set상품_구분_코드(String 상품_구분_코드) {
        this.상품_구분_코드 = 상품_구분_코드;
    }

    /**
     * @return the 고객성명
     */
    public String get고객성명() {
        return 고객성명;
    }

    /**
     * @param 고객성명 the 고객성명 to set
     */
    public void set고객성명(String 고객성명) {
        this.고객성명 = 고객성명;
    }

    /**
     * @return the 핸드폰_번호
     */
    public String get핸드폰_번호() {
        return 핸드폰_번호;
    }

    /**
     * @param 핸드폰_번호 the 핸드폰_번호 to set
     */
    public void set핸드폰_번호(String 핸드폰_번호) {
        this.핸드폰_번호 = 핸드폰_번호;
    }

    /**
     * @return the 가입_해지_경로
     */
    public String get가입_해지_경로() {
        return 가입_해지_경로;
    }

    /**
     * @param 가입_해지_경로 the 가입_해지_경로 to set
     */
    public void set가입_해지_경로(String 가입_해지_경로) {
        this.가입_해지_경로 = 가입_해지_경로;
    }

    /**
     * @return the 가입_해지_일자
     */
    public String get가입_해지_일자() {
        return 가입_해지_일자;
    }

    /**
     * @param 가입_해지_일자 the 가입_해지_일자 to set
     */
    public void set가입_해지_일자(String 가입_해지_일자) {
        this.가입_해지_일자 = 가입_해지_일자;
    }

    /**
     * @return the 가입_해지_시간
     */
    public String get가입_해지_시간() {
        return 가입_해지_시간;
    }

    /**
     * @param 가입_해지_시간 the 가입_해지_시간 to set
     */
    public void set가입_해지_시간(String 가입_해지_시간) {
        this.가입_해지_시간 = 가입_해지_시간;
    }

    /**
     * @return the 해지상세코드
     */
    public String get해지상세코드() {
        return 해지상세코드;
    }

    /**
     * @param 해지상세코드 the 해지상세코드 to set
     */
    public void set해지상세코드(String 해지상세코드) {
        this.해지상세코드 = 해지상세코드;
    }

    /**
     * @return the 고객_email
     */
    public String get고객_email() {
        return 고객_email;
    }

    /**
     * @param 고객_email the 고객_email to set
     */
    public void set고객_email(String 고객_email) {
        this.고객_email = 고객_email;
    }

    /**
     * @return the 제신고_구분_코드
     */
    public String get제신고_구분_코드() {
        return 제신고_구분_코드;
    }

    /**
     * @param 제신고_구분_코드 the 제신고_구분_코드 to set
     */
    public void set제신고_구분_코드(String 제신고_구분_코드) {
        this.제신고_구분_코드 = 제신고_구분_코드;
    }

    /**
     * @return the 제신고_내용
     */
    public String get제신고_내용() {
        return 제신고_내용;
    }

    /**
     * @param 제신고_내용 the 제신고_내용 to set
     */
    public void set제신고_내용(String 제신고_내용) {
        this.제신고_내용 = 제신고_내용;
    }

    /**
     * @return the 결과코드
     */
    public String get결과코드() {
        return 결과코드;
    }

    /**
     * @param 결과코드 the 결과코드 to set
     */
    public void set결과코드(String 결과코드) {
        this.결과코드 = 결과코드;
    }

    /**
     * @return the 예비_필드
     */
    public String get예비_필드() {
        return 예비_필드;
    }

    /**
     * @param 예비_필드 the 예비_필드 to set
     */
    public void set예비_필드(String 예비_필드) {
        this.예비_필드 = 예비_필드;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "HcDto [전문길이=" + 전문길이 + ", Text_ID=" + Text_ID + ", 전문구분코드=" + 전문구분코드 + ", 회원사_코드=" + 회원사_코드 + ", 거래일자="
                + 거래일자 + ", 거래시간=" + 거래시간 + ", 회원사영역=" + 회원사영역 + ", 업무_구분=" + 업무_구분 + ", 고객_번호=" + 고객_번호 + ", CI=" + CI
                + ", 상품_구분_코드=" + 상품_구분_코드 + ", 고객성명=" + 고객성명 + ", 핸드폰_번호=" + 핸드폰_번호 + ", 가입_해지_경로=" + 가입_해지_경로
                + ", 가입_해지_일자=" + 가입_해지_일자 + ", 가입_해지_시간=" + 가입_해지_시간 + ", 해지상세코드=" + 해지상세코드 + ", 고객_email=" + 고객_email
                + ", 제신고_구분_코드=" + 제신고_구분_코드 + ", 제신고_내용=" + 제신고_내용 + ", 결과코드=" + 결과코드 + ", 예비_필드=" + 예비_필드 + "]";
    }
    
}
