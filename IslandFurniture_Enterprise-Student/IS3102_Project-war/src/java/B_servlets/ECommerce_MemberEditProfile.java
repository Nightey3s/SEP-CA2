/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author twk77
 */
@WebServlet(name = "ECommerce_MemberEditProfile", urlPatterns = {"/ECommerce_MemberEditProfile"})
public class ECommerce_MemberEditProfile extends HttpServlet {
    
    
    private String message;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html/charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            System.out.println("test");
            HttpSession session = request.getSession();
            
            String name = (String) request.getParameter("name");
            System.out.println(name);
            String phone = (String) request.getParameter("phone");
            String address = (String) request.getParameter("address");
            String securityQuestion = request.getParameter("securityQuestion");
            int securityQ = Integer.parseInt(securityQuestion);
            String securityAnswer = (String) request.getParameter("securityAnswer");
            String ageinput = request.getParameter("age");
            int age = Integer.parseInt(ageinput);
            String incomeinput = request.getParameter("income");
            int income = Integer.parseInt(incomeinput);
            String email = (String) session.getAttribute("memberEmail");
            System.out.println("current");
            String country = (String) request.getParameter("country");
            System.out.println(country);
            
            
            Client client = ClientBuilder.newClient();
           
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                    .path("updateMember")
                    .queryParam("name", name)
                    .queryParam("phone", phone)
                    .queryParam("address", address)
                    .queryParam("securityQuestion", securityQ)
                    .queryParam("securityAnswer", securityAnswer)
                    .queryParam("age", age)
                    .queryParam("income", income)
                    .queryParam("email", email);
                    
          
            
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            
            Response resultResponse = invocationBuilder.post(null);
            System.out.println(resultResponse);
            
            if(resultResponse.getStatus() == 200){
                System.out.println("current3");
                Member member = new Member();
                member.setName(name);
                member.setPhone(phone);
                member.setAddress(address);
                member.setSecurityQuestion(securityQ);
                member.setSecurityAnswer(securityAnswer);
                member.setAge(age);
                member.setIncome(income);
                member.setEmail(email);
                member.setCity((String)session.getAttribute("countryName"));
                session.setAttribute("member",member);
                message = "Account Updated Successfully.";
                response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?GoodMsg="+message);
            }else{
                message = "Update of Profile Failed.";
                response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?BadMsg="+message);
            }
            
        }catch(Exception e){
            out.println(e);
            message = "Update of Profile Failed1.";
            response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?BadMsg="+message);
        }
    }//End of processRequest 

    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
