/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author twk77
 */
@WebServlet(name = "ECommerce_MinusFurnitureToListServlet", urlPatterns = {"/ECommerce_MinusFurnitureToListServlet"})
public class ECommerce_MinusFurnitureToListServlet extends HttpServlet {

    
//    protected void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        HttpSession ses = request.getSession();
//        try{
//            
//            out.println(ses.getAttribute("shoppingCart"));
//            
//             
//            String SKU = request.getParameter("SKU");
//            ArrayList<ShoppingCartLineItem>shoppingCart = (ArrayList<ShoppingCartLineItem>)ses.getAttribute("shoppingCart");
//            
//            if(shoppingCart == null){
//                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=You have no items in your shopping cart.");
//            }
//            else{
//                for(ShoppingCartLineItem lineItem : shoppingCart){
//                   if(lineItem.getSKU().equals(SKU)){
//                       if(lineItem.getQuantity()>1){
//                           lineItem.setQuantity(lineItem.getQuantity() - 1);
//                       }else{
//                           response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=The quantity cannot be lower than 1.");
//                           return;
//                       }
//                       break;
//                   }
//                }
//            }
//            
//            ses.setAttribute("shoppingCart",shoppingCart);
//            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg="+"Item quantity reduced successfully!");
//        }catch(Exception ex){
//            out.println(ex);
//            ex.printStackTrace();
//            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg="+"Item quantity reduced unsuccessfully");
//        }
//    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession();
        try {
            //get data
            String id = request.getParameter("id");
            String SKU = request.getParameter("SKU");
            String result = " ";
            
            List<ShoppingCartLineItem> cart = (List<ShoppingCartLineItem>) request.getSession().getAttribute("myCart");
            
            for (ShoppingCartLineItem item : cart) {
                if (item.getSKU().equals(SKU)) {
                    //check if more than 1
                    if (item.getQuantity() > 1) {
                        //minus
                        item.setQuantity(item.getQuantity() - 1 );
                        result = "Item quantity reduced successfully!";
                        response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result); 
                    }
                    //check quantity = 1
                    else if (item.getQuantity() == 1 ) {
                        result = "Error. Quantity cannot be less than 1.";
                        response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
                    }
                }
            } 
        }catch (Exception ex) {
                ex.printStackTrace();
                }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
