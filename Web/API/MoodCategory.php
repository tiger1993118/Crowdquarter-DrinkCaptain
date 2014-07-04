<?php
require_once('../system/mysql.class.php');
class MoodCategory
{
    var $db = null;
    var $ID = null;
    var $Name = null;

    function __construct(){
        $this->db = new MySQL(true);
        if ($this->db->Error()) $this->db->Kill();
    }

	/**
	 * Get all mood categories, such as Bad Day, Game Night
	 *
	 * @return JSON return the categories in JSON format
	 */
    public function getMoodCategoryList()
    {

      //read data from real database
      $result = $this->db->Query('Select mood_category_id as "id", name, description from MoodCategory')->fetch_all(MYSQLI_ASSOC);
      $mood_category_list = json_encode(array("MoodCategory" => $result));
        return $mood_category_list;    	
    }
}
?>