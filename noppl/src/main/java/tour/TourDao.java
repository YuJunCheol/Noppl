package tour;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TourDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public List<TourVo> selectList(TourVo vo) {
		return sqlSession.selectList("selectlist", vo);
	}
	
}
