package com.example.vnpay.controller;

import com.example.vnpay.request.VnpayRequest;
import com.example.vnpay.service.VnpayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {

    private final VnpayService vnpayService;

    public PaymentController(VnpayService vnpayService) {
        this.vnpayService = vnpayService;
    }

    // Hiển thị form thanh toán
    @GetMapping("/payment")
    public String paymentForm() {
        return "paymentForm";
    }

    // Xử lý form thanh toán
    @PostMapping("/payment")
    public String createPayment(@ModelAttribute VnpayRequest vnpayRequest, Model model) {
        try {
            String paymentUrl = vnpayService.createPayment(vnpayRequest);
            model.addAttribute("paymentUrl", paymentUrl);
            return "paymentResult";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "paymentForm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi tạo thanh toán: " + e.getMessage());
            return "paymentForm";
        }
    }

    // Xử lý kết quả trả về từ VNPay (ví dụ: sau khi thanh toán xong)
    @GetMapping("/payment/return")
    public String paymentReturn(@RequestParam("vnp_ResponseCode") String responseCode, Model model) {
        if ("00".equals(responseCode)) {
            model.addAttribute("message", "Thanh toán thành công!");
        } else {
            model.addAttribute("message", "Thanh toán thất bại! Mã lỗi: " + responseCode);
        }
        return "paymentResult";
    }
}
