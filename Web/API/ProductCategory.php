<?php
require_once('../system/mysql.class.php');
class ProductCategory
{
    var $db = null;
    var $ID = null;
    var $Name = null;

    function __construct(){
        $this->db = new MySQL(true);
        if ($this->db->Error()) $this->db->Kill();
    }

	/**
	 * Get all product categories, such as Wine, Beer
	 *
	 * @return JSON return the categories in JSON format
	 */
    public function getProductCategoryList()
    {
        //Now return a hard coded JSON data
//         $product_category_list = '{
//   "ProductCategory": [
//     {
//       "id": 1,
//       "name": "Wine"
//     },
//     {
//       "id": 2,
//       "name": "Beer"
//     },
//     {
//       "id": 3,
//       "name": "Rum"
//     }
//   ]
// }';
      //read data from real database
      $result = $this->db->Query('Select product_category_id as "id", name from ProductCategory')->fetch_all(MYSQLI_ASSOC);
      $product_category_list = json_encode(array("ProductCategory" => $result));
        return $product_category_list;    	
    }


    /**
     * Add a new product to database
     *
     * @return boolean Returns TRUE on success or FALSE on error
     */  
    public function add()
    {
        $values=null;
        $values["name"] = MySQL::SQLValue($this->Name,MySQL::SQLVALUE_TEXT);
        $result = $this->db->InsertRow("ProductCategory", $values);
        $this->ID = $this->db->GetLastInsertID();
        return true;      
    }

    /**
     * Get the product category ID by name, if the category exists, return the ID.
     * If the category does not exist, add it to database and return the new ID.
     * It is used while important data from LCBO, since the category name from LCBO is a string, 
     * we need to save them to the table ProductCategory and use the ID in the table Product.
     * 
     * @return integer the ID of the category name
     */  
    public function getIDByName()
    {
        $values=null;
        $values = [" name = " . MySQL::SQLValue($this->Name,MySQL::SQLVALUE_TEXT)];
        $result = $this->db->SelectRows("ProductCategory", $values);
        if ($this->db->RowCount() > 0)
        {
          return $this->db->RecordsArray()[0]['product_category_id'];
        }
        else
        {
          $this->add();
          return $this->ID;
        }
    }

  /**
   * Get all product categories
   *
   * @return ARRAY return the product categories in associate array
   */
    public function all()
    {
      return $this->db->Query('Select * from ProductCategory')->fetch_all(MYSQLI_ASSOC);
    }  
}