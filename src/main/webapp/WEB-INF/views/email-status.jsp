<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Email Status</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">

    <h2 class="mb-4 text-primary text-center">Email Send Status</h2>

    <table class="table table-bordered table-hover">
        <thead class="table-dark">
            <tr>
                <th>HR Name</th>
                <th>HR Email</th>
                <th>Status</th>
                <th>SentAt</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="status" items="${emailStatuses}">
                <tr>
                    <td>${status.hrName}</td>
                    <td>${status.hrEmail}</td>
                    <td>
                        <c:choose>
                            <c:when test="${status.status == 'SENT'}">
                                <span class="badge bg-success">${status.status}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger">${status.status}</span>
                            </c:otherwise>
                        </c:choose>
                          <td>${status.formattedSentAt}</td> <!-- NEW VALUE -->
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="text-center">
        <a href="/" class="btn btn-primary mt-4">Back to Upload</a>
    </div>

</body>
</html>
