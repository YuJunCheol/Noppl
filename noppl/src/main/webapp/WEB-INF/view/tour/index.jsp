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
</head>
<body>
관광지 목록
<table	border ="1">
	<tr>
		<th>No.</th><th>관광지명</th><th>제목</th><th>추천 수</th><th>작성자</th><th>등록일</th><th> </th>
	</tr>
	<c:forEach var = "vo" items ="${list}">
		<tr>
			<td>${vo.areacode }</td>
			<td>${vo.title}</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</c:forEach>
</table>
</body>
</html>