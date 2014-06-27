<?php
require_once('../system/mysql.class.php');
class User
{
    var $ID = null;
    var $Email = null;
    var $FName = null;
    var $LName = null;
    var $db = null;
    var $iURL = null;
    var $FBid = null;
    var $TWid = null;
    var $FBDisplay = null;
    var $TWDisplay = null;
    var $TWIURL = null;
    var $Toke = null;

    function __construct(){
        $this->db = new MySQL(true);
        if ($this->db->Error()) $this->db->Kill();
    }
    function insertFB($data,$token,$tokenExp)
    {
        if($this->loadEmailUser()==0)
        {
            $this->insertEmailUser();
        }
        //echo $this->ID;
        $values=null;
        $values["idBFB"] = MySQL::SQLValue($this->FBid,MySQL::SQLVALUE_TEXT);
        $values["FBdisplayName"] = MySQL::SQLValue($this->FBDisplay,MySQL::SQLVALUE_TEXT);
        $values["rawData"] = MySQL::SQLValue($data,MySQL::SQLVALUE_TEXT);
        $values["Token"] = MySQL::SQLValue($token,MySQL::SQLVALUE_TEXT);
        $values["tokenExpiry"] = MySQL::SQLValue($tokenExp,MySQL::SQLVALUE_DATETIME);
        $values["idBUser"] = MySQL::SQLValue($this->ID,MySQL::SQLVALUE_NUMBER);
        $result = $this->db->InsertRow("BFB", $values);
    }
}
?>