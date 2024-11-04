package za.co.oldmutual.asisa.email;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class MailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	private EmailConfigProperties emailConfigProperties;

	public boolean sendEmail(String toAddress, String mailType, Map<String, Object> mailData) {
		String mailSubject = null;
		String htmlMailText = "";
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/emailTemplate/");
			Template mailTemplate = null;

			switch (mailType) {
			case "UPDATE":
				mailTemplate = freemarkerConfig.getTemplate("updateNotification.ftl");
				mailSubject = emailConfigProperties.getUpdateMailSubject();
				break;
			case "DELETE":
				mailTemplate = freemarkerConfig.getTemplate("deleteNotification.ftl");
				mailSubject = emailConfigProperties.getDeleteMailSubject();
				break;
			case "UPDATE_REJECT":
				mailTemplate = freemarkerConfig.getTemplate("updateRejectNotification.ftl");
				mailSubject = emailConfigProperties.getUpdateRejectMailSubject();
				break;
			case "DELETE_REJECT":
				mailTemplate = freemarkerConfig.getTemplate("deleteRejectNotification.ftl");
				mailSubject = emailConfigProperties.getDeletRejectMailSubject();
				break;
			default:
				LOGGER.error("Mail Template not present for the request of mailType {}", mailType);
				return false;
			}
			htmlMailText = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, mailData);
			helper.setFrom(emailConfigProperties.getFromAddress());
			helper.setTo(toAddress + "@oldmutual.com");
			helper.setText(htmlMailText, true);
			helper.setSubject(mailSubject);
			LOGGER.debug("Sending email notification is [{}] with the contents [{}]", emailConfigProperties.isEnabled(),
					message);
			if (emailConfigProperties.isEnabled()) {
				javaMailSender.send(message);
			}

			return true;
		} catch (Exception e) {
			LOGGER.error("Exception Occurred while sending email notification. The message is [{}]", e.getMessage());
			return false;
		}
	}
}
