package tour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TourController {

	@Autowired
	private TourService tourService;

	// Tour에 관련된 모든 정보를 검색하여 TourInfoVo에 입력. ( AreaVo, SigunguVo, TourVo ) 각각의 코드값은
	// 필요없음.
	@RequestMapping("/tour/index.do")
	public String index(HttpServletRequest req, AreaVo vo) {
		// 1. 지역으로 검색.
		int no = 1;
		
		System.out.println(vo.getSelectopt());
		
		// 지역기반 검색.
		List<AreaVo> areaList = tourService.getAreaList(vo); 
		List<TourInfoVo> list = new ArrayList<TourInfoVo>();
		
		Iterator i = areaList.iterator();
		
		while (i.hasNext()) {
			TourInfoVo tvoinfo = new TourInfoVo();
			AreaVo area = (AreaVo)i.next();
			tvoinfo.setNo(no++);
			tvoinfo.setArea(area.getTitle());
			// TourVo 에서 관광지 명 후기 수 가져오기.
			list.add(tvoinfo);
		}
	
		req.setAttribute("list", list);

			
		return "tour/index";
		}

}


