package com.vn.beta_testing.feature.email_service.service;

import java.nio.charset.StandardCharsets;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(MailSender mailSender,
            JavaMailSender javaMailSender,
            SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendSimpleEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("ads.hoidanit@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        this.mailSender.send(msg);
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }
    }

    @Async
    public void sendEmailFromTemplateSync(
            String to,
            String subject,
            String templateName,
            String username,
            Object value) {

        Context context = new Context();
        context.setVariable("name", username);
        context.setVariable("jobs", value);

        String content = templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }

    public void sendEmailForgotPassword(
            String to,
            String subject,
            String content) {
        this.sendEmailSync(to, subject, content, false, true);
    }

    @Async
    public void sendAccountInfoEmail(String to, String username, String password) {
        try {
            String subject = "Thông tin tài khoản truy cập hệ thống BetaTesting";
            String content = String.format("""
                    <html>
                        <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                            <h2 style="color: #4CAF50;">Chào bạn, %s!</h2>
                            <p>Tài khoản của bạn đã được tạo thành công trên hệ thống <b>BetaTesting</b>.</p>
                            <p>Thông tin đăng nhập của bạn:</p>
                            <ul>
                                <li><b>Email:</b> %s</li>
                                <li><b>Mật khẩu:</b> %s</li>
                            </ul>
                            <p>Bạn có thể đăng nhập tại:
                                <a href="https://betatesting.vn/login" target="_blank">https://betatesting.vn/login</a>
                            </p>
                            <p style="color: gray; font-size: 13px;">
                                Nếu bạn không yêu cầu tạo tài khoản này, vui lòng bỏ qua email này.
                            </p>
                            <hr />
                            <p style="font-size: 12px; color: #999;">
                                © 2025 BetaTesting Team
                            </p>
                        </body>
                    </html>
                    """, username, to, password);

            sendEmailSync(to, subject, content, false, true);
            System.out.println("✅ Gửi email tài khoản thành công tới: " + to);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email tài khoản: " + e.getMessage());
        }
    }

    @Async
    public void sendCampaignInvitationEmail(String to, String campaignTitle, String companyName, String joinLink) {
        try {
            String subject = "Thư mời tham gia chiến dịch BetaTesting: " + campaignTitle;

            String content = String.format(
                    """
                                <html>
                                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                        <div style="max-width: 600px; margin: auto; border: 1px solid #e0e0e0; padding: 20px; border-radius: 10px;">
                                            <h2 style="color: #4CAF50; text-align: center;">Lời mời tham gia chiến dịch thử nghiệm</h2>

                                            <p>Xin chào,</p>
                                            <p>Bạn nhận được lời mời tham gia chiến dịch <b>%s</b> từ công ty <b>%s</b> trên hệ thống <b>BetaTesting</b>.</p>

                                            <p>Hãy cùng đóng góp ý kiến, trải nghiệm sản phẩm, và nhận thưởng hấp dẫn khi tham gia!</p>

                                            <div style="text-align: center; margin: 30px 0;">
                                                <a href="%s" target="_blank"
                                                   style="background-color: #4CAF50; color: white; text-decoration: none; padding: 12px 24px; border-radius: 6px;">
                                                   👉 Tham gia ngay
                                                </a>
                                            </div>

                                            <p style="font-size: 14px; color: #555;">
                                                Nếu bạn không quan tâm đến chiến dịch này, có thể bỏ qua email này.
                                            </p>

                                            <hr style="border: none; border-top: 1px solid #ddd; margin-top: 30px;" />

                                            <p style="font-size: 12px; color: #999; text-align: center;">
                                                © 2025 BetaTesting — Nền tảng quản lý chiến dịch thử nghiệm phần mềm.
                                            </p>
                                        </div>
                                    </body>
                                </html>
                            """,
                    campaignTitle, companyName, joinLink);

            sendEmailSync(to, subject, content, false, true);
            System.out.println("✅ Đã gửi lời mời tham gia chiến dịch tới: " + to);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email mời tham gia chiến dịch: " + e.getMessage());
        }
    }

    public void sendCampaignInvitationEmailCustom(String to, String campaignTitle, String companyName,
            String customMessage, String joinLink) {
        try {
            String subject = "Thư mời tham gia chiến dịch BetaTesting: " + campaignTitle;

            String content = String.format(
                    """
                                <html>
                                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                        <div style="max-width: 600px; margin: auto; border: 1px solid #e0e0e0; padding: 20px; border-radius: 10px;">
                                            <h2 style="color: #4CAF50; text-align: center;">Lời mời tham gia chiến dịch thử nghiệm</h2>

                                            <p>Xin chào,</p>
                                            <p>Bạn nhận được lời mời tham gia chiến dịch <b>%s</b> từ công ty <b>%s</b> trên hệ thống <b>BetaTesting</b>.</p>
                                           <div>%s</div>
                                            <p>Hãy cùng đóng góp ý kiến, trải nghiệm sản phẩm, và nhận thưởng hấp dẫn khi tham gia!</p>

                                            <div style="text-align: center; margin: 30px 0;">
                                                <a href="%s" target="_blank"
                                                   style="background-color: #4CAF50; color: white; text-decoration: none; padding: 12px 24px; border-radius: 6px;">
                                                   👉 Tham gia ngay
                                                </a>
                                            </div>

                                            <p style="font-size: 14px; color: #555;">
                                                Nếu bạn không quan tâm đến chiến dịch này, có thể bỏ qua email này.
                                            </p>

                                            <hr style="border: none; border-top: 1px solid #ddd; margin-top: 30px;" />

                                            <p style="font-size: 12px; color: #999; text-align: center;">
                                                © 2025 BetaTesting — Nền tảng quản lý chiến dịch thử nghiệm phần mềm.
                                            </p>
                                        </div>
                                    </body>
                                </html>
                            """,
                    campaignTitle, companyName, customMessage, joinLink);

            sendEmailSync(to, subject, content, false, true);
            System.out.println("✅ Đã gửi lời mời tham gia chiến dịch tới: " + to);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email mời tham gia chiến dịch: " + e.getMessage());
        }
    }

}
