package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tour.AreaVo;
import tour.SigunguVo;
import tour.TourVo;
import vo.ImageVo;
import vo.OverviewVo;

public class GetData {
	
	private String serVicekey = "06%2BQ4PwUZvpUSyIXm0NpMqx62GwlHanrSmW2D7dHHa6XqYvJLKpgE4tSAIzlXnGFV0g%2BKR2fv5g45hAJNOe1JA%3D%3D";
	private String endPoint ="http://api.visitkorea.or.kr/openapi/service";
	List<AreaVo> arealist = new ArrayList<AreaVo>(); // 지역 리스트
	List<SigunguVo> sigungulist = new ArrayList<SigunguVo>();
	List<TourVo> tourlist = new ArrayList<TourVo>();
	List<ImageVo> imagelist = new ArrayList<ImageVo>();
	List<OverviewVo> overviewlist = new ArrayList<OverviewVo>();
	int imaged = 0;
	int overviewd = 0;
	DB db = new DB();
	
	// 지역 Api입력
	public List<AreaVo> getArea() {
		String url = endPoint + "/rest/KorService/areaCode?serviceKey=" + serVicekey;
		String numOfRows = "&numOfRows=" + "17";
		String pageNo = "&pageNo=" + "1";
		String MobileOS = "&MobileOS=" + "ETC";
		String MobileApp = "&MobileApp=" + "Noppl";
		url += numOfRows + pageNo + MobileOS + MobileApp;
		// System.out.println(url); 
		
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(url);
			
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("item");
			
			if(nodeList.getLength() == 0) {
				System.out.println("검색 실패");
				return null;
			}
			
			for(int i=0; i<nodeList.getLength(); i ++) {
				Node nodeItem = nodeList.item(i);
				
				try {
					AreaVo areaVo = new AreaVo();
					areaVo.setArea_name(getTagValue("name",(Element)nodeItem));
					areaVo.setArea_code(Integer.parseInt(getTagValue("code",(Element)nodeItem)));
					arealist.add(areaVo);
				}catch (Exception e) {
					return null;
				}
			}
		
			return arealist;
		} catch(Exception e) {
			System.out.println("ERROR : getArea =" + e.getMessage());
			return null;
		}
	}
	
	public List<SigunguVo> getSigungu(int code) {
		String url = endPoint + "/rest/KorService/areaCode?serviceKey=" + serVicekey;
		String numOfRows = "&numOfRows=" + "100";
		String pageNo = "&pageNo=" + "1";
		String MobileOS = "&MobileOS=" + "ETC";
		String MobileApp = "&MobileApp=" + "Noppl";
		String areaCode = "&areaCode=" + Integer.toString(code);
		url += numOfRows + pageNo + MobileOS + MobileApp + areaCode;
		// System.out.println(url);
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(url);
			
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("item");
			
			if(nodeList.getLength() == 0) {
				System.out.println("검색 실패");
				return null;
			}
			
			for(int i=0; i<nodeList.getLength(); i ++) {
				Node nodeItem = nodeList.item(i);
				
				try {
					SigunguVo sigunguVo = new SigunguVo();
					sigunguVo.setSigungu_name(getTagValue("name",(Element)nodeItem));
					sigunguVo.setSigungu_code(Integer.parseInt(getTagValue("code",(Element)nodeItem)));
					sigunguVo.setArea_code(code);
					// System.out.println(sigunguVo.getArea_code() + " - " + sigunguVo.getSigungu_name() + " - " + sigunguVo.getSigungu_code());
					sigungulist.add(sigunguVo);
					
					getTour(sigunguVo.getArea_code() , sigunguVo.getSigungu_code());
					
					db.InsertSigungu(sigunguVo);
				}catch (Exception e) {
					return null;
				}
			}
		
			return sigungulist;
		} catch(Exception e) {
			System.out.println("ERROR : getArea =" + e.getMessage());
			return null;
		}
	}
	
	public List<TourVo> getTour(int city_code, int sigungu_code) {
		String url = endPoint + "/rest/KorService/areaBasedList?serviceKey=" + serVicekey;
		String numOfRows = "&numOfRows=" + "100";
		String pageNo = "&pageNo=" + "1";
		String MobileOS = "&MobileOS=" + "ETC";
		String MobileApp = "&MobileApp=" + "Noppl";
		String arrange = "&arrange=A";
		String contentTypeId = "&contentTypeId=12";
		String sigunguCode = "&sigunguCode=" + Integer.toString(sigungu_code);
		String areaCode = "&areaCode=" + Integer.toString(city_code);
		String listYN = "&listYN=Y";
		url += pageNo + numOfRows + MobileApp + MobileOS + arrange + contentTypeId + areaCode + sigunguCode + listYN;
		// System.out.println(url); 
		// &pageNo=1&numOfRows=100&MobileApp=Noppl&MobileOS=ETC&arrange=A&contentTypeId=12&areaCode=4&sigunguCode=4&listYN=Y
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(url);
			
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("item");
			
			if(nodeList.getLength() == 0) {
				System.out.println("검색 실패");
				return null;
			}
			
			//System.out.println(nodeList.getLength());
			
			for(int i=0; i<nodeList.getLength(); i ++) {
				Node nodeItem = nodeList.item(i);
				
				try {
					TourVo tourVo = new TourVo();
					tourVo.setAddr1(getTagValue("addr1",(Element)nodeItem));
					tourVo.setAddr2(getTagValue("addr2",(Element)nodeItem));
					tourVo.setAreacode(Integer.parseInt(getTagValue("areacode",(Element)nodeItem)));
					tourVo.setCat1(getTagValue("cat1",(Element)nodeItem));
					// System.out.println(tourVo.getCat1());
					tourVo.setCat2(getTagValue("cat2",(Element)nodeItem));
					// System.out.println(tourVo.getCat2());
					tourVo.setCat3(getTagValue("cat3",(Element)nodeItem));
					// System.out.println(tourVo.getCat3());
					tourVo.setContentid(getTagValue("contentid",(Element)nodeItem));
					// System.out.println(tourVo.getContentid());
					tourVo.setContenttypeid(Integer.parseInt(getTagValue("contenttypeid", (Element)nodeItem)));
					// System.out.println(tourVo.getContenttypeid());
					tourVo.setCreatedtime(getTagValue("createdtime", (Element)nodeItem));
					// System.out.println(tourVo.getCreatedtime()); 
					tourVo.setFirstimage(getTagValue("firstimage",(Element)nodeItem));
					// System.out.println(tourVo.getFirstimage());
					tourVo.setFirstimage2(getTagValue("firstimage2",(Element)nodeItem));
					// System.out.println(tourVo.getFirstimage2());
					tourVo.setMapx(getTagValue("mapx",(Element)nodeItem));
					// System.out.println(tourVo.getMapx());
					tourVo.setMapy(getTagValue("mapy",(Element)nodeItem));
					// System.out.println(tourVo.getMapy());
					tourVo.setMaplevel(Integer.parseInt(getTagValue("mlevel",(Element)nodeItem)));
					// System.out.println(tourVo.getMaplevel());
					tourVo.setModifiedtime(getTagValue("modifiedtime",(Element)nodeItem));
					// System.out.println(tourVo.getModifiedtime());
					tourVo.setReadcount(Integer.parseInt(getTagValue("readcount",(Element)nodeItem)));
					tourVo.setSigungucode(Integer.parseInt(getTagValue("sigungucode",(Element)nodeItem)));
					tourVo.setTel(getTagValue("tel",(Element)nodeItem));
					// System.out.println(tourVo.getTel());
					tourVo.setTitle(getTagValue("title",(Element)nodeItem));
					// System.out.println(tourVo.getTitle());
					tourVo.setZipcode(getTagValue("zipcode",(Element)nodeItem));
					// System.out.println(tourVo.getZipcode());
					
					//db.InsertTour(tourVo);
					
					getTourImage(tourVo.getContentid());
					getOverView(tourVo.getContentid());
					
					tourlist.add(tourVo);

					//db.InsertSigungu(sigunguVo);
				}catch (Exception e) {
					System.out.println("");
					return null;
				}
			}
			
			// System.out.println(tourlist.size());
		
			return tourlist;
		} catch(Exception e) {
			System.out.println("ERROR : getArea =" + e.getMessage());
			return null;
		}
	}
	
	
	public List<ImageVo> getTourImage(String Id) {
		String url = endPoint + "/rest/KorService/detailImage?serviceKey=" + serVicekey;
		String numOfRows = "&numOfRows=" + "100";
		String pageNo = "&pageNo=" + "1";
		String MobileOS = "&MobileOS=" + "ETC";
		String MobileApp = "&MobileApp=" + "Noppl";
		String contentId = "&contentId=" + Id;
		String imageYN = "&imageYN=Y";
		String subImageYN = "&subImageYN=Y";
		url += numOfRows + pageNo + MobileOS + MobileApp +contentId + imageYN + subImageYN;
		
		// System.out.println(url); 
		// &numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&contentId=1095732&imageYN=Y&subImageYN=Y
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(url);
			
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("item");
			
			if(nodeList.getLength() == 0) {
				// System.out.println("검색 실패 (이미지) : " + Id);
				return null;
			}
			
			for(int i=0; i<nodeList.getLength(); i ++) {
				Node nodeItem = nodeList.item(i);
				
				try {
					ImageVo imageVo = new ImageVo();
					imageVo.setContentid(getTagValue("contentid",(Element)nodeItem));
					imageVo.setOriginimgurl(getTagValue("originimgurl",(Element)nodeItem));
					imageVo.setSmallimageurl(getTagValue("serialnum", (Element)nodeItem));
					imagelist.add(imageVo);
					imaged++;
					// db입력문
					db.InsertImage(imageVo);
					if( imaged % 10 == 0) {
						System.out.println("이미지 : " + imaged);
					}
				}catch (Exception e) {
					return null;
				}
			}
		
			return imagelist;
		} catch(Exception e) {
			System.out.println("ERROR : getArea =" + e.getMessage());
			return null;
		}
	}
	
	public List<ImageVo> getOverView(String Id) {
		String url = endPoint + "/rest/KorService/detailCommon?serviceKey=" + serVicekey;
		String numOfRows = "&numOfRows=" + "1";
		String pageNo = "&pageNo=" + "1";
		String MobileOS = "&MobileOS=" + "ETC";
		String MobileApp = "&MobileApp=" + "Noppl";
		String contentId = "&contentId=" + Id;
		url += numOfRows + pageNo + MobileOS + MobileApp +contentId + "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";
		
		// System.out.println(url); 
		// &numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&contentId=129020&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(url);
			
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("item");
			
			if(nodeList.getLength() == 0) {
				// System.out.println("검색 실패 (개요) : " + Id);
				return null;
			}
			
			for(int i=0; i<nodeList.getLength(); i ++) {
				Node nodeItem = nodeList.item(i);
				
				try {
					OverviewVo overviewVo = new OverviewVo();
					overviewVo.setContentid(getTagValue("contentid",(Element)nodeItem));
					overviewVo.setOverview(getTagValue("overview",(Element)nodeItem));
					overviewlist.add(overviewVo);
					
					if( overviewd % 10 == 0) {
						System.out.println("개요 : " + overviewd);
						overviewd++;
					}
					// db입력문
					db.InsertOverview(overviewVo);
					
				}catch (Exception e) {
					return null;
				}
			}
		
			return imagelist;
		} catch(Exception e) {
			System.out.println("ERROR : getArea =" + e.getMessage());
			return null;
		}
	}
	
	public void InsertImageOverView() {
		List<String> list = new ArrayList<String>();
		list =  db.SelectContentid();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			String id = (String)it.next();
			// System.out.println(id);
			getOverView(id);
		}
		
	}
	
	
	public static void main(String args[]) {
		GetData g = new GetData();
		// g.getArea();
		// Iterator it = g.arealist.iterator();
		//g.getTour(31, 28);

		g.InsertImageOverView();
		System.out.println("완료");
		int i=0;
		//g.getSigungu(35);
		/*
		 * while(it.hasNext()) { AreaVo vo = (AreaVo)it.next(); // db.InsertArea(vo); //
		 * g.getSigungu(1); }
		 */
	}

	
	
	private String getTagValue(String sTag, Element element) {
		try {
			String result = element.getElementsByTagName(sTag).item(0).getTextContent();
			return result;
		}catch(Exception e) {
			return null;
		}
	}
	
	private int getTagValueInt(String sTag, Element element) {
		try {
			String result = element.getElementsByTagName(sTag).item(0).getTextContent();
			return Integer.parseInt(result);
		}catch(Exception e) {
			return 0;
		}
	}
}


// &numOfRows=17&pageNo=1&MobileOS=ETC&MobileApp=AppTest