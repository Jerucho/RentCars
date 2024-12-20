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
			if (usuario.getRol().equals("Usuario") || usuario.getRol().equals("Administrador")) {
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
					int idAuto = Integer.parseInt(request.getParameter("id"));
					Auto autoBorrado = autoDAO.obtenerAuto(idAuto);
					request.setAttribute("auto", autoBorrado);
					System.out.println("Auto a borrar: " + idAuto);
					dispatcher = "/paginas/BorrarAuto.jsp";
					request.getRequestDispatcher(dispatcher).forward(request, response);
					break;
				case "agregarAuto":
					dispatcher = "/paginas/AgregarAuto.jsp";
					request.getRequestDispatcher(dispatcher).forward(request, response);
					break;
				case "modificarAuto":
					int id = Integer.parseInt(request.getParameter("id"));
					Auto auto = autoDAO.obtenerAuto(id);
					request.setAttribute("auto", auto);
					dispatcher = "/paginas/ModificarAuto.jsp";
					request.getRequestDispatcher(dispatcher).forward(request, response);
					break;
				case "verAlquilados":
					ArrayList<Auto> autosAlquilados = autoDAO.listarAlquilados();
					request.setAttribute("autos", autosAlquilados);
					dispatcher = "/paginas/VerAlquilados.jsp";
					request.getRequestDispatcher(dispatcher).forward(request, response);
					break;
				case "autosConMasKilometraje":
					ArrayList<Auto> autosConMasKilometraje = autoDAO.buscarAutosConMasKilometraje();
					request.setAttribute("listaAutos", autosConMasKilometraje);
					dispatcher = "/paginas/ReporteAutosMasKilometraje.jsp";
					request.getRequestDispatcher(dispatcher).forward(request, response);
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
		case "modificarAuto":
			doPut(request, response);
			break;
		case "agregarAuto":
			String marca = request.getParameter("marca");
			String modelo = request.getParameter("modelo");
			String anio = request.getParameter("anio");
			String color = request.getParameter("color");
			String matricula = request.getParameter("matricula");
			double precioDia = Double.parseDouble(request.getParameter("precioDia"));
			String estado = request.getParameter("estado");
			String img = request.getParameter("img");
			double kilometraje = Double.parseDouble(request.getParameter("kilometraje"));

			Auto autoAgregado = new Auto(marca, modelo, anio, color, matricula, precioDia, estado, img, kilometraje);

			autoDAO.registrarAuto(autoAgregado);
			response.sendRedirect("autos?action=verAutos");
			break;
		case "borrarAuto":
			doDelete(request, response);
			break;
		default:
			break;
		}

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Auto auto = recuperarParametrosAuto(request);
		autoDAO.modificarAuto(auto.getId(), auto);
		response.sendRedirect("autos?action=verAutos");
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		response.getWriter().append("Auto eliminado");

		autoDAO.eliminarAuto(id);

		response.sendRedirect("autos?action=verAutos");
	}

	private Auto recuperarParametrosAuto(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		String marca = request.getParameter("marca");
		String modelo = request.getParameter("modelo");
		String anio = request.getParameter("anio");
		String color = request.getParameter("color");
		String matricula = request.getParameter("matricula");
		double precioDia = Double.parseDouble(request.getParameter("precioDia"));
		String estado = request.getParameter("estado");
		String img = request.getParameter("img");
		double kilometraje = Double.parseDouble(request.getParameter("kilometraje"));

		Auto auto = new Auto(id, marca, modelo, anio, color, matricula, precioDia, estado, img, kilometraje);

		return auto;
	}

}