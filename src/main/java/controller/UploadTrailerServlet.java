package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/uploadTrailer")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,   // 1MB
        maxFileSize = 50 * 1024 * 1024,    // 50MB
        maxRequestSize = 100 * 1024 * 1024 // 100MB
)
public class UploadTrailerServlet extends HttpServlet {

    private final String UPLOAD_DIR = "videos/trailers";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Part filePart = request.getPart("trailerFile");
            if (filePart == null || filePart.getSize() == 0) {
                out.print("{\"success\":false,\"message\":\"No file selected\"}");
                return;
            }

            String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

            if (!fileExt.matches("mp4|mov|avi|mkv")) {
                out.print("{\"success\":false,\"message\":\"Only MP4, MOV, AVI, MKV allowed\"}");
                return;
            }

            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) 
                              + "_" + timestamp + "." + fileExt;

            String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            File file = new File(uploadDir, fileName);
            filePart.write(file.getAbsolutePath());

            String fileUrl = UPLOAD_DIR + "/" + fileName;

            out.print("{\"success\":true,\"fileUrl\":\"" + fileUrl + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Upload failed: " + e.getMessage() + "\"}");
        }
    }
}
