<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>書籍の編集｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/thumbnail.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />       
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul style="margin: 0; padding: 0;">
                <li><a href="<%=request.getContextPath()%>/home" class="menu" style="color: #707070; text-decoration: none;">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/" style="color: #707070; text-decoration: none;">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <h1>貸出履歴一覧</h1>
    <div class="col-sm">
        <table class="table table-bordered " style="width: 50%">
            <thead>
                <tr class="table-primary">
                    <th>書籍名</th>
                    <th>貸出日</th>
                    <th>返却日</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="historyBookInfo" items="${historyList}">
                    <tr>
                        <td><a href="<%=request.getContextPath()%>/details?bookId=${historyBookInfo.bookId}" class="menu">${historyBookInfo.title} </a></td>
                        <td>${historyBookInfo.checkout_date}</td>
                        <td>${historyBookInfo.return_date}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>