package controller;

import dao.MovieDAO;
import model.Movie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/movie")
public class MovieServlet extends HttpServlet {

    private MovieDAO movieDAO = new MovieDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                // Show form to create new movie
                request.getRequestDispatcher("/movie-form.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Movie movieToEdit = movieDAO.getMovieById(editId);
                request.setAttribute("movie", movieToEdit);
                request.getRequestDispatcher("/movie-form.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                movieDAO.deleteMovie(deleteId);
                response.sendRedirect("movie");
                break;

            default:
                // Show all movies
                List<Movie> movieList = movieDAO.getAllMovies();
                request.setAttribute("movies", movieList);
                request.getRequestDispatcher("/movie-list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // handle UTF-8 characters

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

        Movie movie = new Movie();
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
            // Add new movie
            movieDAO.addMovie(movie);
        } else {
            // Update existing movie
            movie.setMovieId(Integer.parseInt(idStr));
            movieDAO.updateMovie(movie);
        }

        response.sendRedirect("movie");
    }
}
