/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbaccess;

import Entity.Storeentity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author twk77
 */
public class StoreDB {
    private EntityManager em;
    private static String connection = "jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345";

    public StoreDB(EntityManager em)
    {
        this.em = em;
    }
    
    public List<Storeentity> listAllStores(EntityManager em) {
        Query q = em.createQuery("Select s from Storeentity s where s.isdeleted=FALSE and s.countryId.name='Singapore'");
        List<Storeentity> list = q.getResultList();
        for (Storeentity s : list) {
            em.detach(s);
            s.setCountryId(null);
            s.setRegionalofficeId(null);
            s.setWarehouseId(null);
        }
        List<Storeentity> list2 = new ArrayList();
        list2.add(list.get(0));
        return list;
    }
    
    public int getItemQuantityOfStore( Long storeId, String SKU) {
        int qty = 0;
        try {
            Connection conn = DriverManager.getConnection(connection);
            String stmt = "SELECT sum(l.QUANTITY) as sum FROM storeentity s, warehouseentity w, storagebinentity sb, storagebinentity_lineitementity sbli, lineitementity l, itementity i where s.WAREHOUSE_ID=w.ID and w.ID=sb.WAREHOUSE_ID and sb.ID=sbli.StorageBinEntity_ID and sbli.lineItems_ID=l.ID and l.ITEM_ID=i.ID and s.ID=? and i.SKU=?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, storeId);
            ps.setString(2, SKU);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                qty = rs.getInt("sum");
            }

            return qty;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }    
     
     public static String getStoreInfo(Long storeId)
     {
        try {
            Connection conn = DriverManager.getConnection(connection);
            String stmt = "SELECT * FROM storeentity s where s.ID = ?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, storeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String info = "StoreName: ";
                info += rs.getString("NAME");
                info += "; Address: ";
                info += rs.getString("ADDRESS");
                info += " (";
                info += rs.getString("POSTALCODE");
                info += ") ";
                conn.close();
                return info;
            }else{
                return null;
            }
            
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }
     }
     
     
    
}

