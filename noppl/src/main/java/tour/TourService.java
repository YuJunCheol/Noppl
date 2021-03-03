package tour;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TourService {

	@Autowired
	private TourDao tourDao;
	
	public List<TourVo> getTourList(TourVo vo) {
		// limit 시작값 = (사용자가 요청한 페이지번호 - 1) * 페이지당갯수
		//int startIdx = (vo.getReqPage() - 1) * vo.getPageRow();
		//vo.setStartIdx((vo.getReqPage() - 1) * vo.getPageRow());
		return tourDao.selectTourList(vo);
	}
	
	public List<AreaVo> getAreaList(AreaVo vo) {
			
		return tourDao.selectAreaList(vo);
	}
	
	public List<SigunguVo> getSigunguList(SigunguVo vo) {
		return tourDao.selectSigunguList(vo);
	}
	
}
