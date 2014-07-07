<?php
require_once('../system/mysql.class.php');
class RecommendProductList
{
	var $db = null;
    var $Product_ID = null;
    var $Recommend_Category_ID = null;

    function __construct(){
        $this->db = new MySQL(true);
        if ($this->db->Error()) $this->db->Kill();
    }


    /**
     * Add a product to a recommend category
     *
     * @return boolean Returns TRUE on success or FALSE on error
     */  
    public function add()
    {
        $values=null;
        $values["product_id"] = MySQL::SQLValue($this->Product_ID,MySQL::SQLVALUE_NUMBER);
        $values["recommend_category_id"] = MySQL::SQLValue($this->Recommend_Category_ID,MySQL::SQLVALUE_NUMBER);

        $where=null;
        $where = [" product_id = " . MySQL::SQLValue($this->Product_ID,MySQL::SQLVALUE_NUMBER) . " AND recommend_category_id = " . MySQL::SQLValue($this->Recommend_Category_ID,MySQL::SQLVALUE_NUMBER)];

        // return $this->db->InsertRow("RecommendProductList", $values);      
        return $this->db->AutoInsertUpdate("RecommendProductList", $values, $where);
    }

}