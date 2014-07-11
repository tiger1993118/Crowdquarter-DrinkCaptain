<?php
require_once('../system/mysql.class.php');
class MoodCategory
{
    var $db = null;
    var $ID = null;
    var $Name = null;
    var $Description = null;

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
    
  /**
   * Get all mood categories
   *
   * @return ARRAY return the mood categories in associate array
   */
    public function all()
    {
      return $this->db->Query('Select * from MoodCategory')->fetch_all(MYSQLI_ASSOC);
    } 

   /**
   * Update a mood category
   *
   * @return Boolean Returns TRUE on success or FALSE on error
   */
    public function update()
    {
        $values=null;
        $values["name"] = MySQL::SQLValue($this->Name,MySQL::SQLVALUE_TEXT);
        $values["description"] = MySQL::SQLValue($this->Description,MySQL::SQLVALUE_TEXT);
        $where=null;
        $where["mood_category_id"] =  MySQL::SQLValue($this->ID,MySQL::SQLVALUE_NUMBER);

        return $this->db->UpdateRows("MoodCategory", $values, $where);          
    }

    /**
   * Add a mood category
   *
   * @return integer Returns last insert ID on success or FALSE on failure
   */
    public function add()
    {
        $values=null;
        $values["name"] = MySQL::SQLValue($this->Name,MySQL::SQLVALUE_TEXT);
        $values["description"] = MySQL::SQLValue($this->Description,MySQL::SQLVALUE_TEXT);

        return $this->db->InsertRow("MoodCategory", $values);          
    }
    
}
?>