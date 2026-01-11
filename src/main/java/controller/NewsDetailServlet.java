package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.*;
import model.News;

@WebServlet("/news-detail")
public class NewsDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/news");
            return;
        }

        Map<String, News> newsData = new HashMap<>();

        newsData.put("1", new News(
            1,
            "🎁 Giảm giá sốc khi thay toán bằng VPPAY",
            "18/12/2025",
            "image1.jpg",
            """
            <p>Beta Cinemas mang đến ưu đãi hấp dẫn <b>“Thanh toán bằng VPPAY”</b>
            được giảm giá lên tới <b>10% tối đa 50.000đ</b>.</p>

            <ul>
                <li>🎬 Áp dụng cho tất cả suất chiếu</li>
                <li>Hướng dẫn thanh toán VNPAY-QR để hưởng khuyến mại:
					- Bước 1: Khách hàng chọn phim và/hoặc combo và/ hoặc bỏng nước trên website/APP CGV
					- Bước 2: Chọn hình thức thanh toán bằng VNPAY-QR
					- Bước 3: Đăng nhập ứng dụng Mobile Banking, chọn tính năng QR Pay
					- Bước 4: Quét mã QR hiển thị trên màn hình, nhập mã khuyến mại (YEUVNPAY) và hoàn tất giao dịch
					
					Lưu ý:
					- Khuyến mại chỉ áp dụng khi mua vé và/ hoặc bỏng, nước trên website CGV.
					- Thanh toán bằng ứng dụng Mobile Banking trên điện thoại di động của các ngân hàng sau: Agribank, BIDV, VietinBank, Vietcombank, SCB, IVB, VPBank, ABBank, Eximbank, HDBank, NCB, Nam A Bank, Vietbank, BIDC bank, Saigonbank, SeABank, Ocean Bank, Kienlongbank cùng nhiều ngân hàng khác, chi tiết tại: https://vnpay.vn/khuyenmai.
					- Mỗi khách hàng chỉ được sử dụng Mã giảm giá YEUVNPAY 1 lần/tuần.
					- VNPAY có quyền quyết định cuối cùng trong việc xét duyệt các giao dịch hợp lệ.
                </li>
                <li>🎟 Không giới hạn số lượng vé</li>
            </ul>
            """
        ));

        newsData.put("2", new News(
            2,
            "Into the Future chính thức ra rạp",
            "15/12/2025",
            "image2.jpg",
            """
            <p>Bộ phim khoa học viễn tưởng được mong chờ nhất năm 2025
            đã chính thức khởi chiếu tại <b>Beta Cinemas</b>.</p>
            <li> "Into The Future" là một bộ phim tài liệu khám phá hành trình mà việc "suy tưởng về ngày mai" đã trở thành một hiện tượng văn hóa đại chúng với tên gọi: Khoa học Viễn tưởng (Science Fiction).

Dù có nguồn gốc phong phú và đa dạng, nhưng chỉ có một cái tên duy nhất được ghi nhận là người đã đơn phương định hình cách chúng ta hiểu về khoa học viễn tưởng như một thể loại độc lập ngày nay. Đó chính là Hugo Gernsback. Ông là người đã "nhúng tay" vào mớ hỗn độn của dòng văn học rẻ tiền (pulp literature) hồi đầu thế kỷ 20 để chắt lọc ra tinh túy của khoa học viễn tưởng – đặt cho nó một cái tên, đưa ra một định nghĩa rõ ràng, và biến nó thành một thể loại mà bất kỳ ai cũng có thể tiếp cận.

Mục tiêu của ông là truyền cảm hứng và kích thích trí tuệ của giới trẻ, thúc đẩy họ suy đoán về cách khoa học sẽ nhào nặn tương lai và gợi mở những phát minh mới cho ngày mai. Trong quá trình đó, ông đã vô tình kiến tạo nên cả một dòng chảy khoa học viễn tưởng.

Cộng đồng người hâm mộ lớn mạnh xoay quanh thể loại này đã biến khoa học viễn tưởng thành một phong trào văn hóa và giữ cho nó không ngừng tiến hóa. Mỗi thế hệ lại điều chỉnh nó để thích nghi với những tiến bộ khoa học đương thời, cũng như để phản ánh những hy vọng và nỗi sợ hãi của thời đại đó.

Dưới sự dẫn dắt của các tác giả, nhà sử học, nhà làm phim, nhà thiên văn học và các kỹ sư tên lửa, "Hướng Về Tương Lai" đan xen giữa các cuộc phỏng vấn, hoạt hình và các biểu tượng văn hóa đại chúng để đưa người xem vào một hành trình thú vị và đầy tính giải trí nhằm chứng kiến sự tiến hóa này. Qua đó, bộ phim cũng giúp chúng ta khám phá ra một sự thật rằng: Cách chúng ta kể những câu chuyện về ngày mai thực chất phản ánh về con người chúng ta ở hiện tại nhiều hơn là về tương lai.
            </li>
			"""

        ));

        newsData.put("3", new News(
            3,
            "Action Blast gây sốt phòng vé tuần đầu",
            "14/12/2025",
            "image3.jpg",
            """
            	<p>Action Blast đã lập kỷ lục doanh thu tuần đầu.</p>
            	<li>Phim xoay quanh cuộc đấu trí giữa cảnh sát đặc nhiệm và một thiên tài chế tạo bom ẩn danh. 
            	Khi các địa điểm biểu tượng của thành phố lần lượt bị đặt vào tình trạng báo động đỏ, lực lượng an ninh rơi vào tình thế tiến thoái lưỡng nan.
            	 Nhân vật chính – một cựu binh chuyên về chất nổ – phải chạy đua với thời gian để giải mã các câu đố chết người mà kẻ thủ ác đặt ra.
	            Không chỉ dừng lại ở những pha cháy nổ hoành tráng, BLAST còn đi sâu vào những góc tối của sự phản bội và khao khát công lý.
	             Một bộ phim hội tụ đầy đủ các yếu tố: Hành động mãn nhãn, cốt truyện lôi cuốn và những cú lật kèo (twist) không thể đoán trước.
	            </li>
            """
            		
        ));

        newsData.put("4", new News(
            4,
            "Mystery Island – Bí ẩn chưa lời giải",
            "13/12/2025",
            "image4.jpg",
            """
            <p>Bộ phim trinh thám – tâm lý đầy bất ngờ.</p>
            <li> Ẩn mình giữa đại dương mênh mông và bị bao phủ bởi những cơn bão vĩnh cửu, Mystery Island là nơi mà bản đồ thế giới chưa từng ghi dấu.
             Khi một nhóm nhà thám hiểm vô tình lạc bước vào đây, họ phát hiện ra một hệ sinh thái kỳ diệu với những sinh vật cổ đại và những kho báu bị lãng quên.
            Nhưng hòn đảo không chào đón những vị khách lạ một cách yên bình.
             Để tìm đường về nhà, họ phải giải mã những bí ẩn nằm sâu trong những hang động pha lê và đối mặt với thế lực canh giữ hòn đảo từ hàng nghìn năm qua.
              Một chuyến phiêu lưu mãn nhãn dành cho mọi lứa tuổi!
            </li>
            """
        ));

        newsData.put("5", new News(
            5,
            "🎉 Ưu đãi học sinh – sinh viên",
            "12/12/2025",
            "image5.jpg",
            """
            <p>Giá vé chỉ có 45k cho HSSV thỏa mái đam mê.</p>
            <p>Điều kiện áp dụng giá vé</p>
            <li>Giá vé U22 thay đổi tùy theo rạp phim và ngày mua trong tuần.</li>
            <li>Áp dụng khách hàng thành viên từ 22 tuổi trở xuống và cao trên 1m3</li>
            <li>Áp dụng tích điểm thành viên</li>
            <li>Không áp dụng các suất chiếu đặc biệt và suất chiếu sớm</li>
            <li>Không áp dụng đồng thời với các chương trình khuyến mãi khác</li>
            <li>Trong mọi trường hợp, quyết định của Galaxy Cinema là quyết định cuối cùng</li>
            """
        ));

        newsData.put("6", new News(
            6,
            "🎟 Thành viên T1 – Nhận ưu đãi đặc biệt",
            "11/12/2025",
            "image6.png",
            """
            <p>Ưu đãi dành riêng cho thành viên.</p>
            <p>Năm 2026, Galaxy Cinema tiếp tục duy trì chính sách tích lũy điểm thưởng và sử dụng thanh toán vé/ bắp nước.
             1. Điểm tích lũy còn lại của năm 2025 (tính tới 31.12.2024) sẽ không còn hiệu lực sử dụng từ 28.01.2026. 
              2. Điểm tích lũy từ 01.01.2026 tới hết 27.07.2025 sẽ được bảo lưu và khách hàng có thể sử dụng tiếp tục trong năm 2026.
               3. Vé thăng hạng thành viên VIP 2025 vẫn còn hiệu lực nếu chưa được sử dụng. 
               Khách hàng thành viên có sinh nhật trong tháng sẽ được TẶNG COMBO 2: 01 bắp 02 nước. 
               G-Star và X-Star vừa nhận combo vừa có thêm vé mừng sinh nhật.
                Đặc biệt, quà tặng sinh nhật combo 2 trẻ em áp dụng khi trẻ em đi kèm người lớn là thành viên T1 Cinema.
                Thành viên phải có ít nhất 1 giao dịch (vé/ bắp nước) với chi tiêu > 0 trong vòng 12 tháng trở lại.
                 Áp dụng cho trẻ em có chiều cao dưới 1m3 & ảnh chụp khai sinh/ thẻ học sinh hợp lệ trong tháng sinh nhật trẻ em.
             </p>
             
             <p>Lưu Ý:
             *Bắp và nước sẽ được nhân viên tư vấn tại quầy sau khi check thông tin của khách hàng(khong thể order trên web)
             *Vé sinh nhật có thể sử dụng trong tháng tương ứng.
             *Quà tặng sinh nhật (combo 2, vé xem phim 2D dành cho thành viên hạng G-Star, X-Star) được thả vào tài khoản thành viên & có giá trị sử dụng hiệu lực trong tháng sinh nhật thành viên.
             *Thành viên phải có ít nhất 1 giao dịch (vé/ bắp nước) với chi tiêu > 0 trong vòng 12 tháng trở lại.
             *Thành viên tiếp tục được tặng vé khi nâng hạng thành viên VIP G-Star và X-Star.
             *Số vé tặng hạng thành viên G-Star 02 vé/năm. Khi thành viên G-Star thoả điều kiện nâng hạng thành X-Star, số vé tặng thêm là 02 vé. Tổng số vé tặng hạng thành viên X-Star là 04 vé/năm.
             *Khi đổi quà tặng sinh nhật (vé/ combo bắp nước), quý khách vui lòng mang giấy tờ tùy thân (CCCD/ CMND...)
             *Trong mọi trường hợp, quyết định của Galaxy Cinema là quyết định cuối cùng.       
             </p> 
           """
        ));

        newsData.put("7", new News(
            7,
            "Summer Love – Phim tình cảm đáng xem mùa hè",
            "10/12/2025",
            "image7.jpg",
            """            
            <li>Dưới cái nắng vàng ươm của vùng biển mùa hè, hai tâm hồn xa lạ vô tình va vào nhau. 
            Một người đang trốn chạy quá khứ, một người đang tìm kiếm tương lai. 
            Summer Love là bản tình ca nhẹ nhàng về những rung động đầu đời, về nụ hôn mang vị muối biển và những lời hứa dưới ánh hoàng hôn.
			
			Mùa hè rồi cũng sẽ qua đi, nhưng ký ức về tình yêu năm ấy sẽ còn mãi. 
			Một bộ phim chữa lành tâm hồn, đưa bạn trở về với những xúc cảm thuần khiết nhất của trái tim.
			
			"Có những tình yêu chỉ nảy nở dưới ánh nắng mùa hè, nhưng lại sưởi ấm cả một đời người."
            </li>
            """
        ));

        newsData.put("8", new News(
            8,
            "Comedy Nights mang tiếng cười trở lại",
            "09/12/2025",
            "image8.jpg",
            """
            <li>Chuyện gì sẽ xảy ra khi một nhóm "thánh nhọ" tụ tập lại trong một đêm duy nhất? 
            Khi những kế hoạch hoàn hảo biến thành những thảm họa nực cười, và những hiểu lầm chồng chất lên nhau tạo nên một chuỗi sự kiện không thể kiểm soát.
			
			Comedy Nights là một bữa tiệc tiếng cười đúng nghĩa với những tình huống "khó đỡ",
			 những câu thoại cực lầy và những cú lật kèo khiến bạn chỉ biết ôm bụng cười vang.
			 Hãy quên hết áp lực công việc đi, vì đêm nay, nụ cười là ưu tiên số 1!
			
			"Đừng mang theo muộn phiền vào rạp, vì chúng tôi chỉ có chỗ cho những tràng cười!
			</li>
            """
        ));
        
        newsData.put("8", new News(
                8,
                "Tuần lễ phim bom tấn cuối năm 2025",
                "09/12/2025",
                "image9.jpg",
                """
                <li>Chuyện gì sẽ xảy ra khi một nhóm "thánh nhọ" tụ tập lại trong một đêm duy nhất? 
                Khi những kế hoạch hoàn hảo biến thành những thảm họa nực cười, và những hiểu lầm chồng chất lên nhau tạo nên một chuỗi sự kiện không thể kiểm soát.
    			
    			Comedy Nights là một bữa tiệc tiếng cười đúng nghĩa với những tình huống "khó đỡ",
    			 những câu thoại cực lầy và những cú lật kèo khiến bạn chỉ biết ôm bụng cười vang.
    			 Hãy quên hết áp lực công việc đi, vì đêm nay, nụ cười là ưu tiên số 1!
    			
    			"Đừng mang theo muộn phiền vào rạp, vì chúng tôi chỉ có chỗ cho những tràng cười!
    			</li>
                """
            ));
        newsData.put("9", new News(
                9,
                "Tuần lễ phim bom tấn cuối năm 2025",
                "09/12/2025",
                "image9.jpg",
                """
                <p>TIÊU ĐỀ: TẠM BIỆT 2025 – BÙNG NỔ CÙNG NHỮNG SIÊU PHẨM ĐỈNH CAO!</p>
                <li>Năm 2025 đang dần khép lại, bạn đã sẵn sàng cho một "cú nổ" thực sự tại rạp chưa? Hãy cùng [Tên Rạp Của Bạn] bước vào hành trình điện ảnh không thể quên với Tuần Lễ Phim Bom Tấn Cuối Năm.
					Đây là cơ hội cuối cùng để bạn:
					Thưởng thức lại những siêu phẩm lừng lẫy đã làm mưa làm gió phòng vé suốt năm qua.
					Đón đầu những bộ phim mới nhất vừa mới "cập bến" để chào đón năm 2026.
					Trải nghiệm công nghệ âm thanh, hình ảnh sống động nhất – nơi những cú nổ, những pha hành động trở nên chân thực hơn bao giờ hết.
					Dù bạn là fan của dòng phim hành động nghẹt thở như BLAST, hay muốn đắm chìm trong sự kỳ bí của MYSTERY ISLAND, hoặc đơn giản là tìm kiếm sự ngọt ngào cùng SUMMER LOVE, chúng tôi đều có chỗ dành cho bạn!
				</li>
                """
            ));
        News news = newsData.get(id);

        if (news == null) {
            resp.sendRedirect(req.getContextPath() + "/news");
            return;
        }

        req.setAttribute("news", news);
        req.getRequestDispatcher("/news-detail.jsp").forward(req, resp);
    }
}
