<?php
require_once('../system/mysql.class.php');
class RecommendCategory
{
	var $db = null;
    var $ID = null;
    var $Weekday = null;
    var $Mood_Category_ID = null;
    var $Product_Category_ID = null;

    function __construct(){
        $this->db = new MySQL(true);
        if ($this->db->Error()) $this->db->Kill();
    }


	/**
	 * Get all recommend product in a recommend category, such as Monday/Summer BBQ
	 *
	 * @return JSON return the recommend product in JSON format, it will only return the id, name, volume and price in the list
	 */
    public function getRecommendProductList()
    {

        //Now return a hard coded JSON data
        $product_list = '{
  "RecommendProductList": [
    {
      "id": 1,
      "name": "Molson Canadian",
      "volume": "2838",
      "price": "1350"
    },
    {
      "id": 2,
      "name": "Old Milwaukee",
      "volume": "2838",
      "price": "1100"
    },
    {
      "id": 3,
      "name": "Budweiser",
      "volume": "235",
      "price": "473"
    }
  ]
}';
        return $product_list;
    }

}