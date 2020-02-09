/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

/**
 *
 * @author twk77
 */
public class CommerceDB {
    private EntityManager em;
    private static String connection = "jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345";
    
    public static boolean createECommerceLineItemRecord(int itemID,int quantity,Long salesId) {
        int newlineItemId;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");
            String insertIntoLineItem = "insert into lineitementity(QUANTITY,ITEM_ID) values(?,?);";
            PreparedStatement ps = conn.prepareStatement(insertIntoLineItem, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, quantity);
            ps.setDouble(2, itemID);
            ps.executeUpdate();

            ResultSet rs1 = ps.getGeneratedKeys();
            if (rs1.next()) {
                newlineItemId = rs1.getInt(1);
                System.out.println("Generated Line item id is: " + newlineItemId);
                System.out.println("Insert into Line_item entity successfully");
            } else {
                conn.close();
                newlineItemId=0;
                System.out.println("fail at 1");
                
            }

            //insert new record to storagebinlineitem database
            //link the lineitem info to storage bin (outbond)
            String insertStorageBinLineItem = "insert into storagebinentity_lineitementity values(21,?);";
            ps = conn.prepareStatement(insertStorageBinLineItem);
            ps.setInt(1, newlineItemId);
            int rs2 = ps.executeUpdate();

            if (rs2 > 0) {
                System.out.println("Insert into storagebinentity_lineitementity successfully");
            } else {
                conn.close();
                System.out.println("fail at 2");
            }

            //insert record to salesrecordentity_lineitementity table, link salesOrder and line item together 
            String insertSL = "insert into salesrecordentity_lineitementity values(?,?);";
            ps = conn.prepareStatement(insertSL);
            ps.setInt(1, salesId.intValue());
            ps.setInt(2, newlineItemId);
            int rsinsertSL = ps.executeUpdate();
            if (rsinsertSL > 0) {
                System.out.println("Insert into salesrecordentity_lineitementity successfully");
            } else {
                conn.close();
                System.out.println("fail at 3");
            }

            //update lineitem quantity (deduct the quantity)
            //first get the id of lineitem, that need to be updated
            List<Integer> lineItemsTobeUpdateIDs = new ArrayList<Integer>();
            int stock = 0;
            int lastrecordQuantityToDeduct = quantity;
            String getLineItemId = "SELECT l.* FROM "
                    + "storeentity s, warehouseentity w, storagebinentity sb, storagebinentity_lineitementity sbli, lineitementity l, itementity i "
                    + "where s.WAREHOUSE_ID=w.ID and w.ID=sb.WAREHOUSE_ID and sb.ID=sbli.StorageBinEntity_ID and sbli.lineItems_ID=l.ID "
                    + "and l.ITEM_ID=i.ID and s.ID=10001 and i.id=? order by l.quantity desc;";

            ps = conn.prepareStatement(getLineItemId);
            ps.setInt(1, itemID);
            ResultSet rsLineItemtoUpdate = ps.executeQuery();
            while (rsLineItemtoUpdate.next()) {
                lastrecordQuantityToDeduct = quantity - stock;
                stock += rsLineItemtoUpdate.getInt("QUANTITY");
                lineItemsTobeUpdateIDs.add(rsLineItemtoUpdate.getInt("ID"));
                //the first record can satisfy the deduction request
                if (quantity <= stock) {
                    break;
                }
            }

            //only need to update 1 record
            if (lineItemsTobeUpdateIDs.size() == 1) {
                //update line item quantity
                String updateLineItem = "Update lineitementity set lineitementity.QUANTITY = QUANTITY - ? where lineitementity.id = ?";
                ps = conn.prepareStatement(updateLineItem);
                ps.setInt(1, quantity);
                ps.setInt(2, lineItemsTobeUpdateIDs.get(0));
                int rsupdateLineItem = ps.executeUpdate();
                if (rsupdateLineItem > 0) {

                    System.out.println("Update lineItem quantity successfully");
                } else {
                    conn.close();
                     System.out.println("fail at 4");
                }
            } else if (lineItemsTobeUpdateIDs.size() > 1) {
                for (int i = 0; i < lineItemsTobeUpdateIDs.size() - 1; i++) {
                    String updateLineItem1 = "Update lineitementity set lineitementity.QUANTITY = 0 where lineitementity.id = ?";
                    ps = conn.prepareStatement(updateLineItem1);
                    ps.setInt(1, lineItemsTobeUpdateIDs.get(i));
                    int rsupdateLineItem1 = ps.executeUpdate();
                    if (rsupdateLineItem1 > 0) {
                        System.out.println("Update lineItem quantity successfully 1");
                    } else {
                        conn.close();
                         System.out.println("fail at 5");
                    }
                }

                String updateLineItem2 = "Update lineitementity set lineitementity.QUANTITY = QUANTITY - ? where lineitementity.id = ?";
                ps = conn.prepareStatement(updateLineItem2);
                ps.setInt(1, lastrecordQuantityToDeduct);
                ps.setInt(2, lineItemsTobeUpdateIDs.get(lineItemsTobeUpdateIDs.size() - 1));
                int rsupdateLineItem1 = ps.executeUpdate();
                if (rsupdateLineItem1 > 0) {
                    System.out.println("Update lineItem quantity successfully 2");
                } else {
                    conn.close();
                     System.out.println("fail at 6");
                }
            } else {
                conn.close();
                 System.out.println("fail at 7");
            }

            
            //update storagebinenity table, (add free volume for warehouse)
            String upadtestoragebinentityAdd = "Update storagebinentity set storagebinentity.freeVolume = freeVolume + ? where id = 20;";
            ps = conn.prepareStatement(upadtestoragebinentityAdd);
            ps.setInt(1, quantity);
            int rsAddFreeVolume = ps.executeUpdate();

            if (rsAddFreeVolume > 0) {
                System.out.println("Update storagebinentity free volume successfully");
            } else {
                conn.close();
                 System.out.println("fail at 8");
            }
            
            //update storagebinentity table, (deduct free volume)
            String upadtestoragebinentity = "Update storagebinentity set storagebinentity.freeVolume = freeVolume - ? where id = 9;";
            ps = conn.prepareStatement(upadtestoragebinentity);
            ps.setInt(1, quantity);
            int rs3 = ps.executeUpdate();

            if (rs3 > 0) {
                conn.close();
                System.out.println("Update storagebinentity successfully");
                return true;
                //return Response.status(201).build();
            } else {
                conn.close();
                 System.out.println("fail at 9");
                 return false;
                //return Response.status(404).build();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        //return Response.status(Response.Status.NOT_FOUND).build();
        
    }
}
