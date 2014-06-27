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
        $product_category_list = '{
  "ProductCategory": [
    {
      "id": 1,
      "name": "Wine"
    },
    {
      "id": 2,
      "name": "Beer"
    },
    {
      "id": 3,
      "name": "Rum"
    }
  ]
}';
        return $product_category_list;    	
    }

}