package controller;

import dao.MovieDAO;
import dao.ShowtimeDAO;
import model.Movie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/movie")
public class MovieServlet extends HttpServlet {

    private final MovieDAO movieDAO = new MovieDAO();
    private final ShowtimeDAO showtimeDAO = new ShowtimeDAO();

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
                out.print("{\"success\":false,\"message\":\"Không thể xóa phim này vì còn lịch chiếu hoặc vé.\"}");
            } else {
                boolean deleted = movieDAO.deleteMovie(movieId);
                out.print("{\"success\":" + deleted + "}");
            }
            out.flush();
            return;
        }

        String idStr = request.getParameter("id");

        String title = request.getParameter("title");
        String genre = request.getParameter("genre");
        String director = request.getParameter("director");
        String cast = request.getParameter("cast");
        String description = request.getParameter("description");
        String durationStr = request.getParameter("duration");
        String language = request.getParameter("language");
        String releaseDate = request.getParameter("releaseDate");
        String posterUrl = request.getParameter("posterUrl");
        String trailerUrl = request.getParameter("trailerUrl");
        String status = request.getParameter("status");

        // -------- VALIDATION --------
        Map<String, String> errors = new HashMap<>();

        if (title == null || title.trim().length() < 2)
            errors.put("title", "Title phải ít nhất 2 kí tự");
        if (genre == null || genre.trim().length() < 2)
            errors.put("genre", "Genre phải ít nhất 2 kí tự");
        if (director == null || director.trim().length() < 2)
            errors.put("director", "Director phải ít nhất 2 kí tự");
        if (cast == null || cast.trim().length() < 2)
            errors.put("cast", "Cast phải ít nhất 2 kí tự");
        if (language == null || language.trim().length() < 2)
            errors.put("language", "Language phải ít nhất 2 kí tự");
        if (durationStr == null || !durationStr.matches("\\d+") || Integer.parseInt(durationStr) <= 0)
            errors.put("duration", "Duration phải là số nguyên");
        if (releaseDate == null || releaseDate.trim().isEmpty())
            errors.put("releaseDate", "Release Date chưa chọn");
        if (status == null || !(status.equals("Now Showing") || status.equals("Coming Soon") || status.equals("Archived")))
            errors.put("status", "Invalid status");

        if (!errors.isEmpty()) {
            Movie movie = new Movie();
            if (idStr != null && !idStr.isEmpty())
                movie.setMovieId(Integer.parseInt(idStr));
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setDirector(director);
            movie.setCast(cast);
            movie.setDescription(description);
            movie.setDuration(durationStr != null && !durationStr.isEmpty() ? Integer.parseInt(durationStr) : 0);
            movie.setLanguage(language);
            movie.setReleaseDate(releaseDate);
            movie.setPosterUrl(posterUrl);
            movie.setTrailerUrl(trailerUrl);
            movie.setStatus(status);

            request.setAttribute("errors", errors);
            request.setAttribute("movie", movie);
            request.getRequestDispatcher("/admin/dashboard.jsp?page=movie-form.jsp")
                    .forward(request, response);
            return;
        }

        Movie movie = new Movie();
        if (idStr != null && !idStr.isEmpty())
            movie.setMovieId(Integer.parseInt(idStr));

        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setDirector(director);
        movie.setCast(cast);
        movie.setDescription(description);
        movie.setDuration(Integer.parseInt(durationStr));
        movie.setLanguage(language);
        movie.setReleaseDate(releaseDate);
        movie.setPosterUrl(posterUrl);
        movie.setTrailerUrl(trailerUrl);
        movie.setStatus(status);

        if (idStr == null || idStr.isEmpty()) {
            movieDAO.addMovie(movie);
        } else {
            movieDAO.updateMovie(movie);
        }
        response.sendRedirect(request.getContextPath() + "/movie?action=list");
    }
}