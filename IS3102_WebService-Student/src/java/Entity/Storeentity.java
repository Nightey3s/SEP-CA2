/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason
 */
@Entity
@Table(name = "storeentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Storeentity.findAll", query = "SELECT s FROM Storeentity s"),
    @NamedQuery(name = "Storeentity.findById", query = "SELECT s FROM Storeentity s WHERE s.id = :id"),
    @NamedQuery(name = "Storeentity.findByIsdeleted", query = "SELECT s FROM Storeentity s WHERE s.isdeleted = :isdeleted")})
public class Storeentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ADDRESS")
    private String address;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Lob
    @Size(max = 2147483647)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ISDELETED")
    private Boolean isdeleted;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "NAME")
    private String name;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "POSTALCODE")
    private String postalcode;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "TELEPHONE")
    private String telephone;
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
    @ManyToOne
    private Countryentity countryId;
    @JoinColumn(name = "REGIONALOFFICE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Regionalofficeentity regionalofficeId;
    @JoinColumn(name = "WAREHOUSE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Warehouseentity warehouseId;

    public Storeentity() {
    }

    public Storeentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Countryentity getCountryId() {
        return countryId;
    }

    public void setCountryId(Countryentity countryId) {
        this.countryId = countryId;
    }

    public Regionalofficeentity getRegionalofficeId() {
        return regionalofficeId;
    }

    public void setRegionalofficeId(Regionalofficeentity regionalofficeId) {
        this.regionalofficeId = regionalofficeId;
    }

    public Warehouseentity getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Warehouseentity warehouseId) {
        this.warehouseId = warehouseId;
    }
    
    @GET
    @Path("getQuantity")
    @Produces({"application/json"})
    public Response getItemQuantity(@QueryParam("storeID") Long storeID, @QueryParam("SKU") String SKU) {
        try {

            ProductDB product = new ProductDB();
            Response res = product.checkAvailability(SKU, storeID);
            if (res.getStatus() == 200) {
                System.out.println("pass");
                return Response.ok(MediaType.APPLICATION_JSON).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Storeentity)) {
            return false;
        }
        Storeentity other = (Storeentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Storeentity[ id=" + id + " ]";
    }
    
}
