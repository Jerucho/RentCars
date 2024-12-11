package controlador.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.clases.Auto;
import modelo.clases.Usuario;

import java.io.IOException;
import java.util.ArrayList;

import dao.AutoDAO;

/**
 * Servlet implementation class AutosSV
 */
@WebServlet(name = "autos", urlPatterns = { "/autos" })
public class AutosSV extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AutoDAO autoDAO = new AutoDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AutosSV() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession sesion = request.getSession();
		Usuario usuario = (Usuario) sesion.getAttribute("usuario");
		
		if (usuario == null) {
			response.sendRedirect("paginas/login.jsp");
		} else {
			if (usuario.getRol().equals("administrador")) {
				String action = request.getParameter("action");
				String dispatcher = "";

				switch (action != null ? action : "") {
				case "verAutos":
					ArrayList<Auto> autos = autoDAO.listarAutos();
					request.setAttribute("autos", autos);

					dispatcher = "/paginas/verAutos.jsp";
					request.getRequestDispatcher(dispatcher).forward(request, response);
					break;
				case "borrarAuto":
					doDelete(request, response);
					break;
				case "agregarAuto":
					dispatcher="/paginas/AgregarAuto.jsp";
					break;
				case "AquilarAuto":
					dispatcher="/paginas/AlquilarAuto.jsp";
					break;
				case "ModificarAuto":
					dispatcher="/paginas/ModificarAuto.jsp";
					break;
				case "verAlquilados":
					dispatcher = "/paginas/verAlquilados.jsp";
					break;
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");

		switch (action != null ? action : "") {

		}
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		response.getWriter().append("Auto eliminado");

		RequestDispatcher dispatcher = request.getRequestDispatcher("/paginas/verAutos.jsp");
		dispatcher.forward(request, response);

	}

}