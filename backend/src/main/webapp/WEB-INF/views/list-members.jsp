<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
  <head>
    <title>Membership Management</title>
    <style>
      table {
        width: 100%;
        border-collapse: collapse;
      }
      th, td {
        padding: 8px;
        text-align: left;
        border-bottom: 1px solid #ddd;
      }
      form {
        margin-bottom: 20px;
      }
      input, button {
        margin: 5px;
        padding: 8px;
      }
      .action-btn {
        margin-right: 5px;
      }
      .add-btn {
        background-color: #4CAF50;
        color: white;
        border: none;
        padding: 10px 15px;
        cursor: pointer;
        margin-bottom: 20px;
      }
    </style>
  </head>
  <body>
    <h1>Members</h1>
    
    <!-- Add Member Button -->
    <a href="${pageContext.request.contextPath}/members/add">
      <button class="add-btn">Add New Member</button>
    </a>
    
    <!-- Members Table -->
    <table border="1">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Email</th>
          <th>Join Date</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${empty members}">
            <tr>
              <td colspan="5">No members found.</td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="member" items="${members}">
              <tr>
                <td>${member.id}</td>
                <td>${member.name}</td>
                <td>${member.email}</td>
                <td><fmt:formatDate value="${member.joinDate}" pattern="yyyy-MM-dd" /></td>
                <td>
                  <form action="${pageContext.request.contextPath}/members/edit/${member.id}" method="get" style="display:inline;">
                    <button type="submit" class="action-btn">Edit</button>
                  </form>
                  <form action="${pageContext.request.contextPath}/members" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${member.id}">
                    <button type="submit" class="action-btn" onclick="return confirm('Are you sure you want to delete this member?')">Delete</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </body>
</html>
