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
        $product = '{
  "Product": [
    {
      "id": 1,
      "name": "Molson Canadian",
      "volume": "2838",
      "price": "1350",
      "imageURL": "http:\/\/lcbo.com\/assets\/products\/720x720\/0300699.jpg"
    }
  ]
}';
        return $product;
    }

}