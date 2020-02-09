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
@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession();
        try {
            String id = request.getParameter("id");
            String SKU = request.getParameter("SKU");
            double price = Double.parseDouble(request.getParameter("price"));
            String name = request.getParameter("name");
            String imageURL = request.getParameter("imageURL");
            Long countryID = (Long) s.getAttribute("countryID");

            Long storeID = Long.parseLong("10001");
            int itemQty = getQuantity(storeID, SKU);

            //for item available in the stock
            if (itemQty > 0) {
                // Get Cart from Session.
                List<ShoppingCartLineItem> cart = (List<ShoppingCartLineItem>) request.getSession().getAttribute("myCart");
                // If null, create it.
                if (cart == null) {
                    cart = new ArrayList<>();
                    request.getSession().setAttribute("myCart", cart);
                }
                boolean isInCart = false;
                for (ShoppingCartLineItem item : cart) {
                    //for item already in the cart
                    if (item.getSKU().equals(SKU)) {
                        isInCart = true;
                        //check whether has more stock for this item
                        if (itemQty - item.getQuantity() == 0) {
                            String result = "Item not added to cart, not enough quantity available.111";
                            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
                        } else {
                            item.setQuantity(item.getQuantity() + 1);
                            String result = "Item quantity increased successfully!";
                            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
                        }
                    }
                }
                
            //if the item is newly added into the shopping cart
            if (isInCart == false) {
                    ShoppingCartLineItem cartItem = new ShoppingCartLineItem();
                    cartItem.setId(id);
                    cartItem.setSKU(SKU);
                    cartItem.setPrice(price);
                    cartItem.setName(name);
                    cartItem.setQuantity(1);
                    cartItem.setCountryID(countryID);
                    cartItem.setImageURL(imageURL);
                    cart.add(cartItem);
                } 
            
             s.setAttribute("shoppingCart", cart);
                String result = "Item successfully added into the cart!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
            }
            //not enough stock
            else{
                String result = "Item not added to cart, not enough quantity available.";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getQuantity(Long storeID, String SKU) {
        try {
            System.out.println("getQuantity() SKU: " + SKU);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity")
                    .path("getQuantity")
                    .queryParam("storeID", storeID)
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            System.out.println("Result returned from ws: " + result);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
    
     // <editor-fold defaultstate="collapsed" desc="others">
    /* protected void processRequest(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException{
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            System.out.println("seee");
            try {
                HttpSession session = request.getSession();
                
                String id = request.getParameter("id");
                String SKU = request.getParameter("SKU");
                String price = request.getParameter("price");
                String name = request.getParameter("name");
                String imageURL = request.getParameter("imageURL");
                
                Long storeID = 10001L;
                
                ShoppingCartLineItem singleCart = new ShoppingCartLineItem();
                
                ArrayList<ShoppingCartLineItem> shoppingCart = (ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart");
                
                int quantity = getQuantity(storeID,SKU);
                
                singleCart.setId(id);
                singleCart.setSKU(SKU);
                singleCart.setName(name);
                singleCart.setPrice(Double.parseDouble(price));
                singleCart.setImageURL(imageURL);
                singleCart.setQuantity(1);
                
                if(quantity < 1){
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Item not added to cart.");
                    return;
                }
                
                if(shoppingCart == null){
                    shoppingCart = new ArrayList<ShoppingCartLineItem>();
                    shoppingCart.add(singleCart);
            }else if(!shoppingCart.contains(singleCart)){
                shoppingCart.add(singleCart);
            }else if(shoppingCart.contains(singleCart)){
                for(ShoppingCartLineItem lineItem : shoppingCart){
                    if(lineItem.equals(singleCart)){
                        if(quantity < lineItem.getQuantity() + 1){
                            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Item not added to cart");
                            return;
                        }else{
                            lineItem.setQuantity(lineItem.getQuantity() + 1);
                        }
                    }
                }
            }
                session.setAttribute("shoppingCart",shoppingCart);
                
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=Item successfully added into the cart");
    }catch(Exception ex){
        out.println(ex);
        ex.printStackTrace();
        response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=Invalid Response.");
    }
    

}
    
    public int getQuantity(Long storeID, String SKU){
        try{
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_Webservice-Student/webresources/entity.storeentity")
                    .path("getQuantity")
                    .queryParam("storeID", storeID)
                    .queryParam("SKU", SKU);
            
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response res = invocationBuilder.get();
            
            int result = Integer.parseInt((String)res.readEntity(String.class));
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }*/// </editor-fold>
    /*protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String result;
        try {
            Long storeID = (Long) session.getAttribute("countryID");
            String SKU = request.getParameter("SKU");
            System.out.println(storeID + "  " + SKU + "this is gay");
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.countryentity")
                    .path("getQuantity")
                    .queryParam("storeID", storeID)
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response res1 = invocationBuilder.get();

            if (res1.getStatus() == 200) {
                String id = request.getParameter("id");
                String price1 = request.getParameter("price");
                String name = request.getParameter("name");
                String image = request.getParameter("imageURL");
                ShoppingCartLineItem i = new ShoppingCartLineItem();
                System.out.println("pass3");
                i.setId(id);
                double price = Double.parseDouble(price1);
                i.setPrice(price);
                System.out.println("pass31");
                i.setCountryID(storeID);
                i.setImageURL(image);
                System.out.println("pass31");
                i.setName(name);
                System.out.println("pass31");
                List<ShoppingCartLineItem> Cart = new ArrayList<ShoppingCartLineItem>();
                Cart.add(i);
                session.setAttribute("shoppingCart", Cart);
                System.out.println("pass32");
                result = "Item seccessfully added into cart!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
            } else {
                result = "Failed to add to cart.";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + result);
            }
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
        }
    }*/
    
    
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
