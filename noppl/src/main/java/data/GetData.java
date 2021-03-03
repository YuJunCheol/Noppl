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
					areaVo.setCity_name(getTagValue("name",(Element)nodeItem));
					areaVo.setCity_code(Integer.parseInt(getTagValue("code",(Element)nodeItem)));
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
					
					getTour(sigunguVo.getArea_code() );
					
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
	
	public List<TourVo> getTour(int city_code) {
		
		String url = endPoint + "/rest/KorService/areaBasedList?serviceKey=" + serVicekey;
		String numOfRows = "&numOfRows=" + "339";
		String pageNo = "&pageNo=" + "1";
		String MobileOS = "&MobileOS=" + "ETC";
		String MobileApp = "&MobileApp=" + "Noppl";
		String arrange = "&arrange=A";
		String contentTypeId = "&contentTypeId=12";
		String sigunguCode = "&sigunguCode= ";
		String areaCode = "&areaCode=" + Integer.toString(city_code);
		String listYN = "&listYN=Y";
		url += pageNo + numOfRows + MobileApp + MobileOS + arrange + contentTypeId + areaCode + listYN;
		//System.out.println(url); 
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
			
			for(int i=0; i < nodeList.getLength(); i ++) {
				Node nodeItem = nodeList.item(i);
				
				try {
					TourVo tourVo = new TourVo();
					
					if(getTagValue("addr1",(Element)nodeItem) != null)
						tourVo.setAddr1(getTagValue("addr1",(Element)nodeItem));
					if(getTagValue("addr2",(Element)nodeItem) != null)
						tourVo.setAddr2(getTagValue("addr2",(Element)nodeItem));
					if(getTagValue("areacode",(Element)nodeItem) != null)
						tourVo.setAreacode(Integer.parseInt(getTagValue("areacode",(Element)nodeItem)));
					if(getTagValue("cat1",(Element)nodeItem) != null)
						tourVo.setCat1(getTagValue("cat1",(Element)nodeItem));
					if(getTagValue("cat2",(Element)nodeItem) != null)
						tourVo.setCat2(getTagValue("cat2",(Element)nodeItem));
					if(getTagValue("cat3",(Element)nodeItem) != null)
						tourVo.setCat3(getTagValue("cat3",(Element)nodeItem));
					if(getTagValue("contentid",(Element)nodeItem) != null)
						tourVo.setContentid(getTagValue("contentid",(Element)nodeItem));
					if(getTagValue("contenttypeid", (Element)nodeItem) != null)
						tourVo.setContenttypeid(Integer.parseInt(getTagValue("contenttypeid", (Element)nodeItem)));
					if (getTagValue("createdtime", (Element)nodeItem) != null)
						tourVo.setCreatedtime(getTagValue("createdtime", (Element)nodeItem));
					if(getTagValue("firstimage",(Element)nodeItem) != null) 
						tourVo.setFirstimage(getTagValue("firstimage",(Element)nodeItem));
					if(getTagValue("firstimage2",(Element)nodeItem) != null) 
						tourVo.setFirstimage2(getTagValue("firstimage2",(Element)nodeItem));
					if(getTagValue("mapx",(Element)nodeItem) != null) 
						tourVo.setMapx(getTagValue("mapx",(Element)nodeItem));
					if(getTagValue("mapy",(Element)nodeItem) != null)
						tourVo.setMapy(getTagValue("mapy",(Element)nodeItem));
					if(getTagValue("mlevel",(Element)nodeItem) != null)
						tourVo.setMaplevel(Integer.parseInt(getTagValue("mlevel",(Element)nodeItem)));
					if(getTagValue("modifiedtime",(Element)nodeItem) != null)
						tourVo.setModifiedtime(getTagValue("modifiedtime",(Element)nodeItem));
					if(getTagValue("readcount",(Element)nodeItem) != null)
						tourVo.setReadcount(Integer.parseInt(getTagValue("readcount",(Element)nodeItem)));
					if(getTagValue("sigungucode",(Element)nodeItem) != null)
						tourVo.setSigungucode(Integer.parseInt(getTagValue("sigungucode",(Element)nodeItem)));
					if(getTagValue("tel",(Element)nodeItem) != null)
						tourVo.setTel(getTagValue("tel",(Element)nodeItem));
					if(getTagValue("title",(Element)nodeItem) != null)
						tourVo.setTitle(getTagValue("title",(Element)nodeItem));
					if(getTagValue("zipcode",(Element)nodeItem) != null)
						tourVo.setZipcode(getTagValue("zipcode",(Element)nodeItem));
					
					db.InsertTour(tourVo);
					
					// System.out.println(getTagValue("contentid",(Element)nodeItem));
					// getTourImage(tourVo.getContentid());
					// getOverView(tourVo.getContentid());
					
					// tourlist.add(tourVo);

					//db.InsertSigungu(sigunguVo);
				}catch (Exception e) {
					System.out.println("ERROR : " + e.getMessage());
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
	
	public static void main(String args[]) {
		GetData g = new GetData();
		// g.getArea();
		// Iterator it = g.arealist.iterator();
		g.getTour(39);

		//g.InsertImageOverView();
		System.out.println("완료");
		//int i=0;
		//g.getSigungu(35);
		/*
		 * while(it.hasNext()) { AreaVo vo = (AreaVo)it.next(); // db.InsertArea(vo); //
		 * g.getSigungu(1); }
		 */
		
		
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