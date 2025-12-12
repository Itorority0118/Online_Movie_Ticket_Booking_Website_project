package controller;

import dao.MovieDAO;
import dao.ShowtimeDAO;
import model.Movie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/movie")
public class MovieServlet extends HttpServlet {

    private final MovieDAO movieDAO = new MovieDAO();
    private final ShowtimeDAO showtimeDAO = new ShowtimeDAO(); // kiểm tra ràng buộc showtime

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                request.setAttribute("movie", null);
                if (isAjax)
                    request.getRequestDispatcher("/admin/movie-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=movie-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Movie movieToEdit = movieDAO.getMovieById(editId);
                request.setAttribute("movie", movieToEdit);
                if (isAjax)
                    request.getRequestDispatcher("/admin/movie-form.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=movie-form.jsp").forward(request, response);
                break;
               
            case "now_showing":
            	
                List<Movie> nowShowingMovies = movieDAO.getAllMovies(); 
                request.setAttribute("movieList", nowShowingMovies);

                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
                
            case "coming_soon": 
                List<Movie> comingSoonMovies = movieDAO.getMoviesByStatus("Coming Soon"); 
                request.setAttribute("movieList", comingSoonMovies);

                request.getRequestDispatcher("/index.jsp").forward(request, response); 
                break;
            case "special_show": 
                List<Movie> specialMovies = movieDAO.getMoviesByStatus("Special Show");
                request.setAttribute("movieList", specialMovies);

                request.getRequestDispatcher("/moviespecial.jsp").forward(request, response); 
                break;

            case "list":
            default:
                String keyword = request.getParameter("keyword");

                int page = 1;
                int pageSize = 10;
                try {
                    page = Integer.parseInt(request.getParameter("page"));
                } catch (NumberFormatException ignored) {}

                List<Movie> movieList;
                if (keyword != null && !keyword.trim().isEmpty()) {
                    movieList = movieDAO.findByTitle(keyword.trim());
                } else {
                    movieList = movieDAO.getAllMovies();
                }

                request.setAttribute("movies", movieList);
                request.setAttribute("keyword", keyword);
                request.setAttribute("activeSidebar", "movie");
                if (isAjax)
                    request.getRequestDispatcher("/admin/movie-list.jsp").forward(request, response);
                else
                    request.getRequestDispatcher("/admin/dashboard.jsp?page=movie-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            int movieId = Integer.parseInt(request.getParameter("id"));

            if (showtimeDAO.existsByMovieId(movieId)) {
                out.print("{\"success\":false,\"message\":\"Cannot delete movie because it has showtimes or tickets.\"}");
            } else {
                boolean deleted = movieDAO.deleteMovie(movieId);
                out.print("{\"success\":" + deleted + "}");
            }
            out.flush();
            return;
        }

        // ---------- ADD / UPDATE MOVIE ----------
        String idStr = request.getParameter("id");
        Movie movie = new Movie();
        movie.setTitle(request.getParameter("title"));
        movie.setGenre(request.getParameter("genre"));
        movie.setDirector(request.getParameter("director"));
        movie.setCast(request.getParameter("cast"));
        movie.setDescription(request.getParameter("description"));
        movie.setDuration(Integer.parseInt(request.getParameter("duration")));
        movie.setLanguage(request.getParameter("language"));
        movie.setReleaseDate(request.getParameter("releaseDate"));
        movie.setPosterUrl(request.getParameter("posterUrl"));
        movie.setTrailerUrl(request.getParameter("trailerUrl"));
        movie.setStatus(request.getParameter("status"));

        if (idStr == null || idStr.isEmpty()) {
            movieDAO.addMovie(movie);
        } else {
            movie.setMovieId(Integer.parseInt(idStr));
            movieDAO.updateMovie(movie);
        }

        response.sendRedirect(request.getContextPath() + "/movie?action=list");
    }
}
