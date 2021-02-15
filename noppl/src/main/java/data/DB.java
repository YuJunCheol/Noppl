package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tour.AreaVo;
import tour.SigunguVo;
import tour.TourVo;
import vo.ImageVo;
import vo.OverviewVo;

public class DB {
	
	protected Connection con = null;
	protected Statement st = null;
	protected ResultSet rs = null;
	protected PreparedStatement pstmt = null;
	
	private static String dburl = "jdbc:mysql://3.36.119.157:3306/noppldb?useUnicode=true&characterEncoding=utf8";
	
	public DB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(dburl,"root","ai1234");
			st = con.createStatement();
			
			System.out.println("연결 성공");
		}catch(Exception e) {
			System.out.println("ERROR : DBConnection - ");
			e.printStackTrace();
		}
	}
	
	
	public List<String>SelectContentid() {
		
		List<String> contentidlist = new ArrayList<String>();
		
		String sql = "SELECT contentId FROM tour_info where areacode = 36;";
	
		try {
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				contentidlist.add(rs.getString("contentid"));
			}
		} catch (Exception e) {
			System.out.println("ERROR SelectContentid : " + e.getMessage());
		}
		
		
		return contentidlist;
	}
	
	public void InsertArea(AreaVo vo) {
		int r = 0;
		String sql = "INSERT INTO area (city_code, city_name)"
				+ "VALUES(?,?);";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, vo.getArea_code() );
			pstmt.setString(2, vo.getArea_name() );
			r = pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InsertSigungu(SigunguVo vo) {
		int r = 0;
		String sql = "INSERT INTO sigungu (sigungu_code, city_code,sigungu_name)"
				+ "VALUES(?,?,?);";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, vo.getSigungu_code() );
			pstmt.setInt(2, vo.getArea_code() );
			pstmt.setString(3, vo.getSigungu_name() );
			r = pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InsertTour(TourVo vo) {
		int r = 0;
		String sql = "INSERT INTO tour_info (addr1, addr2,areacode,cat1,cat2,cat3,contentid,contenttypeid,createdtime,firstimage,firstimage2,mapx,mapy,mlevel,modifiedtime,readcount,sigungucode,tel,title,zipcode)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, vo.getAddr1());
			pstmt.setString(2, vo.getAddr2());
			pstmt.setInt(3, vo.getAreacode() );
			pstmt.setString(4, vo.getCat1());
			pstmt.setString(5, vo.getCat2() );
			pstmt.setString(6, vo.getCat3() );
			pstmt.setString(7, vo.getContentid());
			pstmt.setInt(8, vo.getContenttypeid());
			pstmt.setString(9, vo.getCreatedtime() );
			pstmt.setString(10, vo.getFirstimage() );
			pstmt.setString(11, vo.getFirstimage2());
			pstmt.setString(12, vo.getMapx());
			pstmt.setString(13, vo.getMapy());
			pstmt.setInt(14, vo.getMaplevel());
			pstmt.setString(15, vo.getModifiedtime());
			pstmt.setInt(16, 0 );
			pstmt.setInt(17, vo.getSigungucode());
			pstmt.setString(18, vo.getTel());
			pstmt.setString(19, vo.getTitle());
			pstmt.setString(20, vo.getZipcode() );
			
			r = pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InsertOverview(OverviewVo vo) {
		int r = 0;
		String sql = "INSERT INTO tour_overview (contentid, overview)"
				+ "VALUES(?,?);";
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, vo.getContentid());
			pstmt.setString(2, vo.getOverview());
			
			r = pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InsertImage(ImageVo vo) {
		int r = 0;
		String sql = "INSERT INTO tour_image (contentid,originimgurl,smallimageurl)"
				+ "VALUES(?,?,?);";
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, vo.getContentid());
			pstmt.setString(2, vo.getOriginimgurl());
			pstmt.setString(3, vo.getSmallimageurl() );
			
			r = pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
