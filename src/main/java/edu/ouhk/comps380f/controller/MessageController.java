package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.dao.MessageRepository;
import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.Message;
import edu.ouhk.comps380f.model.MessageReply;
import edu.ouhk.comps380f.model.MessageUser;
import edu.ouhk.comps380f.view.DownloadingView;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("message")
public class MessageController {

    @Autowired
    MessageRepository gbEntryRepo;

    private volatile long MESSAGE_ID_SEQUENCE = 1;
    private Map<Long, Message> messageDatabase = new LinkedHashMap<>();
    private Message msg = new Message();

    //copy from TicketController
   /* @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("entries", gbEntryRepo.findAll());
        return "list";
    }*/
    
    @RequestMapping(value = {"{category}/list"}, method = RequestMethod.GET)
    public String list(@PathVariable("category") String category, ModelMap model) {

         model.addAttribute("entries", gbEntryRepo.findAllTopic(category));
        //  modelAndView.addObject("category", category);
        return "list";
    }
    
    @RequestMapping(value = {"", "index"}, method = RequestMethod.GET)
    public ModelAndView index() {
        //System.out.println("1111111111" + form.getCategory());
        return new ModelAndView("index", "ticketForm", new Form());
    }
   
    @RequestMapping(value = {"", "index"}, method = RequestMethod.POST)
    public Object index(Form form) throws IOException {
        //Message ticket = new Message();
        msg.setCategory(form.getCategory());
        System.out.println("1111111111" + form.getCategory());
        return msg;
    }
    

    @RequestMapping(value = "{category}/view/{messageId}", method = RequestMethod.GET)
    public String view(@PathVariable("messageId") int messageId, @PathVariable("category") String category, ModelMap model) {
        model.addAttribute("topic", gbEntryRepo.findById(messageId));
        model.addAttribute("attachment",gbEntryRepo.findAttachmentTopicById(messageId));
        model.addAttribute("entries", gbEntryRepo.findAllReply(messageId));
        return "view";
    }

    public static class ReplyForm {

        private String body;
        private List<MultipartFile> attachments;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }

    }



    public static class Form {

        private String subject;
        private String body;
        private List<MultipartFile> attachments;
        private String category;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }
    }
    
    //-------------------------REPLY MESSAGE----------------------------------------------
    
    @RequestMapping(value = "{category}/view/{messageId}/reply", method = RequestMethod.GET)
    public ModelAndView addReplyForm(@PathVariable("messageId") int messageId) {
        return new ModelAndView("reply", "replyForm", new Form());
    }

    @RequestMapping(value = "{category}/view/{messageId}/reply", method = RequestMethod.POST)
    public View replyHandle(@PathVariable("messageId") int messageId, ReplyForm form, Principal principal) throws IOException {
        MessageReply reply = new MessageReply();
        MessageUser user = new MessageUser();
        //message.setId(form.this.getNextTicketId());
        reply.setCustomerName(principal.getName());
        reply.setBody(form.getBody());
        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                reply.addAttachment(attachment);
            }
        }
        reply.setDate(new Date());
        gbEntryRepo.reply(messageId, reply, user);
        return new RedirectView("/message/", true);
    }

    //-----------------------CREATE TOPIC--------------------------------------------
    
    @RequestMapping(value = "{category}/create", method = RequestMethod.GET)
    public ModelAndView addCommentForm() {
        return new ModelAndView("add", "messageForm", new Form());
    }
    
    @RequestMapping(value = "{category}/create", method = RequestMethod.POST)
    public View addCommentHandle(@PathVariable("category") String category, Form form, Principal principal) throws IOException {
        Message message = new Message();
        MessageUser user = new MessageUser();
        //message.setId(form.this.getNextTicketId());
        message.setCustomerName(principal.getName());
        message.setCategory(category);
        message.setSubject(form.getSubject());
        message.setBody(form.getBody());

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                message.getAttachments().add(attachment);
            }
        }
        message.setDate(new Date());
        gbEntryRepo.create(message, user);
        return new RedirectView("/message/{category}/list", true);
    }

    
    //--------------------------EDIT REPLY----------------------------------------------
    @RequestMapping(value = "edit/reply/{messageId}", method = RequestMethod.GET)
    public ModelAndView showReplyEdit(@PathVariable("messageId") int messageId,
            Principal principal, HttpServletRequest request, ModelMap model) {
        MessageReply message = new MessageReply();
        if (message == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(message.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/message", true));
        }

        ModelAndView modelAndView = new ModelAndView("edit");
        model.addAttribute("message", gbEntryRepo.findById(messageId));
        modelAndView.addObject("messageId", messageId);
  
        return new ModelAndView("replyedit", "editForm", new ReplyForm());
       // return modelAndView;
    }

    @RequestMapping(value = "edit/reply/{messageId}", method = RequestMethod.POST)
    public View replyEdit(@PathVariable("messageId") int messageId, ReplyForm form,
            Principal principal, HttpServletRequest request)
            throws IOException {
        MessageReply message = new MessageReply();
        if (message == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(message.getCustomerName()))) {
            return new RedirectView("/message", true);
        }
        
        message.setBody(form.getBody());
        message.setId(messageId);

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                message.addAttachment(attachment);
            }
        }
        message.setDate(new Date());
        gbEntryRepo.updateReply(message);
        return new RedirectView("/message/", true);
    }
    
    
    
    //----------------------------EDIT TOPIC----------------------------------------------
    @RequestMapping(value = "edit/topic/{messageId}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("messageId") int messageId,
            Principal principal, HttpServletRequest request, ModelMap model) {
        Message message = new Message();
        if (message == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(message.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/message", true));
        }

        ModelAndView modelAndView = new ModelAndView("edit");
        model.addAttribute("message", gbEntryRepo.findById(messageId));
        modelAndView.addObject("messageId", messageId);
  
        return new ModelAndView("edit", "editForm", new Form());
       // return modelAndView;
    }

    @RequestMapping(value = "edit/topic/{messageId}", method = RequestMethod.POST)
    public View edit(@PathVariable("messageId") int messageId, Form form,
            Principal principal, HttpServletRequest request)
            throws IOException {
        Message message = new Message();
        if (message == null || 
                (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(message.getCustomerName()))) {
            return new RedirectView("/message", true);
        }
        message.setSubject(form.getSubject());
        message.setBody(form.getBody());
        message.setId(messageId);

        for (MultipartFile filePart : form.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if (attachment.getName() != null && attachment.getName().length() > 0
                    && attachment.getContents() != null && attachment.getContents().length > 0) {
                message.getAttachments().add(attachment);
            }
        }
        message.setDate(new Date());
        gbEntryRepo.updateTopic(message);
        return new RedirectView("/message/", true);
    }
    
    //-------------------------------ATTACHMENT----------------------------------------------
    @RequestMapping(
            value = "topic/{messageId}/attachment/{attachment:.+}",
            method = RequestMethod.GET
    )

    public View download(@PathVariable("messageId") int messageId,
            @PathVariable("attachment") String name) {
        //Message message = new Message();
        List<Attachment> attachmentList = new ArrayList();
        attachmentList = gbEntryRepo.findAttachmentTopicById(messageId);
        if (attachmentList != null) {
            for (int i=0; i< attachmentList.size(); i++){
                if(attachmentList.get(i).getName().equals(name)){
                    Attachment attachment = attachmentList.get(i);
                    if (attachment != null) {
                return new DownloadingView(attachment.getName(),
                        attachment.getMimeContentType(), attachment.getContents());
            }
                }
            }
            //Attachment attachment = message.getAttachments().get(messageId);
            
        }
        return new RedirectView("/message/", true);
    }

    @RequestMapping(
            value = "topic/{messageId}/delete/{attachment:.+}",
            method = RequestMethod.GET
    )
    public View deleteAttachment(@PathVariable("messageId") long ticketId,
            @PathVariable("attachment") String name) {
        Message message = new Message();
        if (message != null) {
         /*   if (message.hasAttachment(name)) {
                message.deleteAttachment(name);
            }*/
        }
        return new RedirectView("/message/" , true);
    }
    
    
    //---------------------------DELETE TOPIC AND REPLY------------------------------------
    @RequestMapping(value = "delete/topic/{messageId}", method = RequestMethod.GET)
    public View deleteTopic(@PathVariable("messageId") int messageId) {
        gbEntryRepo.deleteTopic(messageId);
        return new RedirectView("/message", true);
    }
    @RequestMapping(value = "delete/reply/{messageId}", method = RequestMethod.GET)
    public View deleteReply(@PathVariable("messageId") int messageId) {
        gbEntryRepo.deleteReply(messageId);
        return new RedirectView("/message", true);
    }
}
