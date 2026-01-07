package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

@WebServlet("/uploadPoster")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 5 * 1024 * 1024,   // 5MB
        maxRequestSize = 10 * 1024 * 1024 // 10MB
)
public class UploadServlet extends HttpServlet {

    private final String UPLOAD_DIR = "images/movies";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Part filePart = request.getPart("posterFile");
            if (filePart == null || filePart.getSize() == 0) {
                out.print("{\"success\":false,\"message\":\"No file selected\"}");
                return;
            }

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            if (!fileExt.matches("jpg|jpeg|png")) {
                out.print("{\"success\":false,\"message\":\"Only JPG, JPEG, PNG allowed\"}");
                return;
            }

            String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            File file = new File(uploadDir, fileName);
            if (file.exists()) file.delete();

            filePart.write(file.getAbsolutePath());

            String fileUrl = UPLOAD_DIR + "/" + fileName;

            out.print("{\"success\":true,\"fileUrl\":\"" + fileUrl + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Upload failed: " + e.getMessage() + "\"}");
        }
    }
}