/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Furniture;
import HelperClasses.RetailProduct;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author royst
 */
@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})

public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String result;
        try {
            Long storeID = (Long) session.getAttribute("countryID");
            String SKU = request.getParameter("SKU");
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.countryentity")
                    .path("getQuantity")
                    .queryParam("storeID", storeID)
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response res1 = invocationBuilder.get();

            if (res1.getStatus() == 200) {
                int quantity = 1;

                String id = request.getParameter("id");
                String price1 = request.getParameter("price");
                String name = request.getParameter("name");
                String image = request.getParameter("imageURL");
                ShoppingCartLineItem i = new ShoppingCartLineItem();
                System.out.println("pass3");
                i.setId(id);
                double price = Double.parseDouble(price1);
                i.setSKU(SKU);
                i.setPrice(price);
                i.setCountryID(storeID);
                i.setQuantity(quantity);
                i.setImageURL(image);
                i.setName(name);
//                ArrayList<ShoppingCartLineItem> inCart = (ArrayList<ShoppingCartLineItem>) (session.getAttribute("shoppingCart"));
//                if (inCart != null) {
//                    for (int k = 0; k < inCart.size(); k++) {
//                        if (inCart.get(k).getSKU().equals(SKU)) {
//                            quantity++;
//                        }
//                    }
//                }
                ArrayList<ShoppingCartLineItem> Cart = new ArrayList<ShoppingCartLineItem>();
                Cart.add(i);
                session.setAttribute("shoppingCart", Cart);
                result = "Item seccessfully added into cart!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
            } else {
                result = "Failed to add to cart.";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
            }
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
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
