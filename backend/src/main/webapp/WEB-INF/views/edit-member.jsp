<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
  <head>
    <title>Edit Member</title>
    <style>
      form {
        max-width: 500px;
        margin: 0 auto;
      }
      .form-group {
        margin-bottom: 15px;
      }
      label {
        display: block;
        margin-bottom: 5px;
      }
      input {
        width: 100%;
        padding: 8px;
        box-sizing: border-box;
      }
      button {
        padding: 10px 15px;
        background-color: #4CAF50;
        color: white;
        border: none;
        cursor: pointer;
      }
      .cancel-btn {
        background-color: #f44336;
        margin-left: 10px;
      }
    </style>
  </head>
  <body>
    <h1>Edit Member</h1>
    
    <form action="${pageContext.request.contextPath}/members" method="post">
      <input type="hidden" name="action" value="edit">
      <input type="hidden" name="id" value="${member.id}">
      
      <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="${member.name}" required>
      </div>
      
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${member.email}" required>
      </div>
      
      <div class="form-group">
        <label for="join_date">Join Date:</label>
        <input type="date" id="join_date" name="join_date" 
               value="<fmt:formatDate value="${member.join_date}" pattern="yyyy-MM-dd" />" required>
      </div>
      
      <div class="form-group">
        <button type="submit">Update Member</button>
        <a href="${pageContext.request.contextPath}/members">
          <button type="button" class="cancel-btn">Cancel</button>
        </a>
      </div>
    </form>
  </body>
</html>
