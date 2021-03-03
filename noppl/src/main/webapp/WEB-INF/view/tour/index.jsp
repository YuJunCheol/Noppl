<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="tour.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>

<script>

function getSelect() {
	$("input[name='selectopt']").val($("#selectopt").val());
}

</script>

</head>
<body>
<h3>관광지 목록</h3>

<form action="index.do" onsubmit="getSelect();" >
	<select id ="selectopt">
		<option value="area">지역</option>
		<option value="tour">관광지 명</option>
	</select> 
	<input type="text" name="searchArea" value="${param.searchArea }">
	<input type = "submit" value = "검색">
</form>

<table	border ="1">
	<tr>
		<th>No.</th><th>관광지명</th><th>지역명</th><th>시군구 이름</th><th>후기 수</th><th>조회 수</th><th>최종 수정일</th>
	</tr>
	<c:forEach var = "vo" items ="${list}" >
		<tr>
			<td>${vo.no}</td>
			<td>${vo.title}</td>
			<td>${vo.area}</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</c:forEach>
</table>
</body>
</html>