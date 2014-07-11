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
	 * Get all recommend product in a recommend category, such as Monday/Summer BBQ/Wine
	 *
	 * @return JSON return the recommend product in JSON format, it will only return the id, name, volume and price in the list
	 */
    public function getRecommendProductList()
    {

        //Now return a hard coded JSON data
//         $product_list = '{
//   "RecommendProductList": [
//     {
//       "id": 1,
//       "name": "Molson Canadian",
//       "volume": "2838",
//       "price": "1350"
//     },
//     {
//       "id": 2,
//       "name": "Old Milwaukee",
//       "volume": "2838",
//       "price": "1100"
//     },
//     {
//       "id": 3,
//       "name": "Budweiser",
//       "volume": "235",
//       "price": "473"
//     }
//   ]
// }';
      //Get the recommend product list from real database
      $sql = 'Select p.product_id as id, p.name, p.volume, p.price, p.imageURL from Product as p ' .
              'join RecommendProductList rpl on p.product_id = rpl.product_id ' .
              'join RecommendCategory rc on rc.recommend_category_id = rpl.recommend_category_id '.
              'where p.product_category_id = ' . MySQL::SQLValue($this->Product_Category_ID,MySQL::SQLVALUE_NUMBER) . 
              ' and rc.weekday = ' . MySQL::SQLValue($this->Weekday,MySQL::SQLVALUE_NUMBER) . 
              ' and rc.mood_category_id = ' . MySQL::SQLValue($this->Mood_Category_ID,MySQL::SQLVALUE_NUMBER);
      $result = $this->db->Query($sql)->fetch_all(MYSQLI_ASSOC);
      $product_list = json_encode(array("RecommendProductList" => $result));
      return $product_list;
    }

  /**
   * Get all recommend categories
   *
   * @return ARRAY return the recommend categories in associate array
   */
    public function all()
    {
      return $this->db->Query('Select * from RecommendCategory')->fetch_all(MYSQLI_ASSOC);
    }    

    /**
     * Get all products and the recommend status of a recommend category, 
     * if the product in the specific recommend list (weekday/mood/product category), 
     * the recommend_category_id is the id, otherwise the recommend_category_id is null
     *
     * @return ARRAY return the product list in associate array
     */
    public function getProductsForRecommend()
    {
        $sql = 'select Product.*, rp.recommend_category_id from Product left join ' .
            '(select Product.product_id, RecommendProductList.recommend_category_id from Product ' .
                'join RecommendProductList on Product.product_id = RecommendProductList.product_id ' .
                'join RecommendCategory on RecommendProductList.recommend_category_id = RecommendCategory.recommend_category_id ' .
                'where Product.product_category_id = ' . MySQL::SQLValue($this->Product_Category_ID,MySQL::SQLVALUE_NUMBER) . ' and RecommendCategory.weekday = ' . MySQL::SQLValue($this->Weekday,MySQL::SQLVALUE_NUMBER) .
                ' and RecommendCategory.mood_category_id = ' . MySQL::SQLValue($this->Mood_Category_ID,MySQL::SQLVALUE_NUMBER) . ' ) as rp ON Product.product_id = rp.product_id order by Product.name';
        return $this->db->Query($sql)->fetch_all(MYSQLI_ASSOC);
    }
    
}