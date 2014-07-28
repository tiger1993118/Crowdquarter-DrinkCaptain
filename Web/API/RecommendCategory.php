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
              ' and rc.mood_category_id = ' . MySQL::SQLValue($this->Mood_Category_ID,MySQL::SQLVALUE_NUMBER) .
              ' order by p.name';
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
                ' and RecommendCategory.mood_category_id = ' . MySQL::SQLValue($this->Mood_Category_ID,MySQL::SQLVALUE_NUMBER) . ' ) as rp ON Product.product_id = rp.product_id '.
                ' where Product.product_category_id = ' . MySQL::SQLValue($this->Product_Category_ID,MySQL::SQLVALUE_NUMBER) . ' order by Product.name';
        return $this->db->Query($sql)->fetch_all(MYSQLI_ASSOC);
    }
    /**
     * Get the recommend_category_id of a combination of weekday and mood 
     * if the combination exists, return the ID, 
     * if the combination doesn't exist, insert it to database and return the ID
     *
     * @return INTEGER return the recommend_category_id
     */
    public function getIDByWeekdayAndMood()
    {
        $where=null;
        $where["weekday"] = MySQL::SQLValue($this->Weekday, MySQL::SQLVALUE_NUMBER);
        $where["mood_category_id"] = MySQL::SQLValue($this->Mood_Category_ID, MySQL::SQLVALUE_NUMBER);
        $result = $this->db->SelectRows("RecommendCategory", $where);
        if ($this->db->RowCount() > 0)
        {
          return $this->db->RecordsArray()[0]['recommend_category_id'];
        }
        else
        {
          $this->add();
          return $this->ID;
        }        
    }
    
    
    /**
     * Add a new recommend category to database
     *
     * @return boolean Returns TRUE on success or FALSE on error
     */  
    public function add()
    {
        $values=null;
        $values["weekday"] = MySQL::SQLValue($this->Weekday, MySQL::SQLVALUE_NUMBER);
        $values["mood_category_id"] = MySQL::SQLValue($this->Mood_Category_ID, MySQL::SQLVALUE_NUMBER);
        $result = $this->db->InsertRow("RecommendCategory", $values);
        $this->ID = $this->db->GetLastInsertID();
        return $result;      
    }    
}