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
	
	@RequestMapping("/tour/index.do")
	public String index(HttpServletRequest req, TourVo vo) {
		List<TourVo> list = tourService.getList(vo);
		
		req.setAttribute("list", list);
		
		return "tour/index";
	}
	
}
