﻿package forum;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

@WebServlet("/forum")
public class ForumController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	void translate(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		translate(request, response);
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();

		ForumVO fvo = new ForumVO();
		ForumDAO fdao = new ForumDAO();

		try {
			BeanUtils.copyProperties(fvo, request.getParameterMap());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		String action = request.getParameter("action");
	
	
		if (action.equals("")) {
			out.println("[심각] 어떠한 정보도 처리 할 수 없었습니다.");
			out.println("관리자 문의 요망");
		} else if (action.equals("inputUserId")) {
			String userId = request.getParameter("userId");
			System.out.println(userId);
			session.setAttribute("userId", userId);
			request.getRequestDispatcher("new_topic.jsp").forward(request, response);
			
		} else if (action.equals("selectOne")) {

			//int forumNum = Integer.parseInt(String.valueOf(request.getAttribute("forumNum")));
			int forumNum = Integer.parseInt(request.getParameter("forumNum"));
	
			try {
				
				fvo = fdao.selectOne(forumNum);
				
				request.setAttribute("postId", fvo.getPostId());
				request.setAttribute("forumNum", fvo.getForumNum());
				request.setAttribute("profile", fvo.getIdentityPhoto());
				request.setAttribute("postFile", fvo.getPostFile());
				request.setAttribute("postSubject", fvo.getPostSubject());
				request.setAttribute("postContent", fvo.getPostContent());
				request.setAttribute("sawCount", fvo.getSawCount());	
				request.setAttribute("comments", fvo.getComment());
			
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.getRequestDispatcher("topic.jsp").forward(request, response);
			
		} else if (action.equals("addpost")) {
			try {
				fdao.insert(fvo);
				response.sendRedirect("ShowForumList");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (action.equals("edit")) {
			int postId = Integer.parseInt(request.getParameter("postId"));
			int postNum = Integer.parseInt(request.getParameter("postNum"));
			String postContentSub = request.getParameter("postContentSub");
			String postContentText = request.getParameter("postContentText");

			System.out.println(postId);
			System.out.println(postNum);
			System.out.println(postContentSub);
			System.out.println(postContentText);
			
			
			try {
				fdao.update(postNum, postContentSub, postContentText);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			request.getRequestDispatcher("ShowForumList").forward(request, response);
		} else if (action.equals("del")) {
			int postNum = Integer.parseInt(request.getParameter("postNum"));
			try {
				fdao.delete(postNum);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			request.getRequestDispatcher("ShowForumList").forward(request, response);
		} else if (action.equals("logout")) {

			request.getSession(true).invalidate();
			request.getRequestDispatcher("ShowForumList").forward(request, response);
		}

		else {
			out.println("[심각] 에러 발생");
		}

	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
	
}
