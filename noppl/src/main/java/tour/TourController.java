package tour;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TourController {

	@Autowired
	private TourService tourService;
	
	// Tour에 관련된 모든 정보를 검색하여 TourInfoVo에 입력. ( AreaVo, SigunguVo, TourVo ) 각각의 코드값은 필요없음.
	@RequestMapping("/tour/index.do")
	public String index(HttpServletRequest req, TourVo vo) {
		// DB에 저장된 관광지 정보.
		List<TourVo> list = tourService.getList(vo);
		// DB에 저장된 지역 이름 정보.
		
		// DB에 저장된 시군구 이름 정보.
		
		
		req.setAttribute("list", list);
		
		return "tour/index";
	}
	
}
