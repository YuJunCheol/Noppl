package tour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TourDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public List<TourVo> selectTourList(TourVo vo) {
		return sqlSession.selectList("selectTour", vo);
	}
	
	public List<AreaVo> selectAreaList(AreaVo vo) {
//		List<AreaVo> avo = new ArrayList<AreaVo>();
//		avo = sqlSession.selectList("selectArea",vo);
//		
//		Iterator it = avo.iterator();
//		
//		while(it.hasNext()) {
//			AreaVo a = (AreaVo)it.next();
//			System.out.println(a.getCode());
//		}
		
		return sqlSession.selectList("selectArea",vo);
	}
	
	public List<SigunguVo> selectSigunguList(SigunguVo vo) {
		return sqlSession.selectList("selectSigungu", vo);
	}
	
}
