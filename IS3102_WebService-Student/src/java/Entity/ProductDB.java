/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.sql.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author royst
 */
public class ProductDB {

    public ProductDB() {
    }

    public Response checkAvailability(String SKU, Long storeID) {
        try {
            System.out.println(storeID + "  " + SKU + "this is even more gay damn gay");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "SELECT sum(l.QUANTITY) as sum FROM storeentity s, warehouseentity w, storagebinentity sb, storagebinentity_lineitementity sbli, lineitementity l, itementity i, countryentity c where s.WAREHOUSE_ID=w.ID and w.ID=sb.WAREHOUSE_ID and sb.ID=sbli.StorageBinEntity_ID and sbli.lineItems_ID=l.ID and l.ITEM_ID=i.ID and i.SKU=? and c.ID=?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setString(1, SKU);
            ps.setLong(2, storeID);
            ResultSet rs = ps.executeQuery();
            int qty = 0;
            if (rs.next()) {
                qty = rs.getInt("sum");
            }
            
                System.out.println(qty);
            if (qty > 0) {
                System.out.println(qty);

                return Response.ok(MediaType.APPLICATION_JSON).build();
            } else {
                System.out.println("gay");
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
