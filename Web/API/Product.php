<?php
require_once('../system/mysql.class.php');
class Product
{
    var $db = null;
    var $ID = null;
    var $Name = null;
    var $Price = null;
    var $Volume = null;
    var $ImageURL = null;
    var $ProductCategoryID = null;
    var $LCBOProductID = null;

    function __construct(){
        $this->db = new MySQL(true);
        if ($this->db->Error()) $this->db->Kill();
    }

    /**
     * Get a product detail information by ID
     *
     * @return JSON return the product information in JSON format, it will include all columns in the table
     */    
    public function getByID()
    {
        //Now return a hard coded JSON data
//         $product = '{
//   "Product": [
//     {
//       "id": 1,
//       "name": "Molson Canadian",
//       "volume": "2838",
//       "price": "1350",
//       "imageURL": "http:\/\/lcbo.com\/assets\/products\/720x720\/0300699.jpg"
//     }
//   ]
// }';
      //read data from real database
        $sql = 'Select product_id as "id", name, volume, price, imageURL, lcbo_product_id from Product Where product_id = ' . MySQL::SQLValue($this->ID,MySQL::SQLVALUE_NUMBER);
        $result = $this->db->Query($sql)->fetch_all(MYSQLI_ASSOC);
        $product = json_encode(array("Product" => $result));
        return $product;
    }

    /**
     * Add a new product to database 
     *
     * @return integer Returns last insert ID on success or FALSE on failure, -1 for product already exist
     */  
    public function add()
    {
        if ($this->productExists())
        {
            return -1;
        }
        $values=null;
        $values["name"] = MySQL::SQLValue($this->Name,MySQL::SQLVALUE_TEXT);
        $values["price"] = MySQL::SQLValue($this->Price,MySQL::SQLVALUE_NUMBER);
        $values["volume"] = MySQL::SQLValue($this->Volume,MySQL::SQLVALUE_NUMBER);
        $values["imageURL"] = MySQL::SQLValue($this->ImageURL,MySQL::SQLVALUE_TEXT);
        $values["product_category_id"] = MySQL::SQLValue($this->ProductCategoryID,MySQL::SQLVALUE_NUMBER);
        $values["lcbo_product_id"] = MySQL::SQLValue($this->LCBOProductID,MySQL::SQLVALUE_NUMBER);

        return $this->db->InsertRow("Product", $values);      
    }

    /**
     * check if the product already in database by LCBO product ID 
     *
     * @return Boolean Returns TRUE if product exists or FALSE on not existing product
     */ 
    public function productExists()
    {
        $where=null;
        $where["lcbo_product_id"] = MySQL::SQLValue($this->LCBOProductID,MySQL::SQLVALUE_NUMBER);
        
        $this->db->SelectRows("Product", $where);
        if ($this->db->RowCount()==0) {
            return false;
        }
        else {
            return true;
        }
        
    }
 
     /**
     * Update a product 
     *
     * @return boolean Returns TRUE on success or FALSE on error
     */  
    public function update()
    {
        $values=null;
        $values["name"] = MySQL::SQLValue($this->Name,MySQL::SQLVALUE_TEXT);
        $values["price"] = MySQL::SQLValue($this->Price,MySQL::SQLVALUE_NUMBER);
        $values["volume"] = MySQL::SQLValue($this->Volume,MySQL::SQLVALUE_NUMBER);
        $values["imageURL"] = MySQL::SQLValue($this->ImageURL,MySQL::SQLVALUE_TEXT);

        $where=null;
        $where["product_id"] = MySQL::SQLValue($this->ID,MySQL::SQLVALUE_NUMBER);

        return $this->db->UpdateRows("Product", $values, $where);      
    }  

     /**
     * Delete a product 
     *
     * @return boolean Returns TRUE on success or FALSE on error
     */  
    public function delete()
    {
        $where=null;
        $where["product_id"] = MySQL::SQLValue($this->ID,MySQL::SQLVALUE_NUMBER);

        //the table RecommendProductList is set to cascade delete in database
        return $this->db->DeleteRows("Product", $where);      
    }    
    
  /**
   * Get all products
   *
   * @return ARRAY return the products in associate array
   */
    public function all()
    {
      return $this->db->Query('Select * from Product')->fetch_all(MYSQLI_ASSOC);
    }  

  /**
   * Get all products of a category
   *
   * @return ARRAY return the products of a category in associate array
   */
    public function getByProductCategory()
    {
      return $this->db->Query('Select * from Product where product_category_id = ' . MySQL::SQLValue($this->ProductCategoryID,MySQL::SQLVALUE_NUMBER))->fetch_all(MYSQLI_ASSOC);
    } 

}